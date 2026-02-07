package com.example.budgetbuddy.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;

/**
 * Defines data-layer operations on <code>Budget</code> data.
 */
public interface BudgetRepository
{
    /**
     * Returns the budget quantity stored in SharedPreferences, DataStore etc.
     *
     * @return a <code>BigDecimal</code> representation of the budget
     */
    @Nullable
    BigDecimal getBudget();

    /**
     * Updates the budget amount stored in SharedPreferences, DataStore etc.
     *
     * @param newBudget the <code>BigDecimal</code> new value for the budget.
     */
    void setBudget(@NonNull BigDecimal newBudget);

    /**
     * Resets the budget back to the default: ""
     */
    void resetBudget();
}
