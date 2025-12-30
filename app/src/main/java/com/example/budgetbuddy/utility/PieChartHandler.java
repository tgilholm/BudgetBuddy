package com.example.budgetbuddy.utility;

import android.graphics.Color;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.PieChartData;
import com.example.budgetbuddy.entities.PieChartLegendItem;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to handle MPAndroidChart PieCharts
 */
public final class PieChartHandler
{
    private static final int MAX_CATEGORIES = 3;    // Avoids hard-coding, max categories can be changed later

    // Final class, no instantiation
    private PieChartHandler()
    {
    }

    /**
     * Sets styling for <code>PieChart</code> objects
     *
     * @param pieChart The PieChart to style
     */
    public static void setupPieChart(@NonNull PieChart pieChart)
    {
        // Set pie chart properties
        pieChart.setUsePercentValues(true);                             // Calculate by category percentage
        pieChart.getDescription()
                .setEnabled(false);                    // Remove the description label
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);                       // Sets a hole in the middle of the cart
        pieChart.setHoleRadius(40f);                                    // Make the hole smaller
        pieChart.setDrawEntryLabels(false);                             // Remove the labels from slices

        // Disable the legend (RecyclerView used instead)
        pieChart.getLegend()
                .setEnabled(false);
    }

    /**
     * Overloads <code>getPieData</code>, abstracts topN setting from callers.
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>PieChartData</code> object for use in a <code>PieChart</code>
     */
    @NonNull
    public static PieChartData getPieData(List<TransactionWithCategory> transactions)
    {
        return getPieData(transactions, MAX_CATEGORIES);
    }



    /**
     * Converts a list of <code>TransactionWithCategory</code> objects into a <code>PieDataSet</code>
     * Note - the resulting colorList in the PieChartData object will consist of unresolved Color IDs.
     * Resolve these by calling <code>ColorHandler.getColorARGB</code>. (Avoids context needed here)
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @param topN         the number beyond which categories will be grouped into "other"
     * @return a <code>PieChartData</code> object for use in a <code>PieChart</code>
     *
     *
     */
    @NonNull
    private static PieChartData getPieData(List<TransactionWithCategory> transactions, int topN)
    {
        List<PieEntry> entries = new ArrayList<>();
        List<PieChartLegendItem> legendItems = new ArrayList<>();
        List<Integer> colorList = new ArrayList<>();

        double totalSpend = TransactionUtils.getTotalSpend(transactions);

        // If no transactions, return everything empty
        if (transactions.isEmpty())
        {
            return new PieChartData(new PieDataSet(new ArrayList<>(), ""), colorList, new ArrayList<>());
        }

        // Get the topN pair
        Pair<List<Map.Entry<Category, Double>>, Map.Entry<String, Double>> namedAndOther = TransactionUtils.getTopNCategoryTotals(transactions, topN);

        // Extract named & other
        List<Map.Entry<Category, Double>> namedCategories = namedAndOther.first;
        Map.Entry<String, Double> otherCategory = namedAndOther.second;

        // Add named to dataset
        for (Map.Entry<Category, Double> entry : namedCategories)
        {
            Category category = entry.getKey();                                 // Extract key to category
            Double amount = entry.getValue();                                   // Extract value to double

            // Add to dataset
            entries.add(new PieEntry(amount.floatValue(), category.getName()));
            colorList.add(category.getColorID());   // Needs to be resolved to ARGB later

            // Add to legend
            String percentage = String.format("%.1f%%", amount / totalSpend * 100);
            legendItems.add(new PieChartLegendItem(category.getName(), percentage, category.getColorID()));
        }

        // Add "other" to dataset;
        if (otherCategory.getValue() > 0)
        {
            String percentage = String.format("%.1f%%", (otherCategory.getValue() / totalSpend) * 100);
            entries.add(new PieEntry(otherCategory.getValue().floatValue(), "Other")); // Cast to float
            colorList.add(R.color.budgetBlue);  // Needs to be resolved to ARGB later
            legendItems.add(new PieChartLegendItem("Other", percentage, R.color.budgetBlue));
        }

        // Create the dataset
        PieDataSet dataSet = new PieDataSet(entries, "dataset");
        return new PieChartData(dataSet, colorList, legendItems);

    }

}
