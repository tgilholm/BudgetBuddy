package com.example.budgetbuddy.data

import java.math.BigDecimal

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
    /**
     * Returns the stored budget quantity as a nullable BigDecimal
     */
    fun getBudget(): BigDecimal?

    /**
     * Sets the stored budget quantity to a new BigDecimal value
     *
     * Note - if using SharedPreferences, convert to string before storing
     */
    fun setBudget(newBudget: BigDecimal)

    /**
     * Resets the stored budget quantity back to the specified default
     */
    fun resetBudget()
}