package com.example.budgetbuddy.utility;

import junit.framework.TestCase;

import java.util.Calendar;

public class InputValidatorTest extends TestCase
{
    private Calendar calendar;

    @Override
    protected void setUp()
    {
        calendar = Calendar.getInstance();
    }


    public void testValidateCurrencyInput()
    {
        // Empty string, erroneous string, correctly-formatted string
        String empty = "";
        String erroneous = "abc";
        String correct = "50.10";

        assertFalse(InputValidator.validateCurrencyInput(empty));
        assertFalse(InputValidator.validateCurrencyInput(erroneous));
        assertTrue(InputValidator.validateCurrencyInput(correct));
    }

    public void testParseDateTime()
    {
        // Strings to parse
        String date = "24/10/2024";
        String time = "12:50";

        // Set up calendar
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        calendar.set(Calendar.MONTH, 9);        // 0-indexed months
        calendar.set(Calendar.YEAR, 2024);

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 50);

        // Parse the strings
        Calendar parsedCalendar = InputValidator.parseDateTime(date, time);

        // Assert the resulting calendar is not null
        assertNotNull(parsedCalendar);

        // Assert each of the fields (impractical to compare calendar objects exactly)
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), parsedCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(calendar.get(Calendar.MONTH), parsedCalendar.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.YEAR), parsedCalendar.get(Calendar.YEAR));

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), parsedCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(calendar.get(Calendar.MINUTE), parsedCalendar.get(Calendar.MINUTE));



    }
}