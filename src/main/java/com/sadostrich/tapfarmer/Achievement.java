package com.sadostrich.tapfarmer;

import java.io.Serializable;

/**
 * Created by Dee on 12/6/13.
 */
public abstract class Achievement implements Serializable
{

    /** This should return true if the criteria has been met **/
    public boolean completed()
    {
        return true;
    }

    /** Returns the title **/
    public String getTitle()
    {
        return null;
    }

    /** Returns the amount of points this achievement is worth**/
    public int getPoints()
    {
        return 0;
    }

    /** Returns the description **/
    public String getDescription()
    {
        return "asd";
    }

}
