package com.sadostrich.tapfarmer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dee on 12/22/13.
 */
public class SavedGame implements Serializable
{

    public double total;
    public double runningTotal;
    public double totalSpent;
    public double cps;
    public ArrayList<Item> items;
    public ArrayList<Achievement> unlockedAchievements;
    public ArrayList<Achievement> lockedAchievements;
    public int totalTaps;

    public SavedGame()
    {
        total = Game.getInstance().getTotal();
        runningTotal = Game.getInstance().getRunningTotal();
        totalSpent = Game.getInstance().getTotalSpent();
        cps = Game.getInstance().getCps();
        items = Game.getInstance().getItems();
        totalTaps = Game.getInstance().getTotalTaps();
        unlockedAchievements = Game.getInstance().getUnlockedAchievements();
        lockedAchievements = Game.getInstance().getLockedAchievements();
        totalTaps = Game.getInstance().getTotalTaps();
    }
}
