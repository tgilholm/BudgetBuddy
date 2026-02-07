package com.example.budgetbuddy.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetbuddy.data.entities.Transaction
import com.example.budgetbuddy.data.entities.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

interface TransactionDao
{
    /**
     * Returns flow list of all Transactions, ordered by dateTime by default.
     */
    @Query("SELECT * FROM 'transaction' ORDER BY 'datetime'")
    fun getAll(): Flow<List<Transaction>>

    /**
     * Performs SQL Join on Category/Transaction, returns flow list of
     * TransactionWithCategory objects.
     *
     */
    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction` ORDER BY 'datetime'")
    fun getAllWithCategories(): Flow<List<TransactionWithCategory>>

    /**
     * Inserts any number of transactions to the database
     */
    @Insert
    suspend fun insert(vararg transactions: Transaction)

    /**
     * Deletes the specified transaction from the database
     */
    @Delete
    suspend fun delete(transaction: Transaction)
}