package com.example.budgetbuddy.domain.services

import com.example.budgetbuddy.data.TransactionRepository
import com.example.budgetbuddy.domain.entities.Transaction
import com.example.budgetbuddy.domain.ValidationResult
import com.example.budgetbuddy.enums.RepeatDuration
import com.example.budgetbuddy.enums.TransactionType
import com.example.budgetbuddy.utility.InputValidator
import javax.inject.Inject

/**
 * Domain-level class handling business logic operations.
 */
class ValidationService @Inject constructor(
    private val transactionRepo: TransactionRepository
)
{
    /**
     * Validates a transaction meets the expected conditions,
     * and adds it via the repository if so. If not, returns a
     * ValidationResult error back to the caller.
     */
    suspend fun validateAndAddTransaction(
        amountString: String,   // All fields are marked NotNull implicitly (no ?)
        categoryID: Long,
        date: String,
        time: String,
        type: TransactionType,
        repeat: RepeatDuration
    ): ValidationResult    // Returns a validation result
    {
        // If parsing the amount results in null, return an error
        val amount = amountString.toDoubleOrNull()
            ?: return ValidationResult.Error("Invalid amount format")

        if (amount <= 0)
        {
            return ValidationResult.Error("Amount must be greater than 0")
        }

        // Return error if -1 (no category selected)
        if (categoryID < 0) // Non-existent ids are (theoretically) impossible
        {
            return ValidationResult.Error("No category selected")
        }

        // Parse date & time
        val calendar = InputValidator.parseDateTime(date, time)
            ?: return ValidationResult.Error("Invalid date or time")


        // If validation succeeded, create a new Transaction
        val transaction = Transaction(
            amount = amount,
            type = type,
            dateTime = calendar,
            categoryID = categoryID,
            repeatDuration = repeat
        )

        // Insert via repo
        transactionRepo.insertTransaction(transaction)

        return ValidationResult.Success
    }
}