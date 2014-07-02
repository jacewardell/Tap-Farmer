package com.sadostrich.tapfarmer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jace Wardell on 12/20/13.
 */
public class Tutorial extends Activity
{
    ImageView tutorialContainer;
    ArrayList<Integer> tutorials;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_tutorial);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tutorialContainer = (ImageView) findViewById(R.id.tutorialWindow);
        tutorials = new ArrayList<Integer>();
        count = 0;

        //Add all items to the list
        setUpTutorial();

        //Changes image to next tutorial slide
        tutorialContainer.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(count == tutorials.size() - 1)
                    {
                        finish();
                    }
                    else
                        count++;

                    tutorialContainer.setImageBitmap(BitmapFactory.decodeResource(getResources(),tutorials.get(count)));
                }
                return false;
            }
        });
    }

    /**
     * Adds images to data structure and sets the image bitmap of the imageview to the first image
     */
    private void setUpTutorial()
    {
        //Bitmap first = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_welcome);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.outWidth = width;
        options.outHeight = height;

        Bitmap first = BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_welcome);

        tutorials.add(R.drawable.tutorial_welcome);
        tutorialContainer.setImageBitmap(first);

        tutorials.add(R.drawable.tutorial_total);

//        Bitmap third = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_cps);
        tutorials.add(R.drawable.tutorial_cps);

//        Bitmap fourth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_pig);
        tutorials.add(R.drawable.tutorial_pig);

//        Bitmap fifth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_items_box);
        tutorials.add(R.drawable.tutorial_items_box);

//        Bitmap sixth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_purchase);
        tutorials.add(R.drawable.tutorial_purchase);

//        Bitmap seventh = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_upgrade);
        tutorials.add(R.drawable.tutorial_upgrade);

//        Bitmap eighth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_description_item);
        tutorials.add(R.drawable.tutorial_description_item);

//        Bitmap ninth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_unlocked);
        tutorials.add(R.drawable.tutorial_unlocked);

//        Bitmap tenth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_locked);
        tutorials.add(R.drawable.tutorial_locked);

//        Bitmap eleventh = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_description_ach);
        tutorials.add(R.drawable.tutorial_description_ach);

//        Bitmap twelfth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_settings);
        tutorials.add(R.drawable.tutorial_settings);

//        Bitmap thirteenth = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_done);
        tutorials.add(R.drawable.tutorial_done);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
