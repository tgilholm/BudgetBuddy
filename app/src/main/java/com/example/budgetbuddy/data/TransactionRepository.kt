package com.example.budgetbuddy.data

import com.example.budgetbuddy.data.entities.Transaction
import com.example.budgetbuddy.data.entities.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract implementations must follow for accessing
 * the data layer and carrying out operations pertaining to Transaction data.
 *
 * The interface segregation principle is enforced here, favouring small,
 * focused interfaces over "fat" ones, or a "god repository".
 */
interface TransactionRepository
{
    /**
     * Returns a Flow list of all transactions
     */
    fun getAllTransactions() : Flow<List<Transaction>>

    /**
     * Returns a Flow list of all transactions with their corresponding category
     */
    fun getAllWithCategory() : Flow<List<TransactionWithCategory>>

    /**
     * Inserts a new transaction in a background thread
     */
    suspend fun insertTransaction(vararg transactions: Transaction)

    /**
     * Deletes an existing transaction in a background thread
     */
    suspend fun deleteTransaction(transaction: Transaction)

    /**
     * Deletes all transactions from the database
     */
    suspend fun deleteAll()
}