package com.example.budgetbuddy.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract implementations must follow for accessing
 * the data layer and carrying out operations pertaining to budget, firstRun
 * and other preference-level flags
 *
 * The interface segregation principle is enforced here, favouring small,
 * focused interfaces over "fat" ones, or a "god repository".
 */
interface PreferenceRepository
{
    val budget: Flow<Double>    // Observable budget value, auto-updated
    val isOnboardingCompleted: Flow<Boolean>

    /**
     * Updates the Double budget value in preferences with a new value
     */
    suspend fun updateBudget(newBudget: Double)

    /**
     * Sets the value of the "first run" or onboarding flag to true or false
     * Setting this value to true will avoid the "startup" page on subsequent runs
     */
    suspend fun setOnboardingCompleted(completed: Boolean)
}