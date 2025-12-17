package com.example.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;
import com.example.budgetbuddy.enums.ValidationState;
import com.example.budgetbuddy.repositories.DataRepository;
import com.example.budgetbuddy.utility.InputValidator;

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
    public ValidationState addTransaction(String stringAmount, TransactionType type, String date, String time, long categoryID, RepeatDuration repeatDuration)
    {
        // Validate the amount field
        double amount;
        if (!InputValidator.validateCurrencyInput(stringAmount))
        {
            return ValidationState.INVALID_AMOUNT;
        }
        else {
            try {
                // Parse the string amount into a double
                amount = Double.parseDouble(stringAmount);
            } catch (NumberFormatException e)
            {
                return ValidationState.INVALID_AMOUNT;
            }
        }

        // Parse the date and time into a Calendar
        Calendar calendar = InputValidator.parseDateTime(date, time);
        if (calendar == null)
        {
            return ValidationState.INVALID_DATE;
        }

        // Validate the category
        if (categoryID < 0)
        {
            return ValidationState.NO_CATEGORY;
        }

        // Pass the transaction data to the Repository
        dataRepository.insertTransaction(new Transaction(amount, type, calendar, categoryID, repeatDuration));
        return ValidationState.NONE;
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
