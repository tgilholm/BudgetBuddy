package com.example.budgetbuddy.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to handle MPAndroidChart PieCharts
 */
public final class PieChartHandler {

    private PieChartHandler() {}

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

    /**
     * Converts a list of <code>TransactionWithCategory</code> objects into a <code>PieDataSet</code>
     * @param context the application context
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>PieDataSet</code> for use in a <code>PieChart</code>
     */
    @NonNull
    public static PieDataSet getPieData(Context context, List<TransactionWithCategory> transactions)
    {
        // DataSet
        PieDataSet dataSet = new PieDataSet(new ArrayList<>(), "");

        List<Integer> colorList = new ArrayList<>();

        // Get total spending
        double totalSpend = TransactionUtils.getTotalSpend(transactions);

        // Gets the total per category as a map
        Map<Category, Double> categoryTotals = TransactionUtils.getCategoryTotals(transactions);

        // Convert to a list for sorting
        List<Map.Entry<Category, Double>> entryList = new ArrayList<>(categoryTotals.entrySet());
        entryList.sort((lhs, rhs) -> rhs.getValue().compareTo(lhs.getValue())); // Sort the list by value (amount)

        // Hold the other total as the value of the other category
        double otherTotal = 0;

        // Adds everything up to 3 to its own category
        for (int i = 0; i < entryList.size(); i++)
        {
            Map.Entry<Category, Double> entry = entryList.get(i);

            if (i < 3) // if less than the limit
            {
                // Add a new entry to the PieEntries
                String label = StringUtils.formatLabel(entry.getKey().getName(), entry.getValue() / totalSpend * 100);

                // Add a colour to the dataSet
                colorList.add(ColorHandler.getColorARGB(context, entry.getKey().getColorID()));

                // Add the entry to the dataset
                dataSet.addEntry(new PieEntry(entry.getValue().floatValue(), label));
            } else
            {
                otherTotal += entry.getValue();
            }
        }

        // Add everything else to "other"
        if (otherTotal > 0)
        {
            String label = StringUtils.formatLabel("Other",  otherTotal / totalSpend * 100);
            dataSet.addEntry(new PieEntry((float) otherTotal, label));

            colorList.add(ColorHandler.getColorARGB(context, R.color.budgetBlue));
        }

        // Add the colourList to the dataset
        dataSet.setColors(colorList);

        // Return the dataSet
        return dataSet;

    }
}
