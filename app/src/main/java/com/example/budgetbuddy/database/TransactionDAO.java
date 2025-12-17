package com.example.budgetbuddy.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.entities.TransactionWithCategory;

import java.util.List;

/**
 * Data Access Object for the Transaction Entity. Defines CRUD methods
 */
@Dao
public interface TransactionDAO {

    /**
     * Gets all the transactions
     * @return a <code>LiveData</code> object containing a list of <code>Transaction</code> objects
     */
    @Query("SELECT * FROM 'transaction'")
    LiveData<List<Transaction>> getAll();

    /**
     * Gets the list of transactions with the category they belong to
     * @return a <code>LiveData</code> object containing a list of <code>TransactionWithCategory</code> objects
     */
    @androidx.room.Transaction
    @Query("SELECT * FROM 'transaction'")
    LiveData<List<TransactionWithCategory>> getTransactionCategory();

    /**
     * Inserts new <code>Transaction</code> objects into the table
     * @param transaction the <code>Transaction</code> object to insert
     */
    @Insert
    void insertTransaction(Transaction... transaction);

    /**
     * Deletes a <code>Transaction</code> object from the table
     * @param transaction the <code>Transaction</code> object to delete
     */
    @Delete
    void delete(Transaction transaction);

}

