package com.example.budgetbuddy.entities;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;

/**
 * Represents a transaction in the database with an ID, amount, type, date and time, category and repeat duration.
 * <code>nextID</code> is a static long variable used to generate unique IDs. It is shared by all <code>Transaction</code> objects
 * and is incremented each time a new object is created.
 */
@Entity(tableName = "transaction")
public final class Transaction
{
    private static long nextID;                     // Unique ID generator

    @PrimaryKey
    private long id;                                // Primary key

    @ColumnInfo(name = "amount")
    private final double amount;                    // Amount field

    @ColumnInfo(name = "type")
    private final TransactionType type;             // Type field

    @ColumnInfo(name = "datetime")
    private final Calendar dateTime;                // DateTime field

    @ColumnInfo(name = "category")
    private final long categoryID;                  // CategoryID foreign key

    @ColumnInfo(name = "repeat")
    private final RepeatDuration repeatDuration;    // RepeatDuration field

    /**
     * Constructs a new Transaction object with the given parameters. Sets <code>id</code> to <code>nextID</code> and
     * increments it
     *
     * @param amount         a <code>double</code> value for the amount
     * @param type           a <code>TransactionType</code> value for the type
     * @param dateTime       a <code>Calendar</code> object for the date and time
     * @param categoryID     a <code>long</code> value for the categoryID
     * @param repeatDuration a <code>RepeatDuration</code> value for the repeat duration
     */
    public Transaction(double amount, TransactionType type, Calendar dateTime, long categoryID, RepeatDuration repeatDuration)
    {
        this.id = nextID++; // Increment the ID counter by 1
        this.amount = amount;
        this.type = type;
        this.dateTime = dateTime;
        this.categoryID = categoryID;
        this.repeatDuration = repeatDuration;
    }

    /**
     * Gets the transaction ID
     *
     * @return the transaction ID
     */
    public long getId()
    {
        return id;
    }

    /**
     * Sets the transaction ID
     *
     * @param id the transaction ID
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Gets the transaction amount
     *
     * @return the transaction amount
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Gets the transaction type
     *
     * @return the transaction type
     */
    public TransactionType getType()
    {
        return type;
    }

    /**
     * Gets the date and time of the transaction
     *
     * @return a <code>Calendar</code> object containing the date and time
     */
    public Calendar getDateTime()
    {
        return dateTime;
    }

    /**
     * Gets the category ID of the transaction
     *
     * @return the category ID
     */
    public long getCategoryID()
    {
        return categoryID;
    }

    /**
     * Gets the repeat duration of the transaction
     *
     * @return the repeat duration
     */
    public RepeatDuration getRepeatDuration()
    {
        return repeatDuration;
    }

    /**
     * Returns a string representation of the transaction
     *
     * @return a <code>String</code> object combining all transaction data
     */
    @NonNull
    public String toString()
    {
        return String.format(Locale.getDefault(), "ID: %s, Amount: %.2f, Type: %s, Date: %s", id, amount, type, dateTime.toString());
    }

    /**
     * Compares two transactions by their <code>dateTime</code> in milliseconds
     *
     * @param o the reference object with which to compare.
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true; // Check if the two objects are exactly the same in memory
        if (o == null || getClass() != o.getClass())
            return false; // Check if the other object is not a Transaction or is null

        Transaction x = (Transaction) o; // Cast o to transaction- only executed if the last check succeeded

        // Compare the date and time in milliseconds
        return Double.compare(x.dateTime.getTimeInMillis(), dateTime.getTimeInMillis()) == 0;
    }

    /**
     * Generates a unique hashcode for this <code>Transaction</code> using all of its fields
     *
     * @return a <code>int</code> value representing the hashcode
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, amount, type, dateTime, categoryID, repeatDuration);
    }

    /**
     * Gets the Transaction date and time as a String in the format hh:mm dd/MM/yyyy
     *
     * @return a <code>String</code> object combining the date and time
     */
    @NonNull
    public String getDateTimeString()
    {
        return String.format(Locale.getDefault(), "%02d:%02d %02d/%02d/%d", dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), dateTime.get(Calendar.DAY_OF_MONTH), dateTime.get(Calendar.MONTH) + 1, dateTime.get(Calendar.YEAR));
    }


}
