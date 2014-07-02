package com.sadostrich.tapfarmer;

import java.io.Serializable;

/**
 * Created by Jace on 12/7/13.
 *C:\Users\Jace\Documents\GitHub\TapFarmer\TapFarmer\build\source\r\debug\com\sadostrich\tapfarmer\Upgrade.java
 * Contains information of an upgrade for an item
 */
public class Upgrade implements Serializable
{
    private String name;
    private String description;
    private String nameAndAmount;
    private int price;
    //0.05 == 5% boost; 1.0 == 100% boost; xyz.0 == xyz.0% boost
    private double cpsBoost ;

    /** Constructs an upgrade object containing a name, description, price, and cps boost **/
    public Upgrade(String name, String description, int price, double cpsBoost)
    {
        this.name = name;
        this.description = description;
        this.nameAndAmount = name + " (" + price + " coins)";
        this.cpsBoost = cpsBoost;
        this.price = price;
    }

    /** Returns the name of the upgrade **/
    public String getName()
    {
        return name;
    }

    public String getNameAndAmount() { return nameAndAmount; }

    /** Returns a description of the upgrade **/
    public String getDescription()
    {
        return description;
    }

    /** Returns the price of the upgrade **/
    public int getPrice()
    {
        return price;
    }

    /** Returns the cps boost of the upgrade **/
    public double getCpsBoost()
    {
        return cpsBoost;
    }
}
