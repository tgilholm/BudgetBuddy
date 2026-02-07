package com.example.budgetbuddy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetbuddy.data.entities.Category
import com.example.budgetbuddy.data.entities.Transaction
import com.example.budgetbuddy.utility.Converters


@Database(
    entities = [Transaction::class,
        Category::class],
    version = 1,
    exportSchema = true
    )
@TypeConverters({Converters::class.java}) // FIXME: convert converters to kotlin
abstract class AppDB : RoomDatabase()
{
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}