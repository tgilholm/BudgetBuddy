package com.example.budgetbuddy.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.budgetbuddy.data.db.javadb;
import com.example.budgetbuddy.data.db.CategoryDAO;
import com.example.budgetbuddy.data.db.TransactionDAO;
import com.example.budgetbuddy.data.entities.Category;
import com.example.budgetbuddy.data.entities.Transaction;
import com.example.budgetbuddy.data.entities.TransactionWithCategory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Abstracts access to <code>TransactionDAO</code> and <code>CategoryDAO</code>.
 * Follows a singleton pattern- only one instance of the repository exists at once time
 */
public class DataRepository
{

    // Hold instances of DAOs, the executorService and this instance
    private final TransactionDAO transactionDAO;
    private final CategoryDAO categoryDAO;
    private final ExecutorService executorService;
    private static volatile DataRepository INSTANCE;


    /**
     * Initialises the DAOs and executorService, connects to the DB
     *
     * @param application the application context
     */
    private DataRepository(Application application)
    {
        javadb javadb = javadb.getDBInstance(application); // Get an instance of the database

        // Initialise the DAOs and executorService
        transactionDAO = javadb.transactionDAO();
        categoryDAO = javadb.categoryDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Gets an instance of the <code>DataRepository</code>. Provides double-checked locking
     * to prevent unnecessary synchronisation and return repository instances.
     *
     * @param application the application context
     * @return an instance of the <code>DataRepository</code>
     */
    public static DataRepository getInstance(Application application)
    {
        if (INSTANCE == null)
        {
            synchronized (DataRepository.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new DataRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Gets the joined <code>Transaction</code> and <code>Category</code> tables
     *
     * @return a <code>LiveData</code> list of <code>TransactionWithCategory</code> objects
     */
    public LiveData<List<TransactionWithCategory>> getAllTransactions()
    {
        return transactionDAO.getTransactionCategory();
    }

    /**
     * Adds a new transaction
     *
     * @param transaction the <code>Transaction</code> object to add
     */
    public void insertTransaction(Transaction transaction)
    {
        // Run in a separate thread
        executorService.execute(() -> transactionDAO.insertTransaction(transaction));
    }

    /**
     * Removes a transaction
     *
     * @param transaction the <code>Transaction</code> object to remove
     */
    public void deleteTransaction(Transaction transaction)
    {
        executorService.execute(() -> transactionDAO.delete(transaction));
    }


    /**
     * Gets the list of categories
     * @return a <code>LiveData</code> list of <code>Category</code> objects
     */
    public LiveData<List<Category>> getAllCategories()
    {
        return categoryDAO.getAll();
    }

    /**
     * Adds a new category
     *
     * @param category the <code>Category</code> object to add
     */
    public void insertCategory(Category category)
    {
        executorService.execute(() -> categoryDAO.insertCategory(category));
    }
}

