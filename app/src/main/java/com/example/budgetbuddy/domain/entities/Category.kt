package com.example.budgetbuddy.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.domain.Result

/**
 * Data class for Category objects acting as an Entity in the room database.
 * Auto-generates primary keys.
 */
@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)    // avoiding static id generation
    val id: Long = 0,                   // init to 0 to force id generation

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "colour")
    val colorID: Int,                   // id of the colour in res
)


/**
 * Sealed interface specifying error types for category validation failures
 */
sealed interface CategoryError
{
    object AlreadyExists : CategoryError
    object EmptyName : CategoryError
    object TooLong : CategoryError
    object NotSelected : CategoryError
}

/*
 * Value class annotated JvmInline, effectively ensuring certain validation conditions
 * are met without lengthy, duplicate code in the UI and Domain layers.
 *
 * Acts similarly to simple wrapper classes, but enforces rules on data
 * and compiles down to the base type with very little overhead. In effect,
 * the use of JvmInline value classes protects domain-level code from invalid code.
 * This is a key tenet of DDD - Domain-driven-design code, wherein it should
 * be effectively impossible to construct impossible data.
 */


/**
 * Value class preventing invalid category names. On object creation, returns
 * either a ValidationResult.Failure with the error type or a ValidationResult.Success
 * with the attached CategoryName object
 */
@JvmInline
value class CategoryName private constructor(val value: String)
{
    companion object
    {
        // Return a validation result with either an error or the name object
        fun create(raw: String): Result<CategoryError, CategoryName>
        {
            val trimmed = raw.trim()
            return when
            {
                // Empty or just whitespace
                trimmed.isBlank() -> Result.Failure(CategoryError.EmptyName)
                trimmed.length > 32 -> Result.Failure(CategoryError.TooLong)

                // Return the trimmed result if successful
                else -> Result.Success(CategoryName(trimmed))
            }
        }

    }
}

/**
 * Value class preventing non-selected category colours to be passed to
 * domain level services. Services can then check if the colour is "correct".
 */
@JvmInline
value class CategoryColor(val value: Int)
{
    companion object
    {
        fun create(raw: Int): Result<CategoryError, CategoryColor>
        {
            return when
            {
                // No category selected
                raw < 0 -> Result.Failure(CategoryError.NotSelected)
                else -> Result.Success(CategoryColor(raw))
            }
        }
    }
}