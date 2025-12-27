package com.example.budgetbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.viewmodel.BudgetViewModel;

/**
 * Extends PreferenceFragmentCompat. Loads <code>preferences.xml</code> to create the options,
 * carries out input validation and displays settings to the user.
 */
public class SettingsFragment extends PreferenceFragmentCompat
{
    /**
     * Gets the preferences from preferences.xml. Intercepts new preferences with a
     * <code>onPreferenceChangeListener</code>.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey)
    {
        // Get an instance of the BudgetViewModel
        BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // Load the preferences from the xml file
        addPreferencesFromResource(R.xml.preferences);

        // Null-check
        EditTextPreference budgetPreference = findPreference("budget");
        if (budgetPreference == null)
        {
            Log.e("SettingsFragment", "Failed to load budget preference");
        } else
        {
            // Send new budgets to the BudgetViewModel to save & validate
            budgetPreference.setOnPreferenceChangeListener((preference, newValue) ->
            {
                switch (budgetViewModel.validateBudget(newValue))
                {
                    case NONE:
                        Toast.makeText(getContext(), "Budget changed to: £" + newValue, Toast.LENGTH_SHORT).show();
                        Log.v("SettingsFragment", "Updated budget to: " + newValue);
                        return true; // Accept the new budget, update preferences
                    case INVALID_AMOUNT:
                        Toast.makeText(getContext(), "Invalid amount! (Format must be XXX.YY)", Toast.LENGTH_SHORT).show();
                        return false;
                    case EMPTY:
                        Toast.makeText(getContext(), "No budget selected!)", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        return false;   // Only accept change if "NONE" validation state received
                }
            });
        }
    }
}
