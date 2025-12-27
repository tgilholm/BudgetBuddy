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


/**
 * ViewModel for interacting with <code>AddFragment</code>.
 * Abstracts business logic from fragment, interfaces with <code>DataRepository</code>
 */
public class AddViewModel extends AndroidViewModel
{
    DataRepository dataRepository;

    /**
     * Constructs a new <code>AddViewModel</code>
     *
     * @param application the application context
     */
    public AddViewModel(Application application)
    {
        super(application);
        dataRepository = DataRepository.getInstance(application);   // Initialise dataRepository
    }

    /**
     * Validates transaction data, passes new <code>Transaction</code> to the <code>DataRepository</code>
     *
     * @param stringAmount   a <code>String</code> of the amount
     * @param type           a <code>TransactionType</code> value
     * @param date           a <code>String</code> representing the transaction date
     * @param time           a <code>String</code> representing the transaction time
     * @param categoryID     a <code>long</code> representing the primary key of a <code>Category</code>
     * @param repeatDuration a <code>RepeatDuration</code> value
     * @return <code>ValidationState.NONE</code> if succeeded, <code>INVALID_AMOUNT</code>, <code>INVALID_CATEGORY</code>, <code>INVALID_DATE</code> otherwise
     */
    public ValidationState addTransaction(String stringAmount, TransactionType type, String date, String time, long categoryID, RepeatDuration repeatDuration)
    {
        // Validate the amount field
        double amount;
        if (!InputValidator.validateCurrencyInput(stringAmount))
        {
            return ValidationState.INVALID_AMOUNT;
        } else
        {
            try
            {
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

    /**
     * Exposes a <code>LiveData</code> version of the category list
     *
     * @return a <code>LiveData</code> list of <code>Category</code> objects
     */
    public LiveData<List<Category>> getCategories()
    {
        return dataRepository.getAllCategories();
    }


    /**
     * Creates a new <code>Category</code> & sends it to the <code>DataRepository</code>
     *
     * @param name    the <code>String</code> category name
     * @param colorID the <code>int</code> id of the color in colors.xml
     */
    public void addCategory(String name, int colorID)
    {
        dataRepository.insertCategory(new Category(name, colorID));
    }
}
