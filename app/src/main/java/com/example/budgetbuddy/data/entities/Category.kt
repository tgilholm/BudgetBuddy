package com.example.budgetbuddy.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)    // avoiding static id generation
    val id: Long = 0,                   // init to 0 to force id generation

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "colour")
    val colorID: Int,                   // id of the colour in res
)
