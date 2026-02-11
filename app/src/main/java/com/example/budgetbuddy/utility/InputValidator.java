package com.example.budgetbuddy.utility;

/*
 Input validation methods (such as those used in getAmount()) are delegated here
 This helps to ensure the single-responsibility principle is adhered to
 */

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Utility class to handle input validation
 */
public final class InputValidator
{
    // Final class, no instantiation
    private InputValidator() {}


    /**
     * Concatenates two strings, validates whether the resulting string matches the correct format and returns a <code>Calendar</code>

     * @param date in the format "dd/mm/yyyy"
     * @param time in the format "hh:mm"
     * @return the parsed <code>Calendar</code> instance if succeeded, else null
     */
    @Nullable
    public static Calendar parseDateTime(String date, String time)
    {
        // Create a SimpleDateFormat to parse the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try
        {
            // Parse the dateTime into a DateObject
            Date parsedDateTime = dateFormat.parse(date + " " + time);

            // If parsedDateTime is not null, pass the Date to calendar
            if (parsedDateTime != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDateTime);
                return calendar;
            } else
            {
                return null;
            }
        } catch (ParseException e)
        {
            Log.e("AddFragment", "Error parsing date & time: " + e.getMessage());
            return null;
        }
    }
}