package com.example.budgetbuddy.utility;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Utility class to handle string formatting
 */
public final class StringUtils
{
    // Final class, no instantiation
    private StringUtils() {}

    // Returns a string padded with spaces to a specified length

    /**
     * Pads a string with a specified amount of spaces
     * @param s the <code>String</code> to pad
     * @param l an <code>int</code> for the amount of spaces
     * @return a formatted <code>String</code>
     */
    @NonNull
    private static String padRight(@NonNull String s, int l)
    {
        if (s.length() >= l)
        {
            return s;
        } else
        {
            return String.format(Locale.getDefault(), "%-" + l + "s", s);
        }
    }

    /**
     * Formats a string for use in a PieChart label
     * @param s the <code>String</code> to pad
     * @param n an <code>int</code> for the amount of spaces
     * @return a formatted <code>String</code>
     */
    @NonNull
    public static String formatLabel(String s, double n)
    {
        return String.format(Locale.getDefault(), "%s %.1f%%",
                padRight(s, 16), n);
    }
}
