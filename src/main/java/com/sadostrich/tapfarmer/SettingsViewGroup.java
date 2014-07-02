package com.sadostrich.tapfarmer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jace on 11/29/13.
 */
public class SettingsViewGroup extends ViewGroup
{
    public SettingsViewGroup(Context context)
    {
        super(context);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4)
    {
//        float dpi = getResources().getDisplayMetrics().densityDpi;
//        float width = getWidth();
//        float height = getHeight();
//
//        View soundText = getChildAt(0);
//
//        soundText.layout((int)(width - (dpi * 0.50f) * 0.5f), (int)(height - (dpi * 0.50f) * 0.5f),
//                (int)(width + (dpi * 0.50f) * 0.5f), (int)(height + (dpi * 0.50f) * 0.5f));
    }
}
