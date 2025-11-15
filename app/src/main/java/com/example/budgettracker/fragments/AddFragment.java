package com.example.budgettracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budgettracker.R;
import com.example.budgettracker.utility.DatePickerFragment;
import com.example.budgettracker.utility.TimePickerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * The fragment subclass for the Add section of the app
 * Connects to fragment_add.xml to provide layout
 */

public class AddFragment extends Fragment
{
    private EditText dateText;
    private EditText timeText;
    private FloatingActionButton addButton;


    // Used to hold user-defined time values
    private final Calendar userTime;

    // Update UI to be more like Google Calendar
    // Dropdown menu for time and date & keep both selected
    /*
    TODO: Add Functionality- save new transaction to persistent storage, update all screens
    If connected to cloud, save to cloud.
     */
    /*
    TODO: Chip group containing chips representing each category- default categories & user defined
     */

    public AddFragment()
    {
        // Get the current time in the constructor
        userTime = Calendar.getInstance();
    }

    public static AddFragment newInstance(String param1, String param2)
    {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
        }
    }

    // Creates the layout and event listeners for the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // Instantiate a new View with the inflated XML layout
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // VIEW IDs
        addButton = v.findViewById(R.id.addButton);
        timeText = v.findViewById(R.id.editTextTime);
        dateText = v.findViewById(R.id.editTextDate);


        // ONCLICK LISTENERS
        // Connect an onClickListener to the date and time fields
        dateText.setOnClickListener(this::onDatePressed);
        timeText.setOnClickListener(this::onTimePressed);

        // Connect an onClickListener the add button
        addButton.setOnClickListener(this::onAddPressed);


        // FRAGMENT RESULT LISTENERS
        /*
        Connects the date and time picker fragments to the parent fragment manager
        onFragmentResult is used to take the bundled date or time from the fragment
        and pass it to AddFragment
         */
        getParentFragmentManager().setFragmentResultListener("dateKey", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle)
            {
                String result = bundle.getString("dateKey");
                dateText.setText(result);   // Update the dateText field with the requested date
            }
        });

        getParentFragmentManager().setFragmentResultListener("timeKey", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle)
            {
                String result = bundle.getString("timeKey");
                timeText.setText(result);   // Update the timeText field with the requested date
            }
        });

        // STARTUP LOGIC
        /*
        - Sets default values for the date and time fields
         */

        // Set the date and time fields to the current date & time
        int year = userTime.get(Calendar.YEAR);
        int month = userTime.get(Calendar.MONTH);
        int day = userTime.get(Calendar.DAY_OF_MONTH);

        // Month is indexed from 0, so add 1 to prevent off by ones
        String currentDate = day + "/" + (month + 1) + "/" + year;
        dateText.setText(currentDate);

        int minute = userTime.get(Calendar.MINUTE);
        int hour = userTime.get(Calendar.HOUR_OF_DAY);

        // Always use 00:00 format for time output, i.e. 03:00 instead of 3.0
        // Set the locale to be the user's region
        timeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        return v;
    }


    // Handle the logic for the fragment startup
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    // Open a DatePickerDialog when the user interacts with the date field
    public void onDatePressed(View view)
    {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getParentFragmentManager(), "datePicker");

    }

    // Open a TimePickerDialog when the user interacts with the date field
    public void onTimePressed(View view)
    {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getParentFragmentManager(), "timePicker");
    }

    // Handle the logic for the add button
    // Collects all the user input into a new transaction
    public void onAddPressed(View view)
    {
        getAmount();
        // Get the transaction amount
        // Get the incoming/outgoing
        // Get the transaction category
        // Get the repeat duration of the transaction

        // Get transaction date and time from EditText fields


    }

    // Get the transaction amount from the user
    private double getAmount()
    {
        // Get the amount from the amountText field
        EditText amountText = requireView().findViewById(R.id.editTextAmount);
        String amount = amountText.getText().toString().trim(); // Remove any whitespace

        // Verify format is correct, inform user via toasts about any errors
        // Check if amountText is empty- this is also validated by the regex but is more specific
        if (amount.isEmpty())
        {
            Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
            return -1; // -1 is the "error code"
        }

        // Regular expression for checking amount format is correct
        /* Regex:
            Breakdown:
            [0-9]+          Matches if the string contains one-or-more numbers- i.e. 50
            [.]             Matches if the string contains a decimal point
            [0-9]{2}        Matches if numeric and ONLY 2 d.p. i.e. 50.12 instead of 50.123
            ([.][0-9]{2})?  The '?' indicates that the entire block is optional (zero or one)
                            This means that 50 is valid, and so is 50.12

            Complete regex: [0-9]+([.][0-9]{2})?
        */
        String regex = "[0-9]+([.][0-9]{2})?";

        // Use the regex to check whether the string meets the format "123" or "123.12"
        if (!(amount.matches(regex)))
        {
            Toast.makeText(getContext(), "Please enter an amount in the format '***' or '***.**'", Toast.LENGTH_LONG).show();
            return -1;
        }
        // If the string is formatted correctly, return it
        // Put the return in a try-catch in case the user tries any funny business not caught by the regex
        try
        {
            return Double.parseDouble(amount);
        } catch (NumberFormatException funnyBusiness)
        {
            Toast.makeText(getContext(), "Error getting amount value", Toast.LENGTH_LONG).show();
            Log.e("Error: ", funnyBusiness.toString());
            return -1;
        }
    }
}