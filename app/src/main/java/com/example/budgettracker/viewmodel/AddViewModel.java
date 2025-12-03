package com.example.budgettracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.budgettracker.entities.Transaction;
import com.example.budgettracker.enums.RepeatDuration;
import com.example.budgettracker.enums.TransactionType;
import com.example.budgettracker.repositories.DataRepository;

import java.util.Calendar;

// The ViewModel that interacts with the AddFragment
// Solely responsible for sending new transactions to the data repository
public class AddViewModel extends AndroidViewModel
{
    // Get an instance of the DataRepository
    DataRepository dataRepository;

    public AddViewModel(Application application)
    {
        super(application);
        dataRepository = DataRepository.getInstance(application);
    }

    // Passes a new transaction to the Repository
    public void addTransaction(double amount, TransactionType type, Calendar dateTime, String category, RepeatDuration repeatDuration)
    {
        // todo input validation here instead of in fragment
        dataRepository.insertTransaction(new Transaction(
                amount,
                type,
                dateTime,
                category,
                repeatDuration));
    }
}
