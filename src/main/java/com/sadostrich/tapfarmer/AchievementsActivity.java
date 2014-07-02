package com.sadostrich.tapfarmer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AchievementsActivity extends Activity
{
    private final int GAME_ID = 10;
    private final int TROPHY_ID = 11;
    private final int SETTINGS_ID = 12;
    private final int INFO_ID = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        float height = getResources().getConfiguration().screenHeightDp;

        getActionBar().setDisplayShowTitleEnabled(false);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Main layout
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);

        //Unlocked Achievements text view
        TextView unlockedText = new TextView(this);
        unlockedText.setText("(" + Game.getInstance().getUnlockedAchievements().size() + ")" + " Unlocked Achievements:");
        unlockedText.setBackgroundResource(R.drawable.sky_title);
        unlockedText.setTextColor(Color.WHITE);
        unlockedText.setTextSize(height * 0.045f);

        //Container for unlocked achievements
        FrameLayout unlockedContainer = new FrameLayout(this);
        unlockedContainer.setBackgroundResource(R.drawable.sky_list);
        unlockedContainer.setId(3);

        //Locked Achievements text view
        TextView lockedText = new TextView(this);
        lockedText.setText("(" + Game.getInstance().getLockedAchievements().size() + ")" +" Locked Achievements:");
        lockedText.setBackgroundResource(R.drawable.grass_title);
        lockedText.setTextColor(Color.WHITE);
        lockedText.setTextSize(height * 0.045f);

        //Container for locked achievements
        FrameLayout lockedContainer = new FrameLayout(this);
        lockedContainer.setBackgroundResource(R.drawable.grass_list);
        lockedContainer.setId(4);

        mainLayout.addView(unlockedText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mainLayout.addView(unlockedContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
        mainLayout.addView(lockedText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mainLayout.addView(lockedContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));

        UnlockedList unlockedList = new UnlockedList();
        LockedList lockedList = new LockedList();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(unlockedContainer.getId(), unlockedList);
        transaction.add(lockedContainer.getId(), lockedList);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem gameIcon = menu.add(Menu.NONE, GAME_ID, Menu.NONE, "");
        gameIcon.setIcon(R.drawable.piggy_bank_icon);
        gameIcon.setTitle("Tap Farmer");
        gameIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem trophyIcon = menu.add(Menu.NONE, TROPHY_ID, Menu.NONE, "");
        trophyIcon.setIcon(R.drawable.trophy_selected);
        trophyIcon.setTitle("Achievements");
        trophyIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        trophyIcon.setEnabled(false);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == GAME_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(AchievementsActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == SETTINGS_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(AchievementsActivity.this, SettingsActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == INFO_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(AchievementsActivity.this, InfoActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class UnlockedList extends ListFragment implements ListAdapter
    {
        public UnlockedList(){}

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setListAdapter(this);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            float height = getResources().getConfiguration().screenHeightDp;

            //Unlocked achievements container
            LinearLayout unlockedItem = new LinearLayout(getActivity());
            unlockedItem.setOrientation(LinearLayout.HORIZONTAL);
            unlockedItem.setMinimumHeight((int) (height * 0.30f));

            //Layout of each list item
            TextView achName = new TextView(getActivity());
            if(Game.getInstance().getUnlockedAchievements().size() > 0)
                achName.setText(Game.getInstance().getUnlockedAchievements().get(i).getTitle());
            achName.setTextSize(height * 0.04f);
            achName.setGravity(Gravity.CENTER);
            achName.setTextColor(Color.WHITE);

            unlockedItem.addView(achName, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));

            //Display item info when long clicked
            unlockedItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(Game.getInstance().getUnlockedAchievements().get(getListView().getPositionForView(view)).getTitle());
                    builder.setMessage(Game.getInstance().getUnlockedAchievements().get(getListView().getPositionForView(view)).getDescription());
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });

            unlockedItem.setBackgroundColor(0xDD002900);
            getListView().setDividerHeight((int) (height * 0.05f));

            return unlockedItem;
        }

        @Override
        public int getCount()
        {
            return Game.getInstance().getUnlockedAchievements().size();
        }

        @Override
        public Object getItem(int i) {
            return Game.getInstance().getUnlockedAchievements().get(i);
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
        public boolean isEnabled(int i) {
            return false;
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

    public class LockedList extends ListFragment implements ListAdapter
    {
        public LockedList(){}

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setListAdapter(this);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            float height = getResources().getConfiguration().screenHeightDp;

            LinearLayout lockedItem = new LinearLayout(getActivity());
            lockedItem.setOrientation(LinearLayout.HORIZONTAL);
            lockedItem.setMinimumHeight((int) (height * 0.30f));

            TextView achName = new TextView(getActivity());
            if(Game.getInstance().getLockedAchievements().size() > 0)
                achName.setText(Game.getInstance().getLockedAchievements().get(i).getTitle());
            achName.setTextSize(height * 0.04f);
            achName.setGravity(Gravity.CENTER);
            achName.setTextColor(Color.WHITE);

            lockedItem.addView(achName, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));

            //Display item info when long clicked
            lockedItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(Game.getInstance().getLockedAchievements().get(getListView().getPositionForView(view)).getTitle());
                    builder.setMessage(Game.getInstance().getLockedAchievements().get(getListView().getPositionForView(view)).getDescription());
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });
            lockedItem.setBackgroundColor(0xDD002900);
            getListView().setDividerHeight((int) (height * 0.05f));

            return lockedItem;
        }

        @Override
        public int getCount()
        {
            return Game.getInstance().getLockedAchievements().size();
        }

        @Override
        public Object getItem(int i) {
            return Game.getInstance().getLockedAchievements().get(i);
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
        public boolean isEnabled(int i) {
            return false;
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
