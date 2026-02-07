package com.example.budgetbuddy.data

import com.example.budgetbuddy.entities.Transaction
import com.example.budgetbuddy.entities.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

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
    suspend fun insertTransaction(transaction: Transaction)

    /**
     * Deletes an existing transaction in a background thread
     */
    suspend fun deleteTransaction(transaction: Transaction)


}