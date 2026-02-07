package com.example.budgetbuddy.domain.usecase

import com.example.budgetbuddy.data.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
)
{

}