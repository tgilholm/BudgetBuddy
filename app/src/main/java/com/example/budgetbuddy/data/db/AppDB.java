package com.example.budgetbuddy.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.utility.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Extends <code>RoomDatabase</code>. Defines <code>Transaction</code> and <code>Category</code> entities
 * Holds a static instance of the database. Provides methods to access the DAOs
 */
@Database(entities = {
        Transaction.class,
        Category.class
}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase
{
    // Abstract access methods for the DAOs
    public abstract TransactionDAO transactionDAO();

    public abstract CategoryDAO categoryDAO();

    // Defined as static to force a single instance, volatile to ensure threads receive updates
    private static volatile AppDB DB_INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);    // Used for long DB queries

    /**
     * Returns a singleton instance of the database. Double-checked locking ensures there is only one instance.
     * If there is no instance, a new one is created.
     *
     * @param context The application context
     * @return An instance of the database
     */
    public static AppDB getDBInstance(final Context context)
    {
        // First check
        if (DB_INSTANCE == null)
        {
            synchronized (AppDB.class)
            {    // Sync and check again
                if (DB_INSTANCE == null)
                {
                    Converters converter = new Converters();        // Get the type converter

                    // Build a new database
                    DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class, "budgetbuddy_DB"  // Name of the DB
                    ).addTypeConverter(converter).build();
                }
            }
        }
        return DB_INSTANCE;
    }
}
