package com.example.budgetbuddy.domain

/**
 * Type-Safe, String-Free sealed interface for error messages
 * designed to be handled at UI level and emitted from domain-level methods
 */
sealed interface DomainResult
{
    /**
     * Defines various "success" conditions to be responded to by UX
     */
    sealed interface Success : DomainResult
    {
        object CategoryCreated : Success
        object TransactionCreated : Success
        object AppResetComplete : Success
    }

    /**
     * Defines various "failure" conditions to be responded to by UX
     */
    sealed interface Failure : DomainResult
    {
        object Empty : Failure
        object TooLong : Failure
        object NotSelected : Failure
    }
}
