package com.example.budgetbuddy.data

import com.example.budgetbuddy.domain.entities.Category
import kotlinx.coroutines.flow.Flow


/**
 * Interface defining the contract implementations must follow for accessing
 * the data layer and carrying out operations pertaining to Category data.
 *
 * The interface segregation principle is enforced here, favouring small,
 * focused interfaces over "fat" ones, or a "god repository".
 */
interface CategoryRepository
{
    /**
     * Returns a Flow list of all categories
     */
    fun getAllCategories() : Flow<List<Category>>

    /**
     * Returns true if a category name already exists in the database
     */
    fun categoryNameExists(name: String) : Boolean

    /**
     * Inserts a new Category in a background thread
     */
    suspend fun insertAllCategories(vararg category: Category)

    /**
     * Deletes an existing Category in a background thread
     */
    suspend fun deleteCategory(category: Category)

    /**
     * Updates a category located by ID. Existing category data is replaced
     * with data extracted from provided Category object. Executes in background
     */
    suspend fun updateCategory(category: Category)

    /**
     * Deletes all categories from the database
     */
    suspend fun deleteAll()


}