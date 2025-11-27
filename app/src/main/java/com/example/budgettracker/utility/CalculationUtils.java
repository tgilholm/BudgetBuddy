package com.example.budgettracker.utility;

// Handles calculation logic re-used throughout the application
public class CalculationUtils
{
    private CalculationUtils() {}   // Declare constructor as private to prevent instantiation

    // Re-usable method for calculating percentage
    public static double calculatePercentage(double value, double total)
    {
        return (value / total) * 100;
    }

}
