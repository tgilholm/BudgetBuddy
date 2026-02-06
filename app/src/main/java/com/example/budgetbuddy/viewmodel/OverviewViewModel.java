package com.example.budgetbuddy.viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetbuddy.entities.PieChartData;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.data.DataRepository;
import com.example.budgetbuddy.utility.PieChartHandler;

import java.util.List;

/**
 * ViewModel for interacting with <code>OverviewFragment</code>.
 * Abstracts business logic from fragment, interfaces with <code>DataRepository</code>
 */
public class OverviewViewModel extends AndroidViewModel
{
    private final DataRepository dataRepository;

    /**
     * Constructs a new <code>OverviewViewModel</code>
     *
     * @param application the application context
     */
    public OverviewViewModel(Application application)
    {
        super(application);

        dataRepository = DataRepository.getInstance(application); // Initialise dataRepository
    }

    /**
     * Return an immutable <code>LiveData</code> list of transactions
     *
     * @return a <code>LiveData</code> list of <code>TransactionWithCategory</code> objects
     */
    public LiveData<List<TransactionWithCategory>> getTransactions()
    {
        return dataRepository.getAllTransactions();
    }

    /**
     * Retrieves the PieChartData from the PieChartHandler. Allows the utility
     * class to be modified without changing access for fragments.
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>PieChartData</code> object for use in a <code>PieChart</code>
     */
    public PieChartData getPieData(List<TransactionWithCategory> transactions)
    {
        return PieChartHandler.getPieData(transactions);
    }

}
