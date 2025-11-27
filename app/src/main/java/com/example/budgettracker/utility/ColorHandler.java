package com.example.budgettracker.utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.budgettracker.R;

// Handles methods related to color
public final class ColorHandler
{
    public static ColorStateList resolveColorID(Context context, int colorID)
    {
        return ColorStateList.valueOf(ContextCompat.getColor(context, colorID));
    }

    // Set the text colour of a textview to red if the amount is negative and green otherwise
    public static void setAmountColour(TextView textView, double amount)
    {
        Context context = textView.getContext();
        int color = (amount < 0) ? R.color.brightRed : R.color.brightGreen;
        textView.setTextColor(resolveColorID(context, color));
    }
}
