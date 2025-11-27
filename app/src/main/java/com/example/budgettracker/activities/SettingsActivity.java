package com.example.budgettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.budgettracker.R;
import com.example.budgettracker.fragments.SettingsFragment;


// The SettingsActivity contains the following options
/*
    Edit the pre-set budget
    Remove all data and start again (following a confirmation screen) TODO re-usable confirmation screen
    Sync data to the cloud TODO cloud-based sync
    Enable/disable notifications and set notification settings
    // TODO back button
 */
public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_settings); // Get the layout from the XML
        EdgeToEdge.enable(this);

        // Load the SettingsFragment by replacing the FrameLayout
        getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, new SettingsFragment()).commit();
    }

    // Return to the mainActivity
    public void backButtonPressed(View view)
    {
        returnToMain();
    }

    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
