package com.sadostrich.tapfarmer;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by Jace on 12/6/13.
 */
public class BackgroundMusicService extends IntentService
{
    public BackgroundMusicService()
    {
        super("BackgroundMusicService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        MediaPlayer player = MediaPlayer.create(this, R.drawable.bg_music);
        player.setLooping(false);
        player.setVolume(100, 100);
        player.start();
    }
}
