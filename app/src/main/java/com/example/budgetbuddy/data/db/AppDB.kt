package com.example.budgetbuddy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.budgetbuddy.entities.Category
import com.example.budgetbuddy.utility.Converters
import kotlin.reflect.KClass


@Database(
    entities = [com.example.budgetbuddy.entities.Transaction::class,
        Category::class],
    version = 1,
    exportSchema = true
    )
@TypeConverters({Converters::class.java})
internal abstract class AppDB : RoomDatabase()
{
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object
    {
        @Volatile
        private var INSTANCE: AppDB? = null
        fun getDatabase(context: Context): AppDB
        {
            // elvis operator ?:
            // return the first operand if true, otherwise evaluate & return the second

            /*
            Either returns the instance if one already exists, or synchronises and returns
            a new instance of the database
             */
            return INSTANCE ?: synchronized(this)
            {
                val converter = Converters()

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "budgetbuddy_DB"
                ).addTypeConverter(converter).build()
                INSTANCE = instance
                instance
            }
        }
    }
}