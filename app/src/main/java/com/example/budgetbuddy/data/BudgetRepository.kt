package com.example.budgetbuddy.data

import java.math.BigDecimal

interface BudgetRepository
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