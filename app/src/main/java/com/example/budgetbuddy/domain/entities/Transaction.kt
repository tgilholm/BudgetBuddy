package com.example.budgetbuddy.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.enums.RepeatDuration
import com.example.budgetbuddy.enums.TransactionType
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