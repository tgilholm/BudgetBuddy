package com.example.budgetbuddy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetbuddy.domain.entities.Category
import com.example.budgetbuddy.domain.entities.Transaction


@Database(
    entities = [Transaction::class,
        Category::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase()
{
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}