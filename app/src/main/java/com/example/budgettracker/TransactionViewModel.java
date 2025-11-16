package com.example.budgettracker;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/*
 The TransactionViewModel for the application. This uses the Lifecycle-safe ViewModel to hold the
 data shared across fragments, such as the TransactionList. It uses the LiveData list to automatically
 trigger observers waiting for changes to the list.
 */
public class TransactionViewModel extends ViewModel
{
    /*
    Hold the transactions in a MutableLiveData list. This allows the list to be exposed publicly
    To the application, but not to be modified unless through the addTransaction or removeTransaction methods
    */
    private final MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>(new ArrayList<>());

    // Exposes the LiveData version of the transaction list to the fragments
    public LiveData<List<Transaction>> getTransactions()
    {
        return transactions;    // Returns the immutable list
    }

    // Adds a transaction to the list. Publicly accessible to all fragments
    public void addTransaction(Transaction newTransaction)
    {
        List<Transaction> list = transactions.getValue();
        if (list != null)
        {
            list.add(newTransaction);
            transactions.setValue(list);    // Trigger all fragment observers
        }

    }


}
