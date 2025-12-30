package com.example.budgetbuddy.entities;

import androidx.annotation.ColorInt;

/**
 * Holds a single row in the RecyclerView of the PieChart's legend.
 * Represents a category's proportion of the total with it's name and colour
 */
public class PieChartLegendItem
{
    private final String name;
    private final String percentage;
    @ColorInt
    private final int color;


    public PieChartLegendItem(String name, String percentage, int color)
    {
        this.name = name;
        this.percentage = percentage;
        this.color = color;
    }


    public String getName()
    {
        return name;
    }

    public String getPercentage()
    {
        return percentage;
    }

    public int getColor()
    {
        return color;
    }
}
