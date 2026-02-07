package com.example.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetbuddy.data.entities.TransactionWithCategory;
import com.example.budgetbuddy.data.DataRepository;

import java.util.List;

/**
 * ViewModel for interacting with the <code>TransactionFragment</code>
 * Interfaces with the <code>DataRepository</code>
 */
public class TransactionViewModel extends AndroidViewModel
{
    private final DataRepository dataRepository;

    /**
     * Constructs a new <code>TransactionViewModel</code>
     *
     * @param application the application context
     */
    public TransactionViewModel(@NonNull Application application)
    {
        super(application);

        dataRepository = DataRepository.getInstance(application);
    }


    /**
     * Exposes an immutable list of transactions
     *
     * @return a list of <code>TransactionWithCategory</code> objects
     */
    public LiveData<List<TransactionWithCategory>> getTransactions()
    {
        return dataRepository.getAllTransactions();
    }


    /**
     * Deletes a transaction from the <code>DataRepository</code>
     *
     * @param transactionWithCategory a <code>TransactionWithCategory</code> object
     */
    public void deleteTransaction(@NonNull TransactionWithCategory transactionWithCategory)
    {
        dataRepository.deleteTransaction(transactionWithCategory.transaction);
    }
}
