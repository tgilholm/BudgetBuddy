package com.example.budgetbuddy.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.utility.ColorHandler;
import com.example.budgetbuddy.utility.Converters;
import com.example.budgetbuddy.viewmodel.BudgetViewModel;
import com.example.budgetbuddy.viewmodel.OverviewViewModel;
import com.example.budgetbuddy.adapters.RecyclerViewAdapter;
import com.example.budgetbuddy.utility.InputValidator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.List;

/**
 * The fragment subclass for the Overview section of the app
 * Connects to fragment_overview.xml to provide layout
 */

public class OverviewFragment extends Fragment
{
    private RecyclerViewAdapter recyclerViewAdapter;
    private OverviewViewModel overviewViewModel;

    private PieChart pieChart;

    private TextView txtBudgetRemaining;
    private TextView txtTotalBudget;


    // A MediatorLiveData is used to link together the LiveData from the budget and transaction list
    private final MediatorLiveData<Pair<Double, Double>> budgetTransactionMediator = new MediatorLiveData<>();


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Get the Views from the layout
        pieChart = view.findViewById(R.id.pieChart);                                // Get the pie chart from the layout
        txtBudgetRemaining = view.findViewById(R.id.tvBudgetRemaining);            // Get the budget remaining text from the layout
        txtTotalBudget = view.findViewById(R.id.tvTotalBudget);                    // Get the total budget text from the layout
        RecyclerView rvPartialHistory = view.findViewById(R.id.rvPartialHistory);   // Get the recycler view from the layout
        FloatingActionButton addButton = view.findViewById(R.id.overviewAddButton); // Get the FloatingActionButton from the layout

        setupPieChart();    // Set the PieChart styling options


        // Set up the Transaction and Budget ViewModels
        BudgetViewModel budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        overviewViewModel = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);


        // Instantiate the RecyclerView with an empty list (the observer will update it)
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(), R.layout.transaction_item);
        rvPartialHistory.setLayoutManager(new LinearLayoutManager(getContext()));       // Use a vertical LinearLayout as the layout manager
        rvPartialHistory.setAdapter(recyclerViewAdapter);                               // Connect to the recyclerViewAdapter


        LiveData<List<TransactionWithCategory>> transactionSource = overviewViewModel.getTransactions();
        LiveData<Double> budgetSource = budgetViewModel.getBudget();

        // The Runnable is executed when either the Transaction list or Budget is updated
        Runnable combinedData = () ->
        {
            Log.v("OverviewFragment", "runnable combinedData called");
            Double currentBudget = budgetSource.getValue();
            List<TransactionWithCategory> currentTransactions = transactionSource.getValue();

            // Null check
            if (currentBudget != null && currentTransactions != null)
            {
                double remainingBudget = overviewViewModel.getBudgetRemaining(currentBudget, currentTransactions);

                // Pass the remaining budget to the mediatorLiveData and trigger the observer
                budgetTransactionMediator.setValue(new Pair<>(currentBudget, remainingBudget));
            }

        };

        // Add the budgetSource and transactionSource to the budgetTransactionMediator
        budgetTransactionMediator.addSource(transactionSource, transactions -> combinedData.run());
        budgetTransactionMediator.addSource(budgetSource, budget -> combinedData.run());

        // When the budgetTransactionMediator observer fires, update the remaining budget in the UI
        budgetTransactionMediator.observe(getViewLifecycleOwner(), budgetResult ->
        {
            if (budgetResult != null)
            {
                updateRemainingBudget(budgetResult.first, budgetResult.second);
            }
        });

        // Transaction list observer- Updates RecyclerView and PieChart only
        transactionSource.observe(getViewLifecycleOwner(), transactionWithCategories ->
        {

            // Update the RecyclerView
            recyclerViewAdapter.updateTransactions(InputValidator.sortTransactions(transactionWithCategories));

            // Update the PieChart
            updatePieChart(transactionWithCategories);

            // Scroll back to the top of the RecyclerView to show the new transaction
            if (rvPartialHistory.getLayoutManager() != null)
            {
                rvPartialHistory.getLayoutManager().scrollToPosition(0);
            }
        });


        // Set up the FloatingActionButton to direct the user to the Add Fragment
        addButton.setOnClickListener(v ->
        {
            Bundle bundle = new Bundle();
            bundle.putInt("addPage", 1);    // Send 1 to change the page title to 'Add'

            // Use FragmentResult to send a message to the MainActivity
            getParentFragmentManager().setFragmentResult("addPage", bundle);

        });
    }

    // Helper method to calculate the remaining budget
    // The budget needs to be recalculated if either the budget changes or a new transaction is added
    // This method is invoked whenever the MediatorLiveData updates
    private void updateRemainingBudget(Double totalBudget, Double remainingBudget)
    {
        Log.v("OverviewFragment", "updateRemainingBudget called");

        // Display the remaining budget
        txtBudgetRemaining.setText(Converters.doubleToCurrencyString(remainingBudget));

        // Display the total budget
        String outputString = "Monthly Budget: " + Converters.doubleToCurrencyString(totalBudget);
        txtTotalBudget.setText(outputString);

        // Set the text colour to red if negative, green if positive
        ColorHandler.setAmountColour(txtBudgetRemaining, remainingBudget);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }


    // Sets up the styling of the pie chart
    private void setupPieChart()
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
        legend.setTextColor(ColorHandler.getThemeColor(requireContext(), com.google.android.material.R.attr.colorOnSurfaceVariant));
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


    // Add new data to the pie chart
    // Displays only the top three categories by name, then aggregates the rest as "other"
    // Also displays a legend of categories and the percentage they take up
    // Pads the label with spaces between the category name and percentage for readability

    private void updatePieChart(List<TransactionWithCategory> transactions)
    {
        if (transactions == null || transactions.isEmpty())
        {
            return;
        }

        // Get the PieEntries from the ViewModel
        PieDataSet dataSet = overviewViewModel.getPieData(transactions);


        // Style the dataset
        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        dataSet.setSliceSpace(2f);
        dataSet.setDrawValues(false); // Remove the labels on the slices themselves

        pieChart.setData(new PieData(dataSet));
        pieChart.invalidate();
    }


}