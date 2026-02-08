package com.example.budgetbuddy.ui.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.budgetbuddy.data.db.javadb;

/**
 * Used to perform factory reset operations from settings.
 * Has direct access to a database instance
 */
public class SettingsViewModel extends AndroidViewModel
{
    private final SharedPreferences sharedPreferences;
    private final javadb db;

    public SettingsViewModel(@NonNull Application application)
    {
        super(application);

        db = javadb.getDBInstance(application);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    /**
     * Reset the app by clearing all transactions categories and reset the budget.
     * Updates firstRun to false again. This executes in a background thread.
     */
    public void resetDB()
    {
        javadb.databaseWriteExecutor.execute(() ->
        {
            db.clearAllTables();
            resetSharedPrefs();
        });
    }


    /**
     * Resets the SharedPreferences budget & firstRun to default values.
     * This is done on the main thread.
     */
    private void resetSharedPrefs()
    {
        // Reset SharedPreferences
        getApplication().getMainExecutor()
                .execute(() ->
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // Set firstRun to true
                    editor.putBoolean("firstRun", true);
                    editor.putString("budget", "0");    // Set budget to 0
                    editor.apply();
                });
    }
}
