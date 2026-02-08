package com.example.budgetbuddy.domain


/**
 * Sealed class providing a functional result wrapper.
 * E - an error type
 * V - a value type
 *
 * On success, return the value type and no error.
 * On failure, return the error type and no value.
 */
sealed class Result<out E, out V>
{
    data class Success<out V>(val value: V) : Result<Nothing, V>()

    data class Failure<out E>(val error: E) : Result<E, Nothing>()
}

