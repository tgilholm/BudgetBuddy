package com.example.budgetbuddy.data.impl

import com.example.budgetbuddy.data.TransactionRepository
import com.example.budgetbuddy.data.db.TransactionDao
import com.example.budgetbuddy.domain.entities.Transaction
import javax.inject.Inject

/**
 * Implements the Transaction Repository interface. Dao is injected
 * at runtime. Implementation is separate from definition. Users remain
 * oblivious to Room/DAOs, etc.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository // Implements TransactionRepo methods
{
    /*
    Methods here simply abstract the injected DAO. Suspended methods
    execute in a background thread for potentially slow operations. Methods
    returning Flow lists do not need to be suspended as Room handles background
    threading for these
     */

    /**
     * Returns a Flow list of all transactions
     */
    override fun getAllTransactions() = dao.getAll()

    /**
     * Returns a Flow list of all transactions with their corresponding category
     */
    override fun getAllWithCategory() = dao.getAllWithCategories()

    /**
     * Inserts a new transaction in a background thread
     */
    override suspend fun insertTransaction(vararg transactions: Transaction)
    {
        /*
        Not a pointer! (thank god)
        * *transactions- this is the "spread" operator. It is used to pass the
        * varargs to the dao without having to individually iterate through them,
        * as the insert() method takes a vararg parameter
         */
        dao.insert(*transactions)
    }

    /**
     * Deletes an existing transaction in a background thread
     */
    override suspend fun deleteTransaction(transaction: Transaction)
    {
        dao.delete(transaction)
    }
}