package com.example.budgetbuddy.utility;

import androidx.core.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;

import org.jetbrains.annotations.Contract;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
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
     * Helper method.
     * Gets the sum of outgoing amounts from a list of <code>TransactionWithCategory</code> objects,
     * and aggregates them into a map of <code>Category</code> and <code>Double</code> objects
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>Map</code> of <code>Category</code> and <code>Double</code> objects with aggregated spending
     */
    @NonNull
    static Map<Category, Double> getCategoryTotals(@NonNull List<TransactionWithCategory> transactions)
    {
        return transactions.stream()
                .filter(t -> t.transaction.getType() == TransactionType.OUTGOING)   // Only consider outgoing values
                .collect(Collectors.groupingBy(t -> t.category,                     // Group by the key
                        Collectors.summingDouble(t -> t.transaction.getAmount())    // Sum up the amounts, return total
                ));
    }


    /**
     * Helper method.
     * Gets the list of category totals, sorts them and returns a list of map entries
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a sorted list of <code>Map.Entry</code> objects (<code>Category, Double</code>)
     */
    @NonNull
    static List<Map.Entry<Category, Double>> getSortedCategoryTotals(@NonNull List<TransactionWithCategory> transactions)
    {
        return getCategoryTotals(transactions).entrySet()                               // Get the set of Map entries
                .stream()                                                               // Iterate through the set
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))          // Sort "backwards", largest first, smallest last
                .collect(Collectors.toList());                                          // Collect each pair into a Map entry and add to the list
    }


    /**
     * Gets the list of transactions, passes them to be sorted into map entries, then groups them into named categories BELOW <code>topN</code>
     * and an "other" category for the last entry.
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @return a <code>Pair</code>, where the first item is a a sorted list of <code>Map.Entry</code> objects (<code>Category, Double</code>)
     * (This represents the "named categories"), and the second item is a <code>Map.Entry</code> object (<code>String, Double</code>) containing
     * the "other" data
     */
    @NonNull
    public static Pair<List<Map.Entry<Category, Double>>, Map.Entry<String, Double>> getTopNCategoryTotals(@NonNull List<TransactionWithCategory> transactions, int topN)
    {
        // Empty check
        if (transactions.isEmpty())
        {
            // Break early
            return new Pair<>(new ArrayList<>(), new AbstractMap.SimpleEntry<>("Other", 0.0));
        }

        // Get the sorted totals
        List<Map.Entry<Category, Double>> sortedCategoryTotals = TransactionUtils.getSortedCategoryTotals(transactions);
        int limit = Math.min(sortedCategoryTotals.size(), topN); // Math.min ensures no out-of-bounds

        // From the start of the list to the limit
        List<Map.Entry<Category, Double>> namedCategories = new ArrayList<>(sortedCategoryTotals.subList(0, limit));

        // Get the other total
        double otherTotal = 0.0;
        if (sortedCategoryTotals.size() > topN)
        {
            // Gets the sum of all categories beyond topN
            otherTotal = sortedCategoryTotals.subList(topN, sortedCategoryTotals.size())
                    .stream()
                    .mapToDouble(Map.Entry::getValue)
                    .sum();
        }

        Map.Entry<String, Double> otherEntry = new AbstractMap.SimpleEntry<>("Other", otherTotal);

        return new Pair<>(namedCategories, otherEntry);
    }
}
