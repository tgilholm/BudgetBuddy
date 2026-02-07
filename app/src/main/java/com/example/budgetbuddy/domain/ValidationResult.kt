package com.example.budgetbuddy.domain

/**
 * Class "handed back" to the caller as a result of validating a given operation
 * Contains either a success condition, or a provided error message. Allows
 * delegating validation to domain-level classes.
 */
sealed class ValidationResult
{
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}