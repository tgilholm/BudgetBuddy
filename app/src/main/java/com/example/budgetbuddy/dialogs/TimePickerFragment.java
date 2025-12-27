package com.example.budgetbuddy.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Extends <code>DialogFragment</code>, implements methods from <code>TimePickerDialog.OnTimeSetListener</code>.
 * Prompts a user to input a time and returns the result.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{

    private final Context context;

    /**
     * Constructs a new TimePickerFragment
     *
     * @param context The application context
     */
    public TimePickerFragment(Context context)
    {
        this.context = context;
    }

    /**
     * Called to create the TimePicker dialog. Passes the current time to the dialog
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return A new <code>TimePickerDialog</code> instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Log.d("TimePickerFragment", "Loaded time picker fragment");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(context, this, hour, minute, true);
    }

    /**
     * Called when the user sets the time int he dialog. Passes the time value to a parent fragment through a bundle
     * and <code>setFragmentResult()</code>.
     *
     * @param view      the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute    the minute that was set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {

        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);        // Always use 00:00 format for time

        Bundle bundle = new Bundle();
        bundle.putString("timeKey", timeString);
        getParentFragmentManager().setFragmentResult("timeKey", bundle); // Set the fragment result

        Log.d("TimePickerFragment", "Set time as: " + timeString);
    }
}
