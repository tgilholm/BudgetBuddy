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
import java.util.stream.Collectors;


/**
 * Utility class to handle transaction logic operations
 */
public final class TransactionUtils
{
    // Final class, no instantiation
    private TransactionUtils()
    {
    }


    /**
     * Sorts a list of transactions by their date/time
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return the sorted list
     */
    @NonNull
    @Contract("_ -> param1")
    public static List<TransactionWithCategory> sortTransactions(@NonNull List<TransactionWithCategory> transactions)
    {
        transactions.sort((o1, o2) -> o2.transaction.getDateTime()
                .compareTo(o1.transaction.getDateTime()));
        return transactions;
    }

    /**
     * Converts an <code>input</code> into a <code>RepeatDuration</code>
     *
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
     *
     * @param transactions a list of <code>Transaction</code> objects
     * @return the sum of all the amounts, as a <code>double</code>
     */
    public static double getTotalSpend(@NonNull List<TransactionWithCategory> transactions)
    {
        /*
        Takes the list of TransactionWithCategory objects & extracts the Transaction.
        Filters to only use outgoing transactions
        Gets the amount from each transaction, sums the amounts and returns the total
         */
        return transactions.stream()
                .map(t -> t.transaction)
                .filter(t -> t.getType() == TransactionType.OUTGOING)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates the remaining budget given a starting budget and a list of transactions
     *
     * @param start        the <code>double</code> starting budget
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>double</code> sum of outgoing transactions
     */
    public static double getBudgetRemaining(double start, @NonNull List<TransactionWithCategory> transactions)
    {
        /*
        Takes the list of TransactionWithCategory objects & extracts the Transaction.
        Filters to only use outgoing transactions
        Adds the negated amount for outgoing transactions and adds the positive amounts for incoming ones
        Returns the sum of these amounts
         */
        double change = transactions.stream()
                .map(t -> t.transaction)
                .mapToDouble(t -> t.getType() == TransactionType.OUTGOING ? -t.getAmount() : t.getAmount())
                .sum();

        // Return the start + the newly-calculated amount
        // (Can result in negative numbers)
        return start + change;
    }


    /**
     * Gets the sum of outgoing amounts from a list of <code>TransactionWithCategory</code> objects,
     * and aggregates them into a map of <code>Category</code> and <code>Double</code> objects
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>Map</code> of <code>Category</code> and <code>Double</code> objects with aggregated spending
     */
    @NonNull
    public static Map<Category, Double> getCategoryTotals(@NonNull List<TransactionWithCategory> transactions)
    {
        // todo convert to stream
        return transactions.stream()
                .filter(t -> t.transaction.getType() == TransactionType.OUTGOING)   // Only consider outgoing values
                .collect(Collectors.groupingBy(t -> t.category,                     // The "key"
                        Collectors.summingDouble(t -> t.transaction.getAmount())    // The "amount", as the sum of the amounts
                ));
    }
}
