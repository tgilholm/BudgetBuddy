package com.example.budgetbuddy.viewmodel;

// Similar format to OverviewViewModel
// The purpose is to centralise all "budget" interactions here
// The ViewModel is responsible for interfacing with SharedPreferences

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.budgetbuddy.enums.ValidationState;
import com.example.budgetbuddy.utility.InputValidator;


// Implements OnSharedPreferenceChangeListener to listen to changes made to the budget from the settings menu
// This means that the changes made will be propagated throughout the application correctly
public class BudgetViewModel extends AndroidViewModel
{

    private final MutableLiveData<Double> budget = new MutableLiveData<>();

    private final SharedPreferences prefs;

    // The sharedPreferenceChangeListener is stored as a member variable to prevent Garbage Collection
    private final SharedPreferences.OnSharedPreferenceChangeListener listener;


    public BudgetViewModel(@NonNull Application application)
    {
        super(application);
        prefs = PreferenceManager.getDefaultSharedPreferences(application);

        listener = (sharedPreferences, key) ->
        {
            // Update the budget when a change is made
            if (key != null && key.equals("budget"))
            {
                getBudgetFromPrefs();
            }
        };

        // Register the ViewModel to listen to changes made to the SharedPreferences
        prefs.registerOnSharedPreferenceChangeListener(listener);

        getBudgetFromPrefs();   // Update the budget value
    }

    /**
     * Retrieve the user-defined budget from shared preferences, cast to double
     * and update the internal budget of this <code>BudgetViewModel</code> instance
     */
    private void getBudgetFromPrefs()
    {
        // Get the budget as a string from prefs
        String stringBudget = prefs.getString("budget", "0");

        // Attempt to parse to a double (should be fine but double check)
        if (InputValidator.validateCurrencyInput(stringBudget))
        {
            budget.postValue(Double.parseDouble(stringBudget));
        } else
        {
            Log.e("BudgetViewModel", "Failed to load string budget from prefs");
        }
    }


    public MutableLiveData<Double> getBudget()
    {
        return budget;
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        // Unregister the Preference listener
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public ValidationState validateBudget(Object budget)
    {
        String validatedInput = null;

        // Cast to string
        try
        {
            validatedInput = ((String) budget).trim();
        } catch (Exception e)
        {
            return ValidationState.INVALID_AMOUNT;
        }

        // Check if empty
        if (validatedInput.isEmpty())
        {
            return ValidationState.EMPTY;
        }

        // Validate budget amount
        if (InputValidator.validateCurrencyInput(validatedInput))
        {
            return ValidationState.NONE;
        } else
        {
            return ValidationState.INVALID_AMOUNT;
        }
    }
}
