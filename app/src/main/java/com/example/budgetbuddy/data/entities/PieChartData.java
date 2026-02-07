package com.example.budgetbuddy.data.entities;

import com.github.mikephil.charting.data.PieDataSet;

import java.util.List;

/**
 * Data Transfer Object containing a <code>PieDataSet</code>, a list of a <code>PieChartLegendItem</code> objects
 * and a list of Color Integers.
 */
public class PieChartData {
    private final PieDataSet dataSet;
    private final List<Integer> colours;
    private final List<PieChartLegendItem> legendItems;


    public PieChartData(PieDataSet dataSet, List<Integer> colours, List<PieChartLegendItem> legendItems) {
        this.dataSet = dataSet;
        this.colours = colours;
        this.legendItems = legendItems;
    }

    public List<Integer> getColours()
    {
        return colours;
    }

    public PieDataSet getDataSet() {
        return dataSet;
    }

    public List<PieChartLegendItem> getLegendItems() {
        return legendItems;
    }
}
