package com.example.budgetbuddy.utility;

import android.content.Context;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.data.entities.Category;
import com.google.android.material.chip.Chip;

/**
 * Utility class, handles the creation of new Material chips.
 */
public final class ChipHandler
{
    // Final class- no instantiation
    private ChipHandler () {}


    /**
     * Generates a new <code>Chip</code> from <code>Category</code> data
     * @param context the application context
     * @param category the <code>Category</code> object to take data from
     * @return a <code>Chip</code>
     */
    @NonNull
    public static Chip createChip(@NonNull Context context, @NonNull Category category)
    {
        Chip chip = new Chip(context, null, R.style.Widget_BudgetTracker_ChipStyle);

        // Set the name of the chip to the category name
        chip.setText(category.getName());
        chip.setCheckable(true);
        chip.setClickable(true);

        // Set the background color of the chip top the category's colour
        chip.setChipBackgroundColor(ColorHandler.resolveColorID(context, category.getColorID()));

        // Set the chip text colour to adapt to the chip background colour
        chip.setTextColor(ColorHandler.resolveForegroundColor(context, ColorHandler.getColorARGB(context, category.getColorID())));

        // Set the "tag" parameter of the Chip to the category ID
        // This facilitates the category selection logic
        chip.setTag(category.getCategoryID());
        return chip;
    }


    /**
     * Creates the "add chip" for creating new categories
     * @param context the application context
     * @return a <code>Chip</code>
     */
    @NonNull
    public static Chip createAddCategoryChip(@NonNull Context context)
    {
        ContextThemeWrapper newContext = new ContextThemeWrapper(context, R.style.ThemeOverlay_BudgetTracker_AddChip);
        Chip chip = new Chip(newContext);
        chip.setChipIconResource(R.drawable.add_icon);
        chip.setText("");
        chip.setClickable(true);

        chip.setChipStartPadding(20);
        chip.setChipEndPadding(20);

        return chip;
    }
}
