package com.sadostrich.tapfarmer;

import java.io.Serializable;

/**
 * Created by Dee on 12/20/13.
 */
public class CoinAchievement extends Achievement implements Serializable {

    private String title, description;
    double amount;
    private boolean completed;

    public CoinAchievement(String title, String description, double amount)
    {
        this.amount = amount;
        this.completed = false;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean completed() {

        if(completed == true)
            return true;
        else if(Game.getInstance().getRunningTotal() >= amount)
        {
            completed = true;
            return true;
        }

        return false;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
