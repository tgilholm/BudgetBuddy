package com.example.budgetbuddy.domain.usecase

import com.example.budgetbuddy.data.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
)
{
    suspend operator fun invoke(
        rawAmount : String,
        rawCategoryID : Long,
        rawDate : String,
        rawTime: String,


    )
}