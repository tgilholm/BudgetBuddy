package com.example.budgetbuddy.utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.example.budgetbuddy.R;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for handling color methods
 */
public final class ColorHandler
{
    // Final class- no instantiation
    private ColorHandler()
    {
    }


    /**
     * Converts a color from colors.xml to a usable <code>ColorStateList</code>
     *
     * @param context the application context
     * @param colorID an id of a color in colors.xml
     * @return a <code>ColorStateList</code> object
     */
    @NonNull
    public static ColorStateList resolveColorID(Context context, int colorID)
    {
        return ColorStateList.valueOf(getColorARGB(context, colorID));
    }

    /**
     * Returns the ARGB of a color in colors.xml
     *
     * @param context the application context
     * @param colorID an id of a color in colors.xml
     * @return an <code>int</code> representing the ARGB of the colour
     */
    public static int getColorARGB(Context context, int colorID)
    {
        return ContextCompat.getColor(context, colorID);
    }

    /**
     * Resolves the ARGB of a list of colours in colors.xml
     * @param context the application context
     * @param colorIDs a list of color ids
     * @return a list of resolved color ints
     */
    public static List<Integer> getColorARGBList(Context context, @NonNull List<Integer> colorIDs)
    {
        return colorIDs.stream()
                .map(colorID -> getColorARGB(context, colorID))          // For each item, get the ARGB
                .collect(Collectors.toList());                                  // Then collect them all into the list and return
    }


    /**
     * Sets the text colour of a <code>TextView</code> to red if <code>amount</code>
     * is negative, and green otherwise
     *
     * @param textView the <code>TextView</code> to edit
     * @param amount   a <code>double</code> quantity
     */
    public static void setAmountColour(@NonNull TextView textView, double amount)
    {
        Context context = textView.getContext();
        int color = (amount < 0) ? R.color.brightRed : R.color.brightGreen;
        textView.setTextColor(resolveColorID(context, color));
    }


    /**
     * Gets the luminance of <code>backgroundColour</code> and returns black or white text.
     * Prevents hard-to-read text or icons.
     *
     * @param context          the application context
     * @param backgroundColour a <code>ColorInt</code> colour
     * @return a color <code>ARGB</code> of white if luminance is low, black otherwise
     */
    @ColorInt
    public static int resolveForegroundColor(@NonNull Context context, @ColorInt int backgroundColour)
    {
        // Calculate the luminance of the background colour
        double luminance = ColorUtils.calculateLuminance(backgroundColour);

        // If luminance is greater than 0.5, return dark text
        if (luminance > 0.5)
        {
            return getColorARGB(context, R.color.text_fixed_dark);
        } else
        {
            return Color.WHITE;
        }

    }

    /**
     * Sets the colour of a <code>Drawable</code> object with a <code>@ColorInt int</code>
     *
     * @param drawable the <code>Drawable</code> object to modify
     * @param colour   the <code>int</code> ID of the colour
     * @return the modified <code>Drawable</code>
     */
    public static Drawable createBackground(Drawable drawable, @ColorInt int colour)
    {
        if (drawable != null)
        {
            drawable = drawable.mutate();      // Make the drawable mutable to differentiate it

            // Set colour with PorterDuffColorFilter
            drawable.setColorFilter(new PorterDuffColorFilter(colour, PorterDuff.Mode.SRC_IN));
        }
        return drawable;
    }
}
