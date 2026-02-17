package com.example.budgetbuddy.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.enums.RepeatDuration
import com.example.budgetbuddy.enums.TransactionType
import com.example.budgetbuddy.domain.Result
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Data class for Transaction objects acting as an Entity in the room database.
 * Auto-generates primary keys.
 */
@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)    // Let Room handle ID generation
    val id: Long = 0,                   // kotlin does not have primitive long

    @ColumnInfo(name = "amount")
    val amount: Double,                 // nor double

    @ColumnInfo(name = "type")
    val type: TransactionType,

    @ColumnInfo(name = "datetime")
    val dateTime: Calendar,

    @ColumnInfo(name = "categoryID")
    val categoryID: Long,               // Foreign key to Category object

    @ColumnInfo(name = "repeat")
    val repeatDuration: RepeatDuration
)
{
    /**
     * Overrides toString method from base data class. Outputs transaction data
     * for logging
     */
    override fun toString(): String
    {
        return String.format(
            Locale.getDefault(),
            "ID: %s, Amount: %.2f, Type: %s, Date: %s",
            id, amount, type, dateTimeString
        )
    }

    // Define the formatting for the dateTime variable
    val dateTimeString: String
        get() = String.format(      // "custom" get setter
            Locale.getDefault(),
            "%02d:%02d %02d/%02d/%d",
            dateTime.get(Calendar.HOUR_OF_DAY),
            dateTime.get(Calendar.MINUTE),
            dateTime.get(Calendar.DAY_OF_MONTH),
            dateTime.get(Calendar.MONTH) + 1,
            dateTime.get(Calendar.YEAR)
        )
}

sealed interface TransactionError
{
    data object Empty : TransactionError
    data object BadFormat : TransactionError
    data object InvalidAmount : TransactionError
    data object NoCategory : TransactionError
    data object InvalidDateTime : TransactionError
}

enum class TransactionType
{
    INCOMING,
    OUTGOING
}

enum class RepeatDuration
{
    NEVER,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}


/**
 * Transaction amounts are taken in as strings and parsed to doubles
 * in this value class. Returns TransactionError if parsing fails
 *
 *
 *               Regex:
 *               [0-9]+               Matches if the string contains one-or-more numbers
 *               [.]                  Matches if the string contains a decimal point
 *               [0-9]{1, 2}          Matches if numeric and at most 2 d.p.
 *               ([.][0-9]{1,2})?     Matches if numeric after the d.p (optional)
 *               Complete Regex: [0-9]+([.][0-9]{2})?
 *
 */
@JvmInline
value class TransactionAmount(val value: Double)
{
    companion object
    {
        fun create(raw: String): Result<TransactionError, TransactionAmount>
        {
            // Pre-trim and compile regex
            val trimmed = raw.trim()
            val regex = Regex("[0-9]+(\\.[0-9]{1,2})?")

            return when
            {
                // Empty strings
                trimmed.isBlank() ->
                    Result.Failure(TransactionError.Empty)

                // Doesn't match pattern
                !trimmed.matches(regex)
                    -> Result.Failure(TransactionError.BadFormat)

                // Parse to double & catch unexpected errors
                else ->
                {
                    /*
                    Returns the TransactionAmount(Double) if non-null, or
                    TransactionError.BadFormat if it is null.
                    ?.let performs a safe call to TransactionAmount([the double])
                    "Safe" meaning only if toDoubleOrNull returned non-null

                    if the left-hand value ^ is null, ?: returns the right-hand value
                     */
                    trimmed.toDoubleOrNull()
                        ?.let { Result.Success(TransactionAmount(it)) }
                        ?: Result.Failure(TransactionError.BadFormat)
                }
            }
        }
    }
}

/**
 * Value class for category foreign keys. Prevents transactions being created
 * without a category.
 */
@JvmInline
value class TransactionCategory(val value: Long /* Foreign key */)
{
    companion object
    {
        fun create(raw: Long): Result<TransactionError, TransactionCategory>
        {
            return when
            {
                raw < 0 -> Result.Failure(TransactionError.NoCategory)
                else -> Result.Success(TransactionCategory(raw))
            }
        }
    }
}


/**
 * Value class enforcing correct date-time format
 */
@JvmInline
value class TransactionDateTime(val calendar: Calendar)
{
    companion object
    {
        fun create(rawDate: String, rawTime: String)
        {
            val dateFormat = SimpleDateFormat("dd/MM/YYYY HH:mm", Locale.getDefault())

            try
            {
                val parsedDateTime = dateFormat.parse(rawDate + " " )
            }
        }

    }
}


