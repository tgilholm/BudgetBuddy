package com.example.budgettracker.utility;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

// Handles methods related to color
public final class ColorHandler
{
    public static ColorStateList resolveColorID(Context context, int colorID)
    {
        return ColorStateList.valueOf(ContextCompat.getColor(context, colorID));
    }
}
