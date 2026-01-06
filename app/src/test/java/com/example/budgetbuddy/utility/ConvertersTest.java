package com.example.budgetbuddy.utility;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 * Tests the methods in the Converters utility class using arrange-act-assert pattern
 */
public class ConvertersTest extends TestCase
{
    private Calendar calendar;


    @Override
    protected void setUp()
    {
        // Instantiate calendar instance
        calendar = Calendar.getInstance();
    }

    public void testCalendarToHourMinute()
    {
        // Arrange the test
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        String expected = "12:30";

        // Act - execute the test
        String actual = Converters.calendarToHourMinute(calendar);

        // Assert the two are equal
        assertEquals(expected, actual);
    }

    public void testCalendarToDayMonthYear()
    {
        // Arrange the test
        calendar.set(Calendar.YEAR, 2026);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        String expected = "12/07/2026";     // Months are 0-indexed

        // Act
        String actual = Converters.calendarToDayMonthYear(calendar);

        // Assert
        assertEquals(expected, actual);
    }

    public void testDoubleToCurrencyString()
    {
        double amount = 50;
        String expected = "£50.00";

        assertEquals(expected, Converters.doubleToCurrencyString(amount));
    }
}