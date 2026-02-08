package com.example.budgetbuddy.domain.usecase

import com.example.budgetbuddy.data.CategoryRepository
import com.example.budgetbuddy.domain.Result
import com.example.budgetbuddy.domain.entities.Category
import com.example.budgetbuddy.domain.entities.CategoryColor
import com.example.budgetbuddy.domain.entities.CategoryError
import com.example.budgetbuddy.domain.entities.CategoryName
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
)
{
    suspend operator fun invoke(
        name: CategoryName,
        color: CategoryColor
    ): Result<CategoryError, Unit>   // Return an error if failed, or nothing if successful
    {

        // Enforce non-duplicate categories
        if (repository.categoryNameExists(name.value))
        {
            return Result.Failure(CategoryError.AlreadyExists)
        }

        // If successful, create and add
        val category = Category(name = name.value, colorID = color.value)
        repository.insertAllCategories(category)

        return Result.Success(Unit)
    }
}