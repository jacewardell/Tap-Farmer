package com.sadostrich.tapfarmer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dee on 12/2/13.
 */
public class Item implements Serializable
{
    private int basePrice;
    private double baseCps;
    private int owned;
    private String name;
    private String description;
    private int resourcePath;
    private boolean hasUpgrade;
    private Queue<Upgrade> upgrades;

    public Item(String name, String description, int basePrice, double baseCps, int resourcePath, Queue<Upgrade> upgrades)
    {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.baseCps = baseCps;
        this.resourcePath = resourcePath;
        this.hasUpgrade = false;
        this.upgrades = upgrades;
        this.owned = 0;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription(){ return description; }

    public int getPrice()
    {
        //Algorithm
        return (int)Math.round(basePrice * Math.pow(1.15, owned));
    }

    public double getCPS()
    {
        return baseCps;
    }

    public double getCpsBonus()
    {
        //algorithm

        return baseCps;
    }

    /** Purchase the item. returns the new amount owned.**/
    public int purchase()
    {
        Game.getInstance().purchase(getPrice(), getCpsBonus());

        return ++owned;
    }

    /** Gets the number of this specific item owned **/
    public int getOwned()
    {
        return owned;
    }

    /** Gets the resource path linked to the item's image **/
    public int getResourcePath() {
        return resourcePath;
    }

    /** Gets whether the item has a current upgrade or not **/
    public boolean getHasUpgrade() {return hasUpgrade; }

    /** Sets if the item has an upgrade or not **/
    public boolean setHasUpgrade(boolean upgrade)
    {
        hasUpgrade = upgrade;
        return hasUpgrade;
    }

    public Queue<Upgrade> getUpgrades() { return upgrades; }

    /** Upgrades the item giving the player a cps boost **/
    public void upgrade(int item)
    {
        Upgrade nextUpgrade = upgrades.remove();
        double newCps = baseCps * nextUpgrade.getCpsBoost();
        baseCps += newCps;
        Game.getInstance().purchase(nextUpgrade.getPrice(), owned * newCps);
    }
}
