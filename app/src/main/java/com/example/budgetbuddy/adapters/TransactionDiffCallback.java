package com.example.budgetbuddy.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.budgetbuddy.domain.entities.Transaction;
import com.example.budgetbuddy.domain.entities.TransactionWithCategory;

import java.util.List;
import java.util.Objects;


/**
 * Extends <code>DiffUtils.Callback</code> to calculate changes between transaction lists
 */
public class TransactionDiffCallback extends DiffUtil.Callback
{
    private final List<TransactionWithCategory> oldTransactionList;
    private final List<TransactionWithCategory> newTransactionList;

    /**
     * Takes an old and new list of <code>TransactionWithCategory</code> objects
     *
     * @param oldTransactionList the old list
     * @param newTransactionList the new list
     */
    public TransactionDiffCallback(List<TransactionWithCategory> oldTransactionList, List<TransactionWithCategory> newTransactionList)
    {
        this.oldTransactionList = oldTransactionList;
        this.newTransactionList = newTransactionList;
    }

    /**
     * Get the size of the old list
     *
     * @return <code>int</code> size value
     */
    @Override
    public int getOldListSize()
    {
        return oldTransactionList.size();
    }

    /**
     * Get the size of the new list
     *
     * @return <code>int</code> size value
     */
    @Override
    public int getNewListSize()
    {
        return newTransactionList.size();
    }

    /**
     * Compares two items in a list by their ID
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return true if the IDs are the same, false if not
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
    {
        return Objects.equals(oldTransactionList.get(oldItemPosition).transaction.getId(), newTransactionList.get(newItemPosition).transaction.getId());
    }

    /**
     * Compares two Transactions by their date and time
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list which replaces the
     *                        oldItem
     * @return true if the contents are the same, false otherwise
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
    {
        Transaction oldTransaction = oldTransactionList.get(oldItemPosition).transaction;
        Transaction newTransaction = newTransactionList.get(newItemPosition).transaction;
        return Objects.equals(oldTransaction.getDateTime(), newTransaction.getDateTime());
    }
}
