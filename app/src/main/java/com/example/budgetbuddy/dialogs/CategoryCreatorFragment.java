package com.example.budgetbuddy.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.adapters.ColorPickerAdapter;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.utility.ColorGridDecoration;

import java.util.List;

/**
 * Extends DialogFragment. Takes user input and creates a new category.
 * Displays a <code>RecyclerView</code> containing a grid of selectable colours
 */
public class CategoryCreatorFragment extends DialogFragment
{
    private final Context context;
    private final List<Category> categories; // Hold a list of categories for input validation
    private int colorChoice = -1;


    /**
     * Constructs a new CategoryCreatorFragment
     * @param context The application context
     * @param categories A list of <code>Category</code> objects
     */
    public CategoryCreatorFragment(Context context, List<Category> categories)
    {
        this.context = context;
        this.categories = categories;
    }

    /**
     * Called to instantiate the fragment view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_creator, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.colorGrid);
        Button addCategoryButton = view.findViewById(R.id.addCategory);
        EditText editTextCategory = view.findViewById(R.id.editTextCategoryName);

        final int columnCount = 4;    // 4 Columns
        final int spacing = 16;

        // List of colours from colors.xml
        Integer[] colorIDs = new Integer[]{
                R.color.red,
                R.color.hotPink,
                R.color.purple,
                R.color.darkPurple,
                R.color.darkBlue,
                R.color.lightBlue,
                R.color.blue,
                R.color.cyan,
                R.color.teal,
                R.color.green,
                R.color.limeGreen,
                R.color.yellowGreen,
                R.color.yellow,
                R.color.lightOrange,
                R.color.orange,
                R.color.darkOrange
        };

        // Anonymous class extending GridLayoutManager to disable recycler view scrolling
        // This also maintains the ability to click on items
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount)
        {
            final boolean isScrollEnabled = false;

            /**
             * Disables vertical scrolling
             * @return always returns false
             */
            @Override
            public boolean canScrollVertically()
            {
                return isScrollEnabled && super.canScrollVertically();
            }
        });

        // Get the colour ID from the chosen list item
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(context, List.of(colorIDs), selectedColour ->
        {
            // When a colour is selected, set it to "ticked" and get the colour from it
            colorChoice = selectedColour;
        });

        // Space apart items in the grid
        recyclerView.addItemDecoration(new ColorGridDecoration(columnCount, spacing, false));
        recyclerView.setAdapter(colorPickerAdapter);

        // Add the click listener to the button
        addCategoryButton.setOnClickListener(v ->
        {
            String inputName = editTextCategory.getText().toString();

            if (!inputName.isEmpty())
            {
                // Check that colorChoice is not set to -1 (the default value, indicating no colour selection)
                if (colorChoice != -1)
                {
                    if (!categoryNameExists(inputName, categories))
                    {
                        // If all checks succeeded, send the category name and colour back to the AddFragment
                        Bundle bundle = new Bundle();
                        String categoryName = editTextCategory.getText().toString();
                        bundle.putString("categoryName", categoryName);        // Attach the category name
                        bundle.putInt("categoryColor", colorChoice);           // Attach the color ID

                        // Use FragmentResult to send a message to the MainActivity
                        getParentFragmentManager().setFragmentResult("newCategory", bundle);

                        // Tell the user via a toast that a new category was added
                        Toast.makeText(context, "New category " + categoryName + " was added!", Toast.LENGTH_SHORT).show();

                        this.dismiss();
                    } else
                    {
                        editTextCategory.setError(inputName + " category already exists!");
                    }
                } else
                {
                    editTextCategory.setError("Please select a colour!");
                }
            } else
            {
                editTextCategory.setError("Please type in a category name!");
            }
        });

        return view;
    }

    /**
     * Compares the category name to the list of categories to check if it already exists
     * @param categoryName the name of the category to check
     * @param categories a list of <code>Category</code> objects
     * @return true if the category already exists, false otherwise
     */
    private boolean categoryNameExists(String categoryName, List<Category> categories)
    {
        boolean exists = false;

        if (categories != null)
        {
            for (Category category : categories)
            {
                // Use equalsIgnoreCase for more rigorous checking
                if (category.getName().trim().equalsIgnoreCase(categoryName.trim()))
                {
                    exists = true;
                    break;  // Break the loop if found
                }
            }
        }

        return exists;
    }
}
