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
            Log.v("BudgetViewModel", "SharedPreferences updated");
            Log.v("BudgetViewModel", "Key = " + key);
            // Update the budget when a change is made
            if (key != null && key.equals("budget"))
            {
                Log.v("BudgetViewModel", prefs.getString(key, "0"));
                getBudgetFromPrefs();
            }
        };

        // Register the ViewModel to listen to changes made to the SharedPreferences
        prefs.registerOnSharedPreferenceChangeListener(listener);

        getBudgetFromPrefs();   // Update the budget value
    }

    // Retrieve the "budget" field from the SharedPreferences
    private void getBudgetFromPrefs()
    {
        // preferences.xml always stores the budget as a string
        Log.v("BudgetViewModel", "getBudgetFromPrefs fired, read budget as " + prefs.getFloat("budget", 0));
        budget.postValue((double) prefs.getFloat("budget", 0)); // default to 0
    }

    // Return the value of the budget
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
}
