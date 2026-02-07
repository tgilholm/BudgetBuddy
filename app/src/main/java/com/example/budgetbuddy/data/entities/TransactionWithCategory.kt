package com.example.budgetbuddy.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithCategory(
    @Embedded
    val transaction: Transaction,       // "Owns" the DTO

    @Relation(
        parentColumn = "categoryID",
        entityColumn = "id"
    )
    val category: Category              // SQL Join on category id field
)
