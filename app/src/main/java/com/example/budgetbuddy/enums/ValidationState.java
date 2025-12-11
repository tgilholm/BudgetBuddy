package com.example.budgetbuddy.enums;

// Defines "error codes" returned by validation methods
// 0 is a success
public enum ValidationState
{
    NONE,               // Success
    INVALID_AMOUNT,     // <= 0 or non-numeric
    INVALID_DATE,       // Unparseable date/time
    NO_CATEGORY,        // No chip selected
}
