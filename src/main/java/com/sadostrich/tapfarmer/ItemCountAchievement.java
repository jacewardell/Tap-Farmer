package com.sadostrich.tapfarmer;

import java.io.Serializable;

/**
 * Created by Dee on 12/7/13.
 */
public class ItemCountAchievement extends Achievement implements Serializable {

    private String title, description, itemName;
    private boolean completed;

    public ItemCountAchievement(String title, String description, String itemName)
    {
        this.itemName = itemName;
        this.completed = false;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean completed() {

        if(completed == true)
            return true;
        else
        {
            for(Item i: Game.getInstance().getItems())
            {
                if(i.getName() == itemName)
                {
                    if(i.getOwned() >= 100)
                    {
                        completed = true;
                        return true;
                    }
                }
            }
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
