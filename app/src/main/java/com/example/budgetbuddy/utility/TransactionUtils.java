package com.example.budgetbuddy.utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class to handle transaction logic operations
 */
public final class TransactionUtils
{
    // Final class, no instantiation
    private TransactionUtils() {}


    /**
     * Sorts a list of transactions by their date/time
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return the sorted list
     */
    @NonNull
    @Contract("_ -> param1")
    public static List<TransactionWithCategory> sortTransactions(@NonNull List<TransactionWithCategory> transactions)
    {
        transactions.sort((o1, o2) ->
                o2.transaction.getDateTime().compareTo(o1.transaction.getDateTime()));
        return transactions;
    }

    /**
     * Converts an <code>input</code> into a <code>RepeatDuration</code>
     * @param input a name in the enum
     * @return a <code>RepeatDuration</code> type
     */
    @Nullable
    @Contract(pure = true)
    public static RepeatDuration selectRepeatDuration(@NonNull String input)
    {
        switch (input)
        {
            case "Never":
                return RepeatDuration.NEVER;
            case "Day":
                return RepeatDuration.DAILY;
            case "Week":
                return RepeatDuration.WEEKLY;
            case "Month":
                return RepeatDuration.MONTHLY;
            case "Year":
                return RepeatDuration.YEARLY;
            default:
                return null;
        }
    }

    /**
     * Gets the total spending of a list of <code>Transaction</code> objects
     * @param transactions a list of <code>Transaction</code> objects
     * @return the sum of all the amounts, as a <code>double</code>
     */
    public static double getTotalSpend(List<TransactionWithCategory> transactions)
    {
        // todo convert to stream
        double total = 0;
        if (transactions != null)
        {
            for (TransactionWithCategory t : transactions)
            {
                Transaction transaction = t.transaction;
                if (transaction.getType() == TransactionType.OUTGOING)
                {
                    total += transaction.getAmount();
                }
            }
        }
        return total;
    }

    /**
     * Calculates the remaining budget given a starting budget and a list of transactions
     * @param start the <code>double</code> starting budget
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>double</code> sum of outgoing transactions
     */
    public static double getBudgetRemaining(double start, List<TransactionWithCategory> transactions)
    {
        // todo convert to stream
        if (transactions == null)
        {
            return start;
        }

        double result = start;
        for (TransactionWithCategory t : transactions)
        {
            Transaction transaction = t.transaction;
            if (transaction.getType() == TransactionType.OUTGOING)
            {
                result -= transaction.getAmount();            // Subtract outgoings
            } else
            {
                result += transaction.getAmount();            // Add incoming
            }
        }
        return result;
    }


    /**
     * Gets the sum of outgoing amounts from a list of <code>TransactionWithCategory</code> objects,
     * and aggregates them into a map of <code>Category</code> and <code>Double</code> objects
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>Map</code> of <code>Category</code> and <code>Double</code> objects with aggregated spending
     */
    @NonNull
    public static Map<Category, Double> getCategoryTotals(List<TransactionWithCategory> transactions)
    {
        // todo convert to stream
        Map<Category, Double> totalPerCategory = new HashMap<>();

        // Checks if transactions is null
        if (transactions == null)
        {
            return totalPerCategory;    // Returns empty HashMap if so
        }

        // Put the transactions into the Map
        for (TransactionWithCategory t : transactions)
        {
            Transaction transaction = t.transaction;
            Category category = t.category;
            double amount = transaction.getAmount();

            // Only handles "outgoing" transactions
            if (transaction.getType() == TransactionType.OUTGOING)
            {
                // Uses Map.merge() instead of if-else statements
                // Adds the amount to the total if the category already exists in the map
                // Otherwise, adds the category to the map with amount as the value
                totalPerCategory.merge(category, amount, Double::sum);
            }
        }
        return totalPerCategory;
    }
}
