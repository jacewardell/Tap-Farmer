package com.sadostrich.tapfarmer;

import java.io.Serializable;

/**
 * Created by Dee on 12/6/13.
 */
public class TapAchievement extends Achievement implements Serializable
{

    //Vars
    private int tapGoal, points;
    private String title, description;
    private boolean completed;

    public TapAchievement(String title,  String description, int tapGoal, int points)
    {
        this.tapGoal = tapGoal;
        this.points = points;
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    @Override
    public boolean completed()
    {
        if(completed)
            return true;
        else
        {
            if(Game.getInstance().getTotalTaps() >= tapGoal) //check to make sure its not completed now.
            {
                //TODO: Reward the user points
                completed = true;
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public int getPoints()
    {
        return points;
    }

    @Override
    public String getDescription()
    {
        return description;
    }
}
