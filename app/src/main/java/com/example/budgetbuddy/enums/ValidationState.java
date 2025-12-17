package com.example.budgetbuddy.enums;

/**
 * Enumeration of possible <code>ValidationState</code> values for use as "error codes".
 */
public enum ValidationState
{
    NONE,               // Success
    INVALID_AMOUNT,     // <= 0 or non-numeric
    INVALID_DATE,       // Unparseable date/time
    NO_CATEGORY,        // No chip selected
    EMPTY               // No text selected
}
