package com.example.budgetbuddy.fragments;

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
import com.example.budgetbuddy.adapters.PieChartLegendAdapter;
import com.example.budgetbuddy.entities.PieChartData;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.utility.ColorHandler;
import com.example.budgetbuddy.utility.Converters;
import com.example.budgetbuddy.utility.PieChartHandler;
import com.example.budgetbuddy.utility.TransactionUtils;
import com.example.budgetbuddy.viewmodel.BudgetViewModel;
import com.example.budgetbuddy.viewmodel.OverviewViewModel;
import com.example.budgetbuddy.adapters.RecyclerViewAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.List;

/**
 * The fragment subclass for the Overview section of the app.
 * Connects to fragment_overview.xml to provide layout
 */
public class OverviewFragment extends Fragment
{
    private RecyclerViewAdapter recyclerViewAdapter;
    private View emptyView;     // Instance of empty layout for no-transaction state
    private PieChart pieChart;
    private PieChartLegendAdapter pieLegendAdapter;
    private OverviewViewModel overviewViewModel;
    private TextView txtBudgetRemaining;
    private TextView txtTotalBudget;


    // Links the budget and transaction LiveData
    private final MediatorLiveData<Pair<Double, Double>> budgetTransactionMediator = new MediatorLiveData<>();


    /**
     * Instantiates the <code>View</code>. Gets the layout elements, sets up the <code>ViewModels</code>.
     * Sets up the <code>FloatingActionButton</code> to direct the user to the <code>AddFragment</code>.
     * Observes the transaction list and updates the <code>RecyclerView</code> and <code>PieChart</code>.
     * Observes the <code>MediatorLiveData</code> combining the two to update the remaining budget.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        Log.d("OverviewFragment", "Loaded overview fragment");

        super.onViewCreated(view, savedInstanceState);

        // Get the Views from the layout
        pieChart = view.findViewById(R.id.pieChart);
        TextView txtRecentTransactions = view.findViewById(R.id.tvRecentTransactions);
        txtBudgetRemaining = view.findViewById(R.id.tvBudgetRemaining);
        txtTotalBudget = view.findViewById(R.id.tvTotalBudget);
        RecyclerView rvPartialHistory = view.findViewById(R.id.rvPartialHistory);
        RecyclerView pieChartLegend = view.findViewById(R.id.pieChartLegend);
        FloatingActionButton addButton = view.findViewById(R.id.overviewAddButton);
        emptyView = view.findViewById(R.id.overviewEmptyState);     // Get the empty layout


        // Set up the Overview and Budget ViewModels
        BudgetViewModel budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        overviewViewModel = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);
        PieChartHandler.setupPieChart(pieChart);


        // Instantiate the RecyclerView with an empty list (the observer will update it)
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<>(), R.layout.transaction_item);
        rvPartialHistory.setLayoutManager(new LinearLayoutManager(getContext()));       // Use a vertical LinearLayout as the layout manager
        rvPartialHistory.setAdapter(recyclerViewAdapter);                               // Connect to the recyclerViewAdapter

        // Instantiate the Pie Chart legend
        pieLegendAdapter = new PieChartLegendAdapter(new ArrayList<>(), getContext());
        pieChartLegend.setLayoutManager(new LinearLayoutManager(getContext()));
        pieChartLegend.setAdapter(pieLegendAdapter);


        // Get the data sources for the mediator
        LiveData<List<TransactionWithCategory>> transactionSource = overviewViewModel.getTransactions();
        LiveData<Double> budgetSource = budgetViewModel.getBudget();

        // The Runnable is executed when either the Transaction list or Budget is updated
        Runnable combinedData = () ->
        {
            Double currentBudget = budgetSource.getValue();
            List<TransactionWithCategory> currentTransactions = transactionSource.getValue();

            // Null check
            if (currentBudget != null && currentTransactions != null)
            {
                Log.d("OverviewFragment", "Executing combinedData. Budget: " + currentBudget + " Transaction list size: " + currentTransactions.size());
                double remainingBudget = TransactionUtils.getBudgetRemaining(currentBudget, currentTransactions);

                // Pass the remaining budget to the mediatorLiveData and trigger the observer
                budgetTransactionMediator.setValue(new Pair<>(currentBudget, remainingBudget));
            } else
            {
                Log.e("OverviewFragment", "Failed to recalculate current budget!");
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
            // Check if the transaction list is null/empty
            boolean listEmpty = transactionWithCategories == null || transactionWithCategories.isEmpty();

            // If empty, hide the views
            pieChart.setVisibility(listEmpty ? View.GONE : View.VISIBLE);   // Gone if empty, visible if not
            rvPartialHistory.setVisibility(listEmpty ? View.GONE : View.VISIBLE);
            pieChartLegend.setVisibility(listEmpty ? View.GONE : View.VISIBLE);
            txtRecentTransactions.setVisibility(listEmpty ? View.GONE : View.VISIBLE);
            emptyView.setVisibility(listEmpty ? View.VISIBLE : View.GONE);  // Visible if empty, gone if not


            // If not, update the pieChart and recyclerview
            if (!listEmpty)
            {
                recyclerViewAdapter.updateTransactions(TransactionUtils.sortTransactions(transactionWithCategories));
                updatePieChart(transactionWithCategories);

                // Scroll back to the top of the RecyclerView to show the new transaction
                if (rvPartialHistory.getLayoutManager() != null)
                {
                    rvPartialHistory.getLayoutManager()
                            .scrollToPosition(0);
                }
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

    /**
     * Helper method to calculate the remaining budget and update the UI
     *
     * @param totalBudget     <code>Double</code> value representing total budget
     * @param remainingBudget <code>Double</code> value representing remaining budget
     */
    private void updateRemainingBudget(Double totalBudget, Double remainingBudget)
    {
        // Display the remaining budget
        txtBudgetRemaining.setText(Converters.doubleToCurrencyString(remainingBudget));
        Log.d("OverviewFragment", "Recalculated remaining budget: " + remainingBudget + " of: " + totalBudget);

        // Display the total budget
        String outputString = "Monthly Budget: " + Converters.doubleToCurrencyString(totalBudget);
        txtTotalBudget.setText(outputString);

        // Set the text colour to red if negative, green if positive
        ColorHandler.setAmountColour(txtBudgetRemaining, remainingBudget);
    }


    /**
     * Called to inflate the layout to create the view
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
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }


    /**
     * Updates the PieChart with the new data
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects to update the chart with
     */
    private void updatePieChart(List<TransactionWithCategory> transactions)
    {
        if (transactions == null || transactions.isEmpty())
        {
            Log.e("OverviewFragment", "Failed to update pie chart");
        }

        // Get the PieChartData
        PieChartData pieData = overviewViewModel.getPieData(transactions);
        PieDataSet dataSet = pieData.getDataSet();

        // Style the dataset
        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        dataSet.setSliceSpace(2f);
        dataSet.setDrawValues(false); // Remove the labels on the slices themselves

        // Set the dataSet colours
        dataSet.setColors(ColorHandler.getColorARGBList(getContext(), pieData.getColours()));

        // Update the recyclerView
        pieLegendAdapter.updateLegendItems(pieData.getLegendItems());

        pieChart.setData(new PieData(dataSet));     // Set the slices
        pieChart.invalidate();                      // Redraw
    }
}