package com.example.budgetbuddy.data.impl

import com.example.budgetbuddy.data.CategoryRepository
import com.example.budgetbuddy.data.db.CategoryDao
import com.example.budgetbuddy.domain.entities.Category
import javax.inject.Inject

/**
 * Implements the Category Repository interface. Dao is injected
 * at runtime. Implementation is separate from definition. Users remain
 * oblivious to Room/DAOs, etc.
 *
 */
class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository // Implements CategoryRepo methods
{
    /**
     * Returns a Flow list of all categories
     */
    override fun getAllCategories() = dao.getAll()

    /**
     * Inserts a new Category in a background thread
     */
    override suspend fun insertAllCategories(vararg category: Category)
    {
        dao.insertAll(*category)    // insert all at once- "spread" operator
    }

    /**
     * Deletes an existing Category in a background thread
     */
    override suspend fun deleteCategory(category: Category)
    {
        dao.delete(category)
    }

    /**
     * Updates a category located by ID. Existing category data is replaced
     * with data extracted from provided Category object. Executes in background
     */
    override suspend fun updateCategory(category: Category)
    {
        dao.update(category)
    }
}