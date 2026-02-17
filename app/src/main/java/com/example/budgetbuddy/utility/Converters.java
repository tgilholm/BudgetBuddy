package com.example.budgetbuddy.utility;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

/**
 * Utility class, converts complex values to primitives usable in RoomDB tables
 */
public class Converters
{

    /**
     * Extracts the hour and minute from a <code>Calendar</code> and expresses it in a formatted string
     * @param calendar a <code>Calendar</code> object
     * @return a formatted <code>String</code>
     */
    @NonNull
    public static String calendarToHourMinute(@NonNull Calendar calendar) {
        return String.format(Locale.getDefault(), "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    /**
     * Extracts the day, month and year from a <code>Calendar</code> and expresses it in a formatted string
     * @param calendar a <code>Calendar</code> object
     * @return a formatted <code>String</code>
     */
    @NonNull
    public static String calendarToDayMonthYear(@NonNull Calendar calendar) {
        return String.format(Locale.getDefault(), "%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

    }

    /**
     * Expresses a double as a currency value (in the format £00.00)
     * @param value a <code>double</code> value
     * @return a formatted <code>String</code>
     */
    @NonNull
    public static String doubleToCurrencyString(double value) {
        return String.format(Locale.getDefault(), "£%.2f", value);
    }
}
