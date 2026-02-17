package com.example.budgetbuddy.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.budgetbuddy.data.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of PreferenceRepository accessing user data with Jetpack
 * DataStore, which supersedes SharedPreferences and allows the use of coroutines
 */
class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>   // Inject dataStore dependency
) : PreferenceRepository
{

    /**
     * Holds the dataStore keys of the preferences' key-value pairs
     */
    private object Keys
    {
        val BUDGET = doublePreferencesKey("budget") // location of "budget" key-value pair
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed") // first run or no
    }

    /*
    Overrides the base budget value in the interface. Classes observe the budget without needing to know the
    implementation behind them.

    Here the dataStore is queried to find the budget. If it cannot be found or does not exist, it is created
    with that key and the default value- in this case, 0.0, is returned.
     */
    override val budget: Flow<Double>
        get() = dataStore.data.map { prefs -> prefs[Keys.BUDGET] ?: 0.0 }


    // The ONBOARDING_COMPLETED flag does not exist when the app is first created, so it defaults to false
    override val isOnboardingCompleted: Flow<Boolean>
        get() = dataStore.data.map { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] ?: false
        } // if not found, return false

    /**
     * Updates the Double budget value in preferences with a new value
     */
    override suspend fun updateBudget(newBudget: Double)
    {
        dataStore.edit { prefs ->
            prefs[Keys.BUDGET] = newBudget  // Replaces the old budget with the new one
        }
    }

    /**
     * Sets the value of the "first run" or onboarding flag to true or false
     * Setting this value to true will avoid the "startup" page on subsequent runs
     */
    override suspend fun setOnboardingCompleted(completed: Boolean)
    {
        dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed
        }
    }
}