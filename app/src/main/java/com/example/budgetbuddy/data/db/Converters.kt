package com.example.budgetbuddy.data.db

import androidx.room.TypeConverter
import java.util.Calendar

/**
 * Converts complex data structures into primitives & vice versa for storing
 * in the Room database. Converter methods must be annotated @TypeConverter
 */
class Converters
{
    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long
    {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun fromLong (value: Long): Calendar
    {
        // returns a new calendar object, sets the time to the provided value
        return value.let {
            Calendar.getInstance().apply { timeInMillis = it }
        }
    }
}