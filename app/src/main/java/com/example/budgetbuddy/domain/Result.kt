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

/**
 * Type-safe guarding extension method. If the operation was successful,
 * the result value is returned. If unsuccessful, the method "bails early"
 * and returns Nothing.
 */
inline fun <E, V> Result<E, V>.getOrReturn(
    onFailure: (Result.Failure<E>) -> Nothing
): V
{
    return when (this)
    {
        // If successful, return the value
        is Result.Success -> this.value

        // If unsuccessful, return early
        is Result.Failure -> onFailure(this)
    }
}