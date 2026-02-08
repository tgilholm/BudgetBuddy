package com.example.budgetbuddy.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Extends <code>DialogFragment</code>, implements methods from <code>DatePickerDialog.OnDateSetListener</code>.
 * Prompts a user to input a date and returns the result.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{

    private final Context context;

    /**
     * Constructs a new DatePickerFragment
     *
     * @param context The application context
     */
    public DatePickerFragment(Context context)
    {
        this.context = context;
    }

    /**
     * Called to create the DatePicker dialog. Passes the current date to the dialog
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return A new <code>DatePickerDialog</code> instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Log.d("DatePickerFragment", "Loaded date picker fragment");

        // Get the current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        // Pass the current date as the default value
        return new DatePickerDialog(context, this, year, month, dayOfMonth);
    }

    /**
     * Called when the user sets the date in the dialog. Passes the date value to a parent fragment through a bundle
     * and <code>setFragmentResult()</code>.
     *
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        String dateString = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year); // month + 1, month is 0-indexed

        Bundle bundle = new Bundle();
        bundle.putString("dateKey", dateString);
        getParentFragmentManager().setFragmentResult("dateKey", bundle); // Set the fragment result

        Log.d("DatePickerFragment", "Date set to " + dateString);
    }
}
