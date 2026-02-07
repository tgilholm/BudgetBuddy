@file:Suppress("unused")

package com.example.budgetbuddy.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Object (create & initialise at once) used to generate Singleton instances
 * of database classes.
 *
 * Annotated @Module to indicate to Hilt that this object generates objects
 * and to refer here when attempting to reference dependencies on them. Hilt
 * generates a factory class that calls provideDatabase when the DB is needed,
 * or provideTransactionDao when a DAO is needed, etc.
 *
 * This is an "object" - a class initialised as soon as it is created,
 * so as to allow Hilt to simply access these methods without having to instantiate the
 * class first.
 */
@Module
@InstallIn(SingletonComponent::class)
object DBModule
{
    // "by" keyword- get the sa
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    /**
     * Returns the database instance for use in the Hilt dependency tree.
     * Hilt generates a singleton database instance here. Only the code
     * needed to create a new Database is required- Hilt will handle
     * whether or not to create or return this instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDB
    {
        // Create the database instance
        return Room.databaseBuilder(
            context,
            AppDB::class.java,
            "budgetbuddy_DB"
        ).build()
    }

    /**
     * Returns or creates a singleton instance of the transaction DAO.
     * Consumes the AppDB instance provided by provideDatabase
     */
    @Singleton
    @Provides
    fun provideTransactionDao(db: AppDB): TransactionDao
    {
        return db.transactionDao()
    }

    /**
     * Returns or creates a singleton instance of the category DAO.
     * Consumes the AppDB instance provided by provideDatabase
     */
    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDB): CategoryDao
    {
        return db.categoryDao()
    }

    /**
     * Returns or creates a singleton instance of the Preferences DataStore
     */
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences>
    {
        return context.dataStore
    }
}