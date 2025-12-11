package com.example.budgetbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.utility.InputValidator;

// The SettingsFragment contains the following options
/*
    Edit the pre-set budget
    Remove all data and start again (following a confirmation screen)
 */
public class SettingsFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey)
    {
        addPreferencesFromResource(R.xml.preferences);


        // INPUT VALIDATION
        // Validate the change to a budget
        // This is done by intercepting the change and passing it through the InputValidator
        // before it can be saved to appPreferences

        EditTextPreference budgetPreference = findPreference("budget");
        if (budgetPreference != null) {

            // Use a PreferenceChangeListener to detect changes to the value
            // preference refers to the preference being changed, newValue is the value a user typed in
            budgetPreference.setOnPreferenceChangeListener((preference, newValue) ->
            {
                String validatedInput;
                try
                {
                    // Attempt to cast to a String
                    validatedInput = ((String) newValue).trim();

                    // Validate the input and show a toast if successful
                    if (InputValidator.validateCurrencyInput(getContext(), validatedInput)) {
                        Toast.makeText(getContext(), "Budget changed to: £" + validatedInput, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    Log.v("SettingsFragment", "Failed to set preferences");
                    return false;

                } catch (Exception e)
                {
                    Toast.makeText(getContext(), "Invalid input!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

    }
}
