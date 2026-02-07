package com.example.budgetbuddy.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.data.entities.Category;
import com.example.budgetbuddy.enums.ValidationState;
import com.example.budgetbuddy.utility.InputValidator;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for interacting with the <code>FirstTimeStartupActivity</code>
 * Interfaces with the <code>DataRepository</code> and <code>SharedPreferences</code>
 */
@HiltViewModel
public class StartupViewModel extends ViewModel
{
    private final DataRepository dataRepository;

    private final SharedPreferences prefs;

    /**
     * Constructs a new <code>StartupViewModel</code>
     *
     * @param application the application context
     */
    public StartupViewModel(Application application)
    {
        super(application);

        // Get an instance of the DataRepository
        dataRepository = DataRepository.getInstance(application);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(application);
    }


    /**
     * Creates the default <code>Category</code> objects and adds them to the RoomDB
     */
    public void addDefaultCategories()
    {

        // Use observeForever to get the list of categories
        dataRepository.getAllCategories().observeForever(new Observer<>()
        {
            @Override
            public void onChanged(List<Category> categories)
            {
                // If the list of categories is null or empty, add the defaults
                if (categories == null || categories.isEmpty())
                {
                    Log.v("StartupViewModel", "Adding defaults");
                    List<String> categoryNames = Arrays.asList("Entertainment", "Petrol", "Pets", "Travel", "Shopping", "Income");
                    List<Integer> colorIDs = Arrays.asList(R.color.limeGreen, R.color.lightBlue, R.color.darkOrange, R.color.purple, R.color.teal, R.color.hotPink);

                    for (String i : categoryNames)
                    {
                        // Resolve the color IDs from colors.xml to a color integer before passing to Chip
                        int colorID = colorIDs.get(categoryNames.indexOf(i));

                        Category category = new Category(i, colorID);

                        // Add the default categories
                        dataRepository.insertCategory(category);

                        Log.d("StartupViewModel", "Added default category: " + i);
                    }
                }
                // Remove the observer
                dataRepository.getAllCategories().removeObserver(this);
            }
        });
    }

    /**
     * Validates that a <code>String</code> input is suitable to be saved as a budget
     *
     * @param input a <code>String</code> budget
     * @return <code>ValidationState.NONE</code> if successful, otherwise <code>INVALID_AMOUNT</code> or <code>EMPTY</code>
     */
    @NonNull
    public ValidationState validateBudget(@NonNull String input)
    {
        // Check if empty
        if (!input.isEmpty())
        {
            // Validate the input
            if (InputValidator.validateCurrencyInput(input))
            {
                updatePreferences(Double.parseDouble(input));
                return ValidationState.NONE;
            } else
            {
                return ValidationState.INVALID_AMOUNT;
            }
        } else
        {
            return ValidationState.EMPTY;
        }
    }


    /**
     * Helper method to set the budget requested by a user and update the firstRun flag
     *
     * @param newBudget the new budget set by the user
     */
    private void updatePreferences(@NonNull Double newBudget)
    {
        prefs.edit().putString("budget", String.valueOf(newBudget)).apply();
        prefs.edit().putBoolean("firstRun", false).apply();

        Log.v("FirstTimeStartupActivity", "Updated appPreferences");
    }

    /**
     * Queries the <code>SharedPreferences</code> to get the state of the firstRun flag
     * @return true if is the first run (or by default), false otherwise
     */
    public boolean getFirstRun()
    {
        return prefs.getBoolean("firstRun", true);
    }
}
