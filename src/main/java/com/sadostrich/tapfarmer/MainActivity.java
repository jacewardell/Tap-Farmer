package com.sadostrich.tapfarmer;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends Activity implements CpsListener
{
    private final int GAME_ID = 10;
    private final int TROPHY_ID = 11;
    private final int SETTINGS_ID = 12;
    private final int INFO_ID = 13;

    //--- GUI VARS ---//
    private TextView totalCoins;
    private TextView coinsPerSec;

    private ItemsList itemsList;

    public MainActivity(){ super();}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowTitleEnabled(false);
        float height = getResources().getConfiguration().screenHeightDp;

        //Lock screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try
        {
            Intent bgMusic = new Intent(this, BackgroundMusicService.class);
            startService(bgMusic);
        }catch(Exception e){}

        //Main layout creation
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);

        //Could be used for swiping between activities
        mainLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                //For swiping between activities
                return false;
            }
        });

        //Set the timer listener
        Game.getInstance().setCpsListener(this);

        //load a saved game if there is one.
        try
        {
            FileInputStream ins = openFileInput("game.dat");
            ObjectInputStream reader = new ObjectInputStream(ins);
            SavedGame load = (SavedGame)reader.readObject();
            Game.getInstance().loadFromSave(load);
            System.out.println("Game loaded!");
        }
        catch (Exception e)
        {
            System.out.println("ERROR NO Game loaded!");
        }

//        RelativeLayout gameLayout = (RelativeLayout)findViewById(R.layout.activity_main);

        //Upper layout containing piggy bank and totals
        LinearLayout gameLayout = new LinearLayout(this);
        gameLayout.setOrientation(LinearLayout.VERTICAL);
        gameLayout.setId(1);

        //Total coins view and layout
        totalCoins = new TextView(this);
        totalCoins.setBackgroundResource(R.drawable.sky);
        totalCoins.setTextSize(height * 0.05f);
        totalCoins.setTextColor(0xFFF0F0F0);
        totalCoins.setGravity(Gravity.CENTER);
        totalCoins.setText("Total: " + Game.getInstance().getTotal());
        gameLayout.addView(totalCoins, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        //Coins per second view and layout
        coinsPerSec = new TextView(this);
        coinsPerSec.setBackgroundResource(R.drawable.grass_fence);
        coinsPerSec.setTextSize(24);
        coinsPerSec.setTextColor(0xFFF0F0F0);
        coinsPerSec.setTextSize(height * 0.04f);
        coinsPerSec.setGravity(Gravity.CENTER);
        coinsPerSec.setText(Game.getInstance().getCps() + " CPS");
        gameLayout.addView(coinsPerSec, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        //Piggy bank image view layout
        LinearLayout.LayoutParams piggyLayout = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 10));
        piggyLayout.gravity = Gravity.CENTER;
        Bitmap piggyBankBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.piggy_bank);
        ImageView piggyBank = new ImageView(this);
        piggyBank.setBackgroundResource(R.drawable.mud_grass);
        piggyBank.setImageBitmap(piggyBankBitmap);
        gameLayout.addView(piggyBank, piggyLayout);

        //Listener for the piggy bank
        piggyBank.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //Play coin drop sound
                    MediaPlayer coinDrop = MediaPlayer.create(MainActivity.this, R.drawable.coin_drop);
                    coinDrop.setLooping(false);
                    coinDrop.setVolume(0.3f, 0.3f);
                    coinDrop.start();

                    //Add coins to bank
                    totalCoins.setText("Total: " + Beautify.CommaSeparate(((int)(Game.getInstance().tap()))));
                }
                return true;
            }
        });

        //Container for items
        FrameLayout itemContainer = new FrameLayout(this);
        itemContainer.setBackgroundColor(0xFF000000);
        itemContainer.setId(2);

        mainLayout.addView(gameLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        mainLayout.addView(itemContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        //Fragment containing list view
        itemsList = new ItemsList();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(itemContainer.getId(), itemsList);
        transaction.commit();
    }

    @Override
    public void TimerTick()
    {
        runOnUiThread(new Runnable() { //gotta update from the UI Thread.
            @Override
            public void run() {
                totalCoins.setText("Total: " + Beautify.CommaSeparate((int)(Game.getInstance().getTotal())));
                coinsPerSec.setText(Beautify.CommaSeparateDouble(Math.round(Game.getInstance().getCps() * 100.0) / 100.0) + " CPS");

                //When launching each activity only once, throws exception
                try{ itemsList.getListView().invalidateViews(); }
                catch(IllegalStateException e){}
            }
        });
    }

    @Override
    public void SaveGame()
    {
        System.out.println("Saving Game.");
        try
        {
            FileOutputStream os = openFileOutput("game.dat", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            SavedGame save = new SavedGame();
            output.writeObject(save);
            output.close();
        }
        catch(Exception e)
        {
            System.out.println("Error!");
//            System.out.println(e.getCause().getMessage().toString());
           Log.e("Error in saving", e.getMessage(), e);
        }
    }

    /** Sets up options menu, containing Achievements, Settings, and Info **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem gameIcon = menu.add(Menu.NONE, GAME_ID, Menu.NONE, "");
        gameIcon.setIcon(R.drawable.piggy_bank_icon_selected);
        gameIcon.setTitle("Tap Farmer");
        gameIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        gameIcon.setEnabled(false);

        MenuItem trophyIcon = menu.add(Menu.NONE, TROPHY_ID, Menu.NONE, "");
        trophyIcon.setIcon(R.drawable.trophy);
        trophyIcon.setTitle("Achievements");
        trophyIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem settingsIcon = menu.add(Menu.NONE, SETTINGS_ID, Menu.NONE, "");
        settingsIcon.setIcon(R.drawable.settings);
        settingsIcon.setTitle("Stats");
        settingsIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem infoIcon = menu.add(Menu.NONE, INFO_ID, Menu.NONE, "");
        infoIcon.setIcon(R.drawable.information);
        infoIcon.setTitle("Information");
        infoIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    /** Determines what option was selected **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Launch achievements activity
        if(item.getItemId() == TROPHY_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(MainActivity.this, AchievementsActivity.class);
            startActivity(intent);
            return true;
        }
        //Launch settings activity
        else if(item.getItemId() == SETTINGS_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        //Launch info activity
        else if(item.getItemId() == INFO_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(MainActivity.this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Do you wish to exit?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * List fragment and adapter containing the items that the player can purchase
     * to increase cps and total coins
     */
    public class ItemsList extends ListFragment implements ListAdapter
    {
        public ItemsList(){}

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            setListAdapter(this);
        }

         /**
          * Populates the list view with the appropriate items
          * @param i
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            if(i == 0)
            {
                View header = getLayoutInflater().inflate(R.layout.header, null);
                int[] colors = {0, 0xFF4e2700, 0};
                //header.setBackground(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
                return getLayoutInflater().inflate(R.layout.header, null);
            }
            else
            {
                //Column headers require this adjustment
                i--;

                float height = getResources().getConfiguration().screenHeightDp;

                int[] colors = {0, 0xFF4e2700, 0};
                getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
                getListView().setDividerHeight((int)(height * 0.05f));

                //Layout of each list item
                LinearLayout listItem = new LinearLayout(getActivity());
                listItem.setOrientation(LinearLayout.HORIZONTAL);
                listItem.setMinimumHeight((int)(height * 0.40f));

                //Layout of the price and upgrade button
                LinearLayout priceAndUpgrade = new LinearLayout(getActivity());
                priceAndUpgrade.setOrientation(LinearLayout.VERTICAL);
                priceAndUpgrade.setMinimumHeight((int) (height * 0.40f));

                //Displays the next purchasing price and enables item purchasing
                final Button nextPrice = new Button(getActivity());
                nextPrice.setText( Beautify.CommaSeparate(Game.getInstance().getItems().get(i).getPrice()) );
                nextPrice.setTextSize(height * 0.03f);
                nextPrice.setGravity(Gravity.CENTER);
                nextPrice.setTextColor(Color.WHITE);

                //Disable/enable purchase button depending on player's total
                if(Game.getInstance().getItems().get(i).getPrice() > Game.getInstance().getTotal())
                    nextPrice.setEnabled(false);
                else
                    nextPrice.setEnabled(true);

                //Purchase an item when clicked
                final int finalI = i;
                nextPrice.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Game.getInstance().getItems().get(finalI).purchase();
                        getListView().invalidateViews();
                    }
                });

                //Creates button used for upgrading
                Button upgrade = new Button(getActivity());
                upgrade.setText("Upgrade (" + Beautify.CommaSeparate(Game.getInstance().getItems().get(finalI).getUpgrades().peek().getPrice()) + ")");
                upgrade.setTextSize(height * 0.02f);
                upgrade.setGravity(Gravity.CENTER);
                upgrade.setTextColor(Color.WHITE);
                upgrade.setEnabled(false);

                //Check to see if each item currently has an upgrade or not
                if(Game.getInstance().getItems().get(i).getHasUpgrade() == false || Game.getInstance().getItems().get(i).getUpgrades().size() == 0 ||
                        Game.getInstance().getItems().get(i).getUpgrades().peek().getPrice() > Game.getInstance().getTotal() || Game.getInstance().getItems().get(i).getOwned() == 0)
                    upgrade.setEnabled(false);
                else
                    upgrade.setEnabled(true);

                //Provides user with ability to upgrade
                upgrade.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view)
                    {
                        final View tempView = view;

                        //Dialog window detailing upgrade and price
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(Game.getInstance().getItems().get(finalI).getUpgrades().peek().getNameAndAmount());
                        builder.setMessage(Game.getInstance().getItems().get(finalI).getUpgrades().peek().getDescription());
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("Upgrade", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Game.getInstance().getItems().get(finalI).upgrade(finalI);
                                getListView().invalidateViews();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                priceAndUpgrade.addView(nextPrice, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                priceAndUpgrade.addView(upgrade, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                //Displays the number of items the player owns
                TextView numberOwned = new TextView(getActivity());
                numberOwned.setText(Integer.toString(Game.getInstance().getItems().get(i).getOwned()));
                numberOwned.setTextSize(height * 0.03f);
                numberOwned.setGravity(Gravity.CENTER);
                numberOwned.setTextColor(Color.WHITE);

                //Displays the item's image
                Bitmap itemIconBitmap = BitmapFactory.decodeResource(getResources(), Game.getInstance().getItems().get(i).getResourcePath());
                ImageView itemIcon = new ImageView(getActivity());
                itemIcon.setImageBitmap(itemIconBitmap);

                listItem.addView(priceAndUpgrade, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 2));
                listItem.addView(numberOwned, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 2));
                listItem.addView(itemIcon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 1));

                //Display item info when long clicked
                listItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(Game.getInstance().getItems().get(getListView().getPositionForView(view) - 1).getName());
                        builder.setMessage(Game.getInstance().getItems().get(getListView().getPositionForView(view) - 1).getDescription());
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        return true;
                    }
                });
                 listItem.setBackgroundResource(R.drawable.list_item);

                return listItem;
            }
        }

        @Override
        public int getCount()
        {
            return Game.getInstance().getItems().size() + 1;
        }

        @Override
        public Object getItem(int i) {
            return Game.getInstance().getItems().get(i);
        }

        @Override

        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i)
        {
            if(i == 0)
                return false;
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }
    }
}