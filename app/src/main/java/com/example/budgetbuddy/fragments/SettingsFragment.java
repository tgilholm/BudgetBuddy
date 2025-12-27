package com.example.budgetbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.utility.InputValidator;

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
        addPreferencesFromResource(R.xml.preferences);

        // Validate input before saving
        EditTextPreference budgetPreference = findPreference("budget");
        if (budgetPreference == null)
        {
            Log.e("SettingsFragment", "Failed to load budget preference");
        } else
        {
            budgetPreference.setOnPreferenceChangeListener((preference, newValue) ->
            {
                String validatedInput;
                try
                {
                    // Attempt to cast to a String
                    validatedInput = ((String) newValue).trim();

                    // Validate the input and show a toast if successful
                    if (InputValidator.validateCurrencyInput(validatedInput))
                    {
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
