package com.example.budgetbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.viewmodel.StartupViewModel;

/**
 * Activity to run on the first startup of the application. Accepts user-defined budget & passes
 * budget values to StartupViewModel.
 */
public class FirstTimeStartupActivity extends AppCompatActivity
{
    private StartupViewModel startupViewModel;

    /**
     * Initialises <code>budgetText</code> and <code>startupViewModel</code>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the layout from the XML file
        setContentView(R.layout.activity_first_startup);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        // Get an instance of the StartupViewModel
        startupViewModel = new ViewModelProvider(this).get(StartupViewModel.class);
    }

    /**
     * Invoked when start button is pressed. Takes user input from budgetText & passes to <code>StartupViewModel</code>.
     * Alerts user with <code>Toast</code> messages for invalid <code>ValidationState</code> values
     *
     * @param v the view
     */
    public void startButtonPressed(View v)
    {
        EditText budgetText = findViewById(R.id.editTextBudget);        // Get the EditText from the view

        // Get the input text and pass to the ViewModel
        switch (startupViewModel.validateBudget(budgetText.getText().toString()))
        {
            case NONE:
                goToMain();
                startupViewModel.addDefaultCategories(); // Add default categories
                break;
            case INVALID_AMOUNT:
                Toast.makeText(this, "Invalid budget! (Format must be XXX.YY)", Toast.LENGTH_SHORT).show();
                break;
            case EMPTY:
                Toast.makeText(this, "Please enter a budget!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Starts an <code>Intent</code> to go to <code>MainActivity</code>
     */
    private void goToMain()
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
