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
import com.example.budgetbuddy.data.entities.Category;
import com.example.budgetbuddy.utility.TransactionUtils;
import com.example.budgetbuddy.viewmodel.AddViewModel;
import com.example.budgetbuddy.dialogs.DatePickerFragment;
import com.example.budgetbuddy.utility.ChipHandler;
import com.example.budgetbuddy.dialogs.TimePickerFragment;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;
import com.example.budgetbuddy.utility.Convertersold;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The fragment class for the Add section of the app.
 * Connects to fragment_add.xml to provide layout and event listeners.
 * Takes user input and passes it to the ViewModel for validation.
 */
public class AddFragment extends Fragment
{
    private EditText dateText, timeText, amountText;
    private ChipGroup chipGroupCategories;
    private AddViewModel addViewModel;
    private RadioButton rbIncoming, rbNever;
    private RadioGroup radioGroupType, radioGroupRepeat;

    private List<Category> categoryList = new ArrayList<>();
    private String pendingCategory = "";    // Holds the name of a new category to be selected


    /**
     * Called to instantiate the fragment view. Gets Views from the layout, sets click listeners and result listeners,
     * and connects the AddViewModel to get the list of categories
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("AddFragment", "Loaded AddFragment");

        // Instantiate a new View with the inflated XML layout
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        // Get IDs from the view
        FloatingActionButton addButton = v.findViewById(R.id.addButton);
        timeText = v.findViewById(R.id.editTextTime);
        dateText = v.findViewById(R.id.editTextDate);
        amountText = v.findViewById(R.id.editTextAmount);

        chipGroupCategories = v.findViewById(R.id.chipGroupCategories);
        rbIncoming = v.findViewById(R.id.rbIncoming);
        rbNever = v.findViewById(R.id.rbNever);

        radioGroupType = v.findViewById(R.id.radioGroupSelectType);
        radioGroupRepeat = v.findViewById(R.id.radioGroupSelectRepeat);

        addViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);        // Connect the AddViewModel


        dateText.setOnClickListener(v1 ->
        {
            // Open a date picker
            DatePickerFragment datePicker = new DatePickerFragment(getContext());
            Log.d("AddFragment", "Opening datePicker");
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        timeText.setOnClickListener(v1 ->
        {
            // Open a time picker
            TimePickerFragment timePicker = new TimePickerFragment(getContext());
            Log.d("AddFragment", "Opening timePicker");
            timePicker.show(getParentFragmentManager(), "timePicker");
        });
        addButton.setOnClickListener(this::onAddPressed);


        // Get the result from the datePicker and update dateText
        getParentFragmentManager().setFragmentResultListener("dateKey", this, (requestKey, bundle) ->
        {
            String result = bundle.getString("dateKey");
            dateText.setText(result);   // Update the dateText field with the requested date

            Log.d("AddFragment", "Received date from picker: " + result);
        });

        // Get the result from the timePicker and update timeText
        getParentFragmentManager().setFragmentResultListener("timeKey", this, (requestKey, bundle) ->
        {
            String result = bundle.getString("timeKey");
            timeText.setText(result);   // Update the timeText field with the requested date

            Log.d("AddFragment", "Received time from picker: " + result);
        });

        // Start the fragment result listener for the category creator
        getParentFragmentManager().setFragmentResultListener("newCategory", this, (requestKey, bundle) ->
        {
            String categoryName = bundle.getString("categoryName");
            int categoryColor = bundle.getInt("categoryColor");

            // Send the category name and color to the AddViewModel to add a new category
            addViewModel.addCategory(categoryName, categoryColor);
            pendingCategory = categoryName;

            Log.d("AddFragment", "Received new category from creator: " + categoryName);
        });


        // Refreshes the chipGroup with new categories
        addViewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
        {
            categoryList = categories;
            populateChipGroup(categories);
        });

        resetDateTime();   // Sets default values for the date and time fields
        return v;
    }


    /**
     * Creates a new Chip for each of the categories in the category list. The background of the chip is set to the category colour.
     * Additionally creates the "addChip" which opens a CategoryCreatorFragment
     *
     * @param categories a list of Category objects
     *
     */
    private void populateChipGroup(@NonNull List<Category> categories)
    {
        // Empty the ChipGroup
        chipGroupCategories.removeAllViews();

        // Create the "add category" chip
        Chip addChip = ChipHandler.createAddCategoryChip(requireContext());

        // Open the CategoryCreator when clicked
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

        Log.d("AddFragment", "Populated ChipGroup with " + categories.size() + " categories");
    }


    /**
     * Sets the DateText and TimeText fields to the current system time
     */
    private void resetDateTime()
    {
        Calendar userTime = Calendar.getInstance();

        // Always use 00:00 format for time output, i.e. 03:00 instead of 3.0
        // Set the locale to be the user's region
        timeText.setText(Convertersold.calendarToHourMinute(userTime));
        dateText.setText(Convertersold.calendarToDayMonthYear(userTime));

    }

    /**
     * Takes data from the layout fields and passes them to the ViewModel for validation. Handles each ValidationState case
     * with a toast message displaying a short message about each condition
     *
     * @param view Connects to the View
     */
    public void onAddPressed(View view)
    {
        String amount = getAmount();                            // Get the transaction amount
        String date = getDate();                                // Get the date
        String time = getTime();                                // Get the time
        TransactionType type = getType();                       // Get the transaction type
        long categoryID = getSelectedCategoryID();              // Get the transaction category
        RepeatDuration repeatDuration = getRepeatDuration();    // Get the repeat duration


        // Handle the result of adding the transaction
        switch (addViewModel.addTransaction(amount, type, date, time, categoryID, repeatDuration))
        {
            case NONE:
                // Successful add- clear the fields for the next transaction
                Toast.makeText(getContext(), "Added new transaction!", Toast.LENGTH_SHORT).show();
                Log.v("AddFragment", "Added new transaction with amount: " + amount);
                resetFields();
                break;
            case EMPTY:
                Toast.makeText(getContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                break;
            case INVALID_AMOUNT:
                Toast.makeText(getContext(), "Invalid amount! (Format must be XXX.YY)", Toast.LENGTH_SHORT).show();
                break;
            case NO_CATEGORY:
                Toast.makeText(getContext(), "No category selected!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Resets all the input fields back to their defaults
     */
    private void resetFields()
    {
        amountText.setText("");             // Reset the amountText
        radioGroupType.clearCheck();
        rbIncoming.setChecked(true);        // Reset the type
        chipGroupCategories.clearCheck();   // Reset the category chipGroup
        resetDateTime();                    // Reset the date and time
        radioGroupRepeat.clearCheck();
        rbNever.setChecked(true);           // Reset the repeat duration
        Log.d("AddFragment", "Reset all input fields");
    }


    /**
     * Get the amount string from the EditText with whitespace removed.
     *
     * @return the amount string
     */
    @NonNull
    private String getAmount()
    {
        return amountText.getText().toString().trim();
    }

    /**
     * Get the transaction type from the RadioGroup
     *
     * @return the transaction type
     */
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

    /**
     * Returns the category ID stored in the tag of the selected chip.
     *
     * @return the category ID or -1
     */
    private long getSelectedCategoryID()
    {
        // Get the selected chip from the group
        int selectedChipId = chipGroupCategories.getCheckedChipId();

        // If no chip is selected, return -1
        if (selectedChipId == View.NO_ID) // View.NO_ID has a value of -1
        {
            return -1;
        } else
        {
            // Return the tag property of the selected chip
            Chip selectedCategory = chipGroupCategories.findViewById(selectedChipId);
            return (long) selectedCategory.getTag();
        }
    }

    /**
     * Gets the date from the EditText
     *
     * @return the date string
     */
    @NonNull
    private String getDate()
    {
        return dateText.getText().toString();
    }


    /**
     * Gets the time from the EditText
     *
     * @return the time string
     */
    @NonNull
    private String getTime()
    {
        return timeText.getText().toString();
    }


    /**
     * Get the repeat duration from the radio group
     *
     * @return the repeat duration
     */
    private RepeatDuration getRepeatDuration()
    {
        // Get the radio group from the layout
        RadioGroup radioGroupSelectRepeat = requireView().findViewById(R.id.radioGroupSelectRepeat);
        int selectedRadioButtonID = radioGroupSelectRepeat.getCheckedRadioButtonId();

        // Get the selected RadioButton by indexing the radio group
        RadioButton rbSelected = radioGroupSelectRepeat.findViewById(selectedRadioButtonID);

        // Pass the rbSelected field to the selectRepeatDuration method in InputValidator
        return TransactionUtils.selectRepeatDuration(rbSelected.getText().toString());
    }
}