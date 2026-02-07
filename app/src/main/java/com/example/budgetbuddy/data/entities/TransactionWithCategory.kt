package com.example.budgetbuddy.data.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data Transfer Object comprised of a Transaction and the connected Category.
 * Generated using a SQL join on the Room database
 */
data class TransactionWithCategory(
    @Embedded
    val transaction: Transaction,       // "Owns" the DTO

    @Relation(
        parentColumn = "categoryID",
        entityColumn = "id"
    )
    val category: Category              // SQL Join on category id field
)
