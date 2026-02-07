package com.example.budgetbuddy.data

import com.example.budgetbuddy.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository
{
    /**
     * Returns a Flow list of all categories
     */
    fun getAllCategories() : Flow<List<Category>>

    /**
     * Inserts a new Category in a background thread
     */
    suspend fun insertCategory(category: Category)
}