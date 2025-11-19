package com.example.budgettracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgettracker.R;
import com.example.budgettracker.Transaction;
import com.example.budgettracker.TransactionViewModel;
import com.example.budgettracker.adapters.RecyclerViewAdapter;
import com.example.budgettracker.utility.InputValidator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The fragment subclass for the Overview section of the app
 * Connects to fragment_overview.xml to provide layout
 */

public class OverviewFragment extends Fragment
{

    // Create an instance of the TransactionViewModel
    private TransactionViewModel transactionViewModel;

    // Create an instance of the RecyclerViewAdapter
    private RecyclerViewAdapter recyclerViewAdapter;

    private PieChart pieChart;

    /* TODO: Update remaining budget
    Update txtBudgetAmount when new transactions are added.
    Subtract the total outgoings from the total budget to get the remaining budget
     */

    /* TODO: Pie-chart functionality
    Pie charts will have colour-coded slices representing constituent categories
    They will automatically calculate how much of the spending is taken up by which category
    The user has the option to change the time span the average is taken from (default week)
    Categories are then shown to the right of the pie chart, ordered by percentage
     */

    /*TODO: Recent Transactions DONE
    Get the most recent (last week or so) transactions from the file and display them in a
    list view, sorted by date.
     */


    public OverviewFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        // Connect the TransactionViewModel to the same one in MainActivity
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // Get the current Transaction list and convert it to a standard List
        List<Transaction> transactions = transactionViewModel.getTransactions().getValue();

        // Set up the recyclerViewAdapter with the current (sorted) transaction list
        if (transactions != null)
        {
            recyclerViewAdapter = new RecyclerViewAdapter(InputValidator.sortTransactions(transactions));
        }

        // Set up the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Set up an observer on the TransactionViewModel
        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), transactionList ->
        {
            // When the transactionViewModel observes an update on transaction list, update the RecyclerView

            // Send the new (sorted) list to the recyclerViewAdapter
            recyclerViewAdapter.updateTransactions(InputValidator.sortTransactions(transactionList));
            Log.v("OverviewFragment", String.valueOf(transactionList.size()));
            Log.v("OverviewFragment", String.valueOf(recyclerViewAdapter.getItemCount()));
        });

        // Update the pie chart

        pieChart = view.findViewById(R.id.pieChart);    // Get the pie chart from the layout
        setupPieChart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    // Set up the pie chart
    // Gets each of the categories and the total spending (currently for all time)
    // Gets the number of items in each category

    // Calculates the proportion of each element
    private void setupPieChart() {
        // Get the list of transactions
        List<Transaction> transactions = transactionViewModel.getTransactions().getValue();

        // List of pie entries
        List<PieEntry> pieEntries = new ArrayList<>();
        // Convert each of the transactions into a PieEntry
        for (Transaction t : transactions)
        {
            // Add the category as the label and amount as the data
            pieEntries.add(new PieEntry((float) t.getAmount(), t.getCategory()));
        }

        // Add the pie entries to the chart
        PieDataSet dataSet = new PieDataSet(pieEntries, "Spending");
        pieChart.setData(new PieData(dataSet));
    }
}