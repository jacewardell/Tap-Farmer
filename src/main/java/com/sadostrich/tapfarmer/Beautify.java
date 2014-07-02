package com.sadostrich.tapfarmer;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Dee on 12/11/13.
 */
public class Beautify
{
    /** Comma separates an int **/
    public static String CommaSeparate(int original)
    {
        String result =  NumberFormat.getNumberInstance(Locale.US).format(original);
        return result;
    }

    /** Comma separates an int **/
    public static String CommaSeparateDouble(double original)
    {
        String result =  NumberFormat.getNumberInstance(Locale.US).format(original);
        return result;
    }
}
