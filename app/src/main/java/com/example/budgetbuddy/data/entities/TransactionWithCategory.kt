package com.example.budgetbuddy.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * Joins together transaction and category, linked by the category ID.
 * Room performs a SQL JOIN on the Transaction and Category entities.
 * This allows accessing the category data in a Transaction context.
 */
public class TransactionWithCategory
{
    @Embedded
    public Transaction transaction;

    @Relation(parentColumn = "category", entityColumn = "categoryID")
    public Category category;


    public TransactionWithCategory(Transaction transaction, Category category)
    {
        this.transaction = transaction;
        this.category = category;
    }
}
