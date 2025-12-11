package com.example.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;
import com.example.budgetbuddy.repositories.DataRepository;

import java.util.Calendar;
import java.util.List;

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
    public void addTransaction(double amount, TransactionType type, Calendar dateTime, long categoryID, RepeatDuration repeatDuration)
    {

        dataRepository.insertTransaction(new Transaction(
                amount,
                type,
                dateTime,
                categoryID,
                repeatDuration));
    }

    public LiveData<List<Category>> getCategories()
    {
        return dataRepository.getAllCategories();
    }


    // Sends a new category to the Repository
    public void addCategory(String name, int colorID)
    {
        dataRepository.insertCategory(new Category(name, colorID));
    }
}
