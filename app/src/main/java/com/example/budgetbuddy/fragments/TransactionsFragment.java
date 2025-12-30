package com.example.budgetbuddy.fragments;

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
import android.widget.TextView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.adapters.EditRecyclerViewAdapter;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.utility.TransactionUtils;
import com.example.budgetbuddy.viewmodel.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The fragment subclass for the Transactions section of the app.
 * Connects to fragment_transactions.xml to provide layout.
 * Displays a <code>RecyclerView</code> with deletable transaction history
 */
public class TransactionsFragment extends Fragment
{
    /**
     * Inflates thet layout for the fragment
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    /**
     * Connects to the TransactionViewModel, connects transaction history to the RecyclerView.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        Log.d("TransactionsFragment", "Loaded TransactionsFragment");
        super.onViewCreated(view, savedInstanceState);

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        View emptyView = view.findViewById(R.id.transactionEmptyState);     // Instance of empty layout for no-transaction state


        // Connect the TransactionViewModel
        TransactionViewModel transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // Empty List to instantiate the RecyclerView
        List<TransactionWithCategory> emptyList = new ArrayList<>();

        // Set up the editRecyclerViewAdapter
        EditRecyclerViewAdapter editRecyclerViewAdapter = new EditRecyclerViewAdapter(emptyList, transactionViewModel::deleteTransaction, R.layout.editable_transaction_item);

        // Get the recycler view from the layout and set the adapter
        RecyclerView rvFullHistory = view.findViewById(R.id.rvFullHistory);
        rvFullHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFullHistory.setAdapter(editRecyclerViewAdapter);

        // Set an observer on the transaction list to update the Recycler View
        transactionViewModel.getTransactions()
                .observe(getViewLifecycleOwner(), transactionList ->
                {
                    // Check if the transaction list is null/empty
                    boolean listEmpty = transactionList == null || transactionList.isEmpty();

                    // Hide the views and show the empty screen if so
                    rvFullHistory.setVisibility(listEmpty ? View.GONE : View.VISIBLE);
                    txtTitle.setVisibility(listEmpty ? View.GONE : View.VISIBLE);
                    emptyView.setVisibility(listEmpty ? View.VISIBLE : View.GONE);

                    if (!listEmpty)
                    {
                        // Add the list to the recyclerView on update
                        editRecyclerViewAdapter.updateTransactions(TransactionUtils.sortTransactions(transactionList));

                        // Scroll back to the top of the RecyclerView to show the new transaction
                        if (rvFullHistory.getLayoutManager() != null)
                        {
                            rvFullHistory.getLayoutManager()
                                    .scrollToPosition(0);
                        }
                    }
                    Log.d("TransactionsFragment", "Updating Transaction List");
                });
    }
}