package com.example.budgetbuddy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
 * Value class annotated JvmInline, effectively ensuring certain validation conditions
 * are met without lengthy, duplicate code in the UI and Domain layers.
 *
 * Acts similarly to simple wrapper classes, but enforces rules on data
 * and compiles down to the base type with very little overhead. In effect,
 * the use of JvmInline value classes protects domain-level code from invalid code.
 * This is a key tenet of DDD - Domain-driven-design code, wherein it should
 * be effectively impossible to construct impossible data.
 */
@JvmInline
value class CategoryName(val value: String)
{
    init
    {
        // New types must meet these conditions
        require(value.isNotBlank()) { "Category name cannot be blank" }
        require(value.length <= 32) { "Category name cannot exceed 32 characters" }
    }
}

/**
 * Value class preventing non-selected category colours to be passed to
 * domain level services. Services can then check if the colour is "correct".
 */
@JvmInline
value class CategoryColor(val value: Int)
{
    init
    {
        require(value != -1) { "Category colour must be selected" }
    }
}