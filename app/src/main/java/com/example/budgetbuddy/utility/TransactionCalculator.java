package com.example.budgetbuddy.utility;

import com.example.budgetbuddy.entities.TransactionWithCategory;

import java.util.List;

// Stateless calculator for transactions
// Does not rely on a viewmodel or fragment for state
// Handles calculations such as category totals
public final class TransactionCalculator
{
    public static List<TransactionWithCategory> sortTransactions(List<TransactionWithCategory> transactions)
    {
        transactions.sort((o1, o2) ->
                o2.transaction.getDateTime().compareTo(o1.transaction.getDateTime()));
        return transactions;
    }
}
