package com.example.budgetbuddy.utility;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

public class PieChartHandler {

    /**
     * Styles <code>PieChart</code> objects
     * @param pieChart The PieChart to style
     * @param colour The colour to use for the legend
     */
    public static void setupPieChart(@NonNull PieChart pieChart, @ColorInt int colour)
    {
        // Set pie chart properties
        pieChart.setUsePercentValues(true);                             // Calculate by category percentage
        pieChart.getDescription().setEnabled(false);                    // Remove the description label
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);                       // Sets a hole in the middle of the cart
        pieChart.setHoleRadius(40f);                                    // Make the hole smaller
        pieChart.setDrawEntryLabels(false);                             // Remove the labels from slices


        // Set the legend of the pie chart
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12f);

        // Set the colour to the dynamic foreground colour
        legend.setTextColor(colour);
        legend.setTypeface(Typeface.MONOSPACE);                         // Use a monospace font so string padding works properly

        // Set the alignment to be to the centre right of the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);       // Stack the legend vertically

        // Set the draw location of the legend
        legend.setDrawInside(false);
        legend.setXEntrySpace(0f);     // X offset
        legend.setYEntrySpace(0f);      // Y offset
    }
}
