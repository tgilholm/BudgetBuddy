package com.example.budgetbuddy.domain.usecase

import com.example.budgetbuddy.data.CategoryRepository
import com.example.budgetbuddy.domain.Result
import com.example.budgetbuddy.domain.entities.Category
import com.example.budgetbuddy.domain.entities.CategoryColor
import com.example.budgetbuddy.domain.entities.CategoryError
import com.example.budgetbuddy.domain.entities.CategoryName
import com.example.budgetbuddy.domain.getOrReturn
import javax.inject.Inject

/**
 * Use Case for adding new Categories to the Repository. Parses raw values
 * into value classes with built-in validation, then checks for duplicate
 * category names and finally adds the new Category.
 *
 * "Expensive" DB queries are prevented by returning early (getOrReturn)
 * if the value class parsing fails; value classes act as "guards" against
 * invalid data.
 */
class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
)
{
    suspend operator fun invoke(
        rawName: String,
        rawColor: Int
    ): Result<CategoryError, Unit>   // Return an error if failed, or nothing if successful
    {
        // Check value class parameters
        val name = CategoryName.create(rawName).getOrReturn { return it }
        val color = CategoryColor.create(rawColor).getOrReturn { return it }

        // Enforce non-duplicate categories
        if (repository.categoryNameExists(name.value))
        {
            return Result.Failure(CategoryError.AlreadyExists)
        }

        // If successful, create and add
        repository.insertAllCategories(Category(name = name.value, colorID = color.value))

        return Result.Success(Unit)
    }
}