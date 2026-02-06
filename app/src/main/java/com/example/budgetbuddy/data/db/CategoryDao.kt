package com.example.budgetbuddy.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetbuddy.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao
{
    /**
     * Inserts any number of Category objects into the database.
     */
    @Insert
    fun insertAll(vararg categories: Category)

    /**
     * Deletes a Category object from the database
     */
    @Delete
    fun delete(category: Category)

    /**
     * Returns all Category objects stored in the database as a Flow list.
     * Ordered by name in ascending order by default
     */
    @Query("SELECT * FROM 'category' ORDER BY 'name'")
    fun getAll(): Flow<List<Category>>

    /**
     * Updates a category located by ID. Existing category data is replaced
     * with data extracted from provided Category object.
     */
    @Update
    fun update(category: Category)
}