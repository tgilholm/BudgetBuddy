package com.example.budgetbuddy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.dialogs.CategoryCreatorFragment;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.viewmodel.AddViewModel;
import com.example.budgetbuddy.dialogs.DatePickerFragment;
import com.example.budgetbuddy.utility.ChipHandler;
import com.example.budgetbuddy.utility.InputValidator;
import com.example.budgetbuddy.dialogs.TimePickerFragment;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;
import com.example.budgetbuddy.utility.Converters;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The fragment subclass for the Add section of the app
 * Connects to fragment_add.xml to provide layout
 */

public class AddFragment extends Fragment
{
    private EditText dateText;
    private EditText timeText;
    private EditText amountText;
    private ChipGroup chipGroupCategories;
    private AddViewModel addViewModel;
    private RadioButton rbIncoming;
    private RadioGroup radioGroupType;
    private RadioGroup radioGroupRepeat;

    private List<Category> categoryList = new ArrayList<>();
    private String pendingCategory = "";    // Holds the name of a new category to be selected


    // Creates the layout and event listeners for the fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Instantiate a new View with the inflated XML layout
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // Get IDs from the view
        FloatingActionButton addButton = v.findViewById(R.id.addButton);
        timeText = v.findViewById(R.id.editTextTime);
        dateText = v.findViewById(R.id.editTextDate);
        chipGroupCategories = v.findViewById(R.id.chipGroupCategories);
        amountText = v.findViewById(R.id.editTextAmount);
        rbIncoming = v.findViewById(R.id.rbIncoming);
        radioGroupType = v.findViewById(R.id.radioGroupSelectType);
        radioGroupRepeat = v.findViewById(R.id.radioGroupSelectRepeat);


        // Connect the AddViewModel
        addViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);


        // Set the onClickListener for the time and date pickers
        dateText.setOnClickListener(v1 ->
        {
            // Open a date picker
            DatePickerFragment datePicker = new DatePickerFragment(getContext());
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        timeText.setOnClickListener(v1 ->
        {
            // Open a time picker
            TimePickerFragment timePicker = new TimePickerFragment(getContext());
            timePicker.show(getParentFragmentManager(), "timePicker");
        });
        addButton.setOnClickListener(this::onAddPressed);


        // FRAGMENT RESULT LISTENERS
        /*
        Connects the date and time picker fragments to the parent fragment manager
        onFragmentResult is used to take the bundled date or time from the fragment
        and pass it to AddFragment
         */
        getParentFragmentManager().setFragmentResultListener("dateKey", this, (requestKey, bundle) ->
        {
            String result = bundle.getString("dateKey");
            dateText.setText(result);   // Update the dateText field with the requested date
        });

        getParentFragmentManager().setFragmentResultListener("timeKey", this, (requestKey, bundle) ->
        {
            String result = bundle.getString("timeKey");
            timeText.setText(result);   // Update the timeText field with the requested date
        });

        // Start the fragment result listener for the category creator
        getParentFragmentManager().setFragmentResultListener("newCategory", this, (requestKey, bundle) ->
        {
            String categoryName = bundle.getString("categoryName");
            int categoryColor = bundle.getInt("categoryColor");

            // Send the category name and color to the AddViewModel to add a new category
            addViewModel.addCategory(categoryName, categoryColor);
            pendingCategory = categoryName;
        });


        // Set up the observer on the categories list
        // When new categories are added, refresh the category list
        addViewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
        {
            categoryList = categories;
            populateChipGroup(categories);

        });

        resetDateTime();   // Sets default values for the date and time fields
        return v;
    }

    // Load the categories from the database and create chips for each
    // The chipGroup is recreated each time a new category is added
    private void populateChipGroup(@NonNull List<Category> categories)
    {
        // Remove all the chips
        chipGroupCategories.removeAllViews();

        // Create and add the "add category" chip
        Chip addChip = ChipHandler.createAddCategoryChip(requireContext());

        // Set the click listener of the "addChip" to open the category creation screen
        addChip.setOnClickListener(v ->
        {
            CategoryCreatorFragment categoryCreatorFragment = new CategoryCreatorFragment(requireContext(), categoryList);
            categoryCreatorFragment.show(getParentFragmentManager(), "categoryCreator");
        });

        // Add the category chips
        for (Category c : categories)
        {
            Chip chip = ChipHandler.createChip(requireContext(), c);

            // Force the view to give the chip an ID
            if (chip.getId() == View.NO_ID)
            {
                chip.setId(View.generateViewId());
            }

            chipGroupCategories.addView(chip);

            // Check if the pendingCategory string matches the chip name
            // If so, check (select) the chip and reset pendingCategory
            if (pendingCategory != null && pendingCategory.equals(c.getName()))
            {
                Log.v("AddFragment", "Checking chip");
                chipGroupCategories.check(chip.getId());
                pendingCategory = null;
            }
        }
        chipGroupCategories.addView(addChip);
    }


    // Sets the DateText and TimeText fields to the current time
    private void resetDateTime()
    {
        Calendar userTime = Calendar.getInstance();

        // Always use 00:00 format for time output, i.e. 03:00 instead of 3.0
        // Set the locale to be the user's region
        timeText.setText(Converters.calendarToHourMinute(userTime));
        dateText.setText(Converters.calendarToDayMonthYear(userTime));

    }

    // Handle the logic for the add button
    // Collects all the user input into a new transaction
    public void onAddPressed(View view)
    {
        // Bundle all the user input into a new transaction
        double amount = getAmount();        // Get the transaction amount
        if (amount < 0)
        {
            return;
        }         // Break here if getAmount() failed

        TransactionType type = getType();   // Get the transaction type

        Calendar dateTime = getDateAndTime();
        if (dateTime == null)
        {
            return;
        }     // Break here if getDateAndTime() failed

        long categoryID = getCategoryID();    // Get the transaction category
        if (categoryID < 0)
        {
            return;
        }     // Break here if getCategoryID() failed

        RepeatDuration repeatDuration = getRepeatDuration();

        Log.v("AddFragment", "Adding new transaction");

        // Send the transaction details to the ViewModel
        addViewModel.addTransaction(amount, type, dateTime, categoryID, repeatDuration);

        // Inform the user via a toast that the transaction was added
        Toast.makeText(getContext(), "Added new transaction!", Toast.LENGTH_SHORT).show();

        // Clear all the input fields
        resetFields();
    }

    // Set all the input fields back to their defaults
    private void resetFields()
    {
        amountText.setText("");             // Reset the amountText
        radioGroupType.clearCheck();        // Reset the type
        chipGroupCategories.clearCheck();   // Reset the category chipGroup
        resetDateTime();                    // Reset the date and time
        radioGroupRepeat.clearCheck();      // Reset the repeat duration
    }


    // Get the transaction amount inputted by the user
    private double getAmount()
    {
        // Get the amount from the amountText field
        String amount = amountText.getText().toString().trim(); // Remove any whitespace

        // Validate the input
        if (InputValidator.validateCurrencyInput(getContext(), amount))
        {
            return Double.parseDouble(amount);
        } else
        {
            Log.v("AddFragment", "Invalid amount input");
            return 0;
        }
    }

    // Return the type of the transaction- incoming or outgoing
    private TransactionType getType()
    {
        // Only one radio button can be selected at a time
        if (rbIncoming.isChecked())
        {
            return TransactionType.INCOMING;
        } else
        {
            return TransactionType.OUTGOING;
        }
    }

    // Return the category of the transaction
    private long getCategoryID()
    {
        // The ChipGroup uses Single Selection mode, making the process of finding the checked chip faster
        int selectedChipId = chipGroupCategories.getCheckedChipId();

        // If no chip is selected, inform the user via a toast message
        if (selectedChipId == View.NO_ID) // View.NO_ID has a value of -1
        {
            Toast.makeText(getContext(), "No Category Selected!", Toast.LENGTH_SHORT).show();
            return 0; // Return null if no chip is selected
        } else
        {
            // Return the tag property of the selected chip
            Chip selectedCategory = chipGroupCategories.findViewById(selectedChipId);
            return (long) selectedCategory.getTag();
        }
    }

    // Gets the date and time in the dateText and timeText fields and returns them as a Calendar object
    // Storing the values as a Calendar helps with operations elsewhere in the program
    private Calendar getDateAndTime()
    {
        // Get the values from the EditText fields
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();

        // Pass the date and time values to the validateDateAndTime method
        return InputValidator.validateDateTimeInput(getContext(), date + " " + time);
    }

    // Get the repeat duration from the radio group
    private RepeatDuration getRepeatDuration()
    {
        // Get the radio group from the layout
        RadioGroup radioGroupSelectRepeat = requireView().findViewById(R.id.radioGroupSelectRepeat);
        int selectedRadioButtonID = radioGroupSelectRepeat.getCheckedRadioButtonId();

        // Get the selected RadioButton by indexing the radio group
        RadioButton rbSelected = radioGroupSelectRepeat.findViewById(selectedRadioButtonID);

        // Pass the rbSelected field to the selectRepeatDuration method in InputValidator
        return InputValidator.selectRepeatDuration(rbSelected.getText().toString());
    }
}