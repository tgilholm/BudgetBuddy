package com.example.budgetbuddy.utility;

/*
 Input validation methods (such as those used in getAmount()) are delegated here
 This helps to ensure the single-responsibility principle is adhered to
 */

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.Contract;
import com.example.budgetbuddy.enums.RepeatDuration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class InputValidator
{
    /**
     * Validates that a string is not empty and conforms to the format set out by the regex. <br><br>
     * Regex breakdown: <br>
     * <pre>
     * [0-9]+          Matches if the string contains one-or-more numbers- i.e. 50<br>
     * [.]             Matches if the string contains a decimal point<br>
     * [0-9]{1, 2}        Matches if numeric and at most 2 d.p.<br>
     * ([.][0-9]{1,2})?  Matches if numeric after the d.p (optional) <br>
     * Complete Regex: [0-9]+([.][0-9]{2})?
     * </pre>
     *
     * @param input a numeric string, e.g. "12.34"
     * @return true, if <code>input</code> is of the correct format, false otherwise
     */
    public static boolean validateCurrencyInput(@NonNull String input)
    {
        // Checks for empty strings
        if (input.isEmpty())
        {
            return false;
        }

        // Checks the input against the regex
        Log.v("InputValidator", "Input String: " + input + ", matches regex: " + input.matches("[0-9]+([.][0-9]{1,2})?"));
        return input.matches("[0-9]+([.][0-9]{1,2})?");
    }

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

    /**
     * Converts an <code>input</code> into a <code>RepeatDuration</code>
     * @param input a name in the enum
     * @return a <code>RepeatDuration</code> type
     */
    @Nullable
    @Contract(pure = true)
    public static RepeatDuration selectRepeatDuration(@NonNull String input)
    {
        switch (input)
        {
            case "Never":
                return RepeatDuration.NEVER;
            case "Day":
                return RepeatDuration.DAILY;
            case "Week":
                return RepeatDuration.WEEKLY;
            case "Month":
                return RepeatDuration.MONTHLY;
            case "Year":
                return RepeatDuration.YEARLY;
            default:
                return null;
        }
    }

}