package com.example.budgettracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.budgettracker.R;
import com.example.budgettracker.TransactionViewModel;
import com.example.budgettracker.adapters.RecyclerViewAdapter;

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
    //private RecyclerViewAdapter

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

    /*TODO: Recent Transactions
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
        recyclerViewAdapter = new RecyclerViewAdapter();


        // Set up the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);


        // Connect the TransactionViewModel to the same one in MainActivity
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        final String[] lastTransaction = new String[1];

        // Set up an observer on the TransactionViewModel
        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), transactionList ->
        {

            // Send the new list to the recyclerViewAdapter
            recyclerViewAdapter.submitList(transactionList);
            Log.v("OverviewFragment", String.valueOf(transactionList.size()));
            Log.v("OverviewFragment", String.valueOf(recyclerViewAdapter.getItemCount()));

            // TODO USE LESS TERRIBLE METHOD TO UPDATE RECYCLERVIEW
            recyclerViewAdapter.notifyDataSetChanged();

            if (!transactionList.isEmpty())
            {
                lastTransaction[0] = transactionList.get(transactionList.size() - 1).toString();

            }

            Toast.makeText(getContext(), "Transaction list updated: " + lastTransaction[0], Toast.LENGTH_LONG).show();
        });
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


}