package com.example.budgetbuddy.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.enums.ValidationState;
import com.example.budgetbuddy.repositories.DataRepository;
import com.example.budgetbuddy.utility.InputValidator;

import java.util.Arrays;
import java.util.List;

// Used to add default categories to the Category table in the DB
public class StartupViewModel extends AndroidViewModel
{

    private final DataRepository dataRepository;

    private final SharedPreferences prefs;

    public StartupViewModel(Application application)
    {
        super(application);

        // Get an instance of the DataRepository
        dataRepository = DataRepository.getInstance(application);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(application);
    }

    // Query the database to check if there are any categories- if not, add them
    public void addDefaultCategories() {

        // Use observeForever to get the list of categories
        dataRepository.getAllCategories().observeForever(new Observer<>() {
            @Override
            public void onChanged(List<Category> categories) {
                // If the list of categories is null or empty, add the defaults
                if (categories == null || categories.isEmpty()) {
                    Log.v("StartupViewModel", "Adding defaults");
                    List<String> categoryNames = Arrays.asList("Entertainment", "Petrol", "Pets", "Travel", "Shopping", "Income");
                    List<Integer> colorIDs = Arrays.asList(R.color.limeGreen, R.color.lightBlue, R.color.darkOrange, R.color.purple, R.color.teal, R.color.hotPink);

                    for (String i : categoryNames) {
                        // Resolve the color IDs from colors.xml to a color integer before passing to Chip
                        int colorID = colorIDs.get(categoryNames.indexOf(i));

                        Category category = new Category(i, colorID);

                        // Add the default categories
                        dataRepository.insertCategory(category);
                    }
                }
                // Remove the observer
                dataRepository.getAllCategories().removeObserver(this);
            }
        });
    }

    @NonNull
    public ValidationState setBudget(@NonNull String input)
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
        }
        else {
            return ValidationState.EMPTY;
        }
    }


    private void updatePreferences(@NonNull Double newBudget)
    {

        prefs.edit().putFloat("budget", newBudget.floatValue());
        prefs.edit().putBoolean("notFirstRun", true);
        prefs.edit().apply(); // Commit the changes

        Log.v("FirstTimeStartupActivity", "Updated appPreferences");
    }
}
