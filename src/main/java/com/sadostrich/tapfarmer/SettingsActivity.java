package com.sadostrich.tapfarmer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity
{
    private final int GAME_ID = 10;
    private final int TROPHY_ID = 11;
    private final int SETTINGS_ID = 12;
    private final int INFO_ID = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowTitleEnabled(false);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_settings);

        //Running total
        TextView coinsEarned = (TextView) findViewById(R.id.coinsEarned);
        //Current coins
        TextView coinsInBank = (TextView) findViewById(R.id.coinsInBank);
        //Total items bought
        TextView itemsBought = (TextView) findViewById(R.id.itemsBought);
        //Total upgrades bought
        TextView upgradesBought = (TextView) findViewById(R.id.upgradesBought);
        //Total finger taps
        TextView totalTaps = (TextView) findViewById(R.id.totalTaps);
        //Total coins spent
        TextView totalSpent = (TextView) findViewById(R.id.totalSpent);
        //Achievement Progress text
        TextView achievementProgressText = (TextView) findViewById(R.id.achievementProgress);
        //Achievement progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Achievement progress percentage
        TextView progressPercentage = (TextView) findViewById(R.id.progressPercentage);
        //Reset game button
        Button resetButton = (Button) findViewById(R.id.resetButton);

        coinsEarned.setText("Coins Earned: " + Beautify.CommaSeparate((int) Game.getInstance().getRunningTotal()));
        coinsInBank.setText("Coins in Bank: " + Beautify.CommaSeparate((int) (Game.getInstance().getTotal())));
        //Displays total owned of each item in data structure
        itemsBought.setText("Items Bought: " + (Game.getInstance().getItems().get(0).getOwned() + Game.getInstance().getItems().get(1).getOwned() +
                Game.getInstance().getItems().get(2).getOwned() + Game.getInstance().getItems().get(3).getOwned() + Game.getInstance().getItems().get(4).getOwned() +
                Game.getInstance().getItems().get(5).getOwned() + Game.getInstance().getItems().get(6).getOwned() + Game.getInstance().getItems().get(7).getOwned() +
                Game.getInstance().getItems().get(8).getOwned() + Game.getInstance().getItems().get(9).getOwned() + Game.getInstance().getItems().get(10).getOwned()));
        //Displays total number of bought upgrades
        upgradesBought.setText("Upgrades Bought: " + (((3 - Game.getInstance().getItems().get(0).getUpgrades().size()) + (3 - Game.getInstance().getItems().get(1).getUpgrades().size()) +
                (3 - Game.getInstance().getItems().get(2).getUpgrades().size()) + (3 - Game.getInstance().getItems().get(3).getUpgrades().size()) +
                (3 - Game.getInstance().getItems().get(4).getUpgrades().size()) + (3 - Game.getInstance().getItems().get(5).getUpgrades().size()) +
                (3 - Game.getInstance().getItems().get(6).getUpgrades().size()) + (3 - Game.getInstance().getItems().get(7).getUpgrades().size()) +
                (3 - Game.getInstance().getItems().get(8).getUpgrades().size()) + (3 - Game.getInstance().getItems().get(9).getUpgrades().size()) +
                (3 - Game.getInstance().getItems().get(10).getUpgrades().size()))));
        //Displays total number of taps
        totalTaps.setText("Total Taps: " + Beautify.CommaSeparate((int) (Game.getInstance().getTotalTaps())));
        //Displays total coins spent
        totalSpent.setText("Coins Spent: " + Beautify.CommaSeparate((int) (Game.getInstance().getTotalSpent())));

        int progress = (int)(100 * ((double)(Game.getInstance().getUnlockedAchievements().size()) / (double)((Game.getInstance().getLockedAchievements().size()) + (double)(Game.getInstance().getUnlockedAchievements().size()))));
        progressBar.setProgress(progress);
        progressBar.isShown();

        progressPercentage.setText(progress + "%");

        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Create and stylize first reset window
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Reset Game");
                builder.setMessage("By resetting the game, all of your game information will be erased, including " +
                        "total coins, items purchased, and unlocked achievements!\n\nWould you still like to reset?");

                //User chose not to reset. Exit and do nothing else
                builder.setPositiveButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });

                //User chose to reset. Confirm user action with another dialog window
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //Create and stylize second reset window
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                        builder2.setTitle("Warning!");
                        builder2.setMessage("Are you sure you want to reset?");

                        //User chose to reset.  Call method to erase all data
                        builder2.setNegativeButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                                Game.getInstance().resetGame();
                                finish();
                            }
                        });

                        //User chose not to reset. Exit and do nothing else
                        builder2.setPositiveButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                        });

                        //Create and show second window
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                    }
                });

                //Create and show first window
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
        trophyIcon.setIcon(R.drawable.trophy);
        trophyIcon.setTitle("Achievements");
        trophyIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem settingsIcon = menu.add(Menu.NONE, SETTINGS_ID, Menu.NONE, "");
        settingsIcon.setIcon(R.drawable.settings_selected);
        settingsIcon.setTitle("Stats");
        settingsIcon.setEnabled(false);
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
            intent.setClass(SettingsActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == TROPHY_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(SettingsActivity.this, AchievementsActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == INFO_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(SettingsActivity.this, InfoActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
