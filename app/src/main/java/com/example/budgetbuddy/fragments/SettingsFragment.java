package com.example.budgetbuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.activities.MainActivity;
import com.example.budgetbuddy.dialogs.ConfirmResetFragment;
import com.example.budgetbuddy.viewmodel.BudgetViewModel;
import com.example.budgetbuddy.viewmodel.SettingsViewModel;

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
        Log.d("SettingsFragment", "Loaded SettingsFragment");

        BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

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
                        Toast.makeText(getContext(), "Budget changed to: £" + newValue, Toast.LENGTH_SHORT)
                                .show();
                        Log.v("SettingsFragment", "Updated budget to: " + newValue);
                        return true; // Accept the new budget, update preferences
                    case INVALID_AMOUNT:
                        Toast.makeText(getContext(), "Invalid amount! (Format must be XXX.YY)", Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    case EMPTY:
                        Toast.makeText(getContext(), "No budget selected!)", Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    default:
                        return false;   // Only accept change if "NONE" validation state received
                }
            });
        }

        // Reset preference
        Preference resetPreference = findPreference("reset_app");
        if (resetPreference != null) {
            resetPreference.setOnPreferenceClickListener(preference -> {
                ConfirmResetFragment.show(getParentFragmentManager());      // Show the confirm screen before committing
                return true;
            });
        }

        // Listen for result from dialog fragment
        getParentFragmentManager().setFragmentResultListener(
                ConfirmResetFragment.REQUEST_KEY,
                this,
                (requestKey, result) -> {
                    boolean confirmed = result.getBoolean(ConfirmResetFragment.RESPONSE_KEY);
                    if (confirmed) {
                        Log.d("SettingsFragment", "App reset confirmed");
                        settingsViewModel.resetDB();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Toast.makeText(getContext(), "App has been reset", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("SettingsFragment", "User canceled app reset");
                    }
                });
    }
}
