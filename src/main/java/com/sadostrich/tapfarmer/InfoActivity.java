package com.sadostrich.tapfarmer;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class InfoActivity extends Activity
{
    private final int GAME_ID = 10;
    private final int TROPHY_ID = 11;
    private final int SETTINGS_ID = 12;
    private final int INFO_ID = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

        getActionBar().setDisplayShowTitleEnabled(false);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ImageView imageView = (ImageView) findViewById(R.id.tutorial);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setClass(InfoActivity.this, Tutorial.class);
                startActivity(intent);
            }
        });
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
        settingsIcon.setIcon(R.drawable.settings);
        settingsIcon.setTitle("Stats");
        settingsIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem infoIcon = menu.add(Menu.NONE, INFO_ID, Menu.NONE, "");
        infoIcon.setIcon(R.drawable.information_selected);
        infoIcon.setTitle("Information");
        infoIcon.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        infoIcon.setEnabled(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == GAME_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(InfoActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == TROPHY_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(InfoActivity.this, AchievementsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == SETTINGS_ID)
        {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(InfoActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
