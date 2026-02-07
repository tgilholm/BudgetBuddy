package com.example.budgetbuddy.domain.services

import com.example.budgetbuddy.R
import com.example.budgetbuddy.data.CategoryRepository
import com.example.budgetbuddy.data.PreferenceRepository
import com.example.budgetbuddy.data.TransactionRepository
import com.example.budgetbuddy.data.entities.Category
import javax.inject.Inject

class MaintenanceService @Inject constructor(
    private val categoryRepo: CategoryRepository,
    private val transactionRepo: TransactionRepository,
    private val prefsRepo: PreferenceRepository
)
{
    // List of default categories to add upon initialisation
    //TODO : Move to DB callback in DBModule
    val defaultCategories = listOf(
        Category(name = "Entertainment", colorID = R.color.limeGreen),
        Category(name = "Petrol", colorID = R.color.lightBlue),
        Category(name = "Pets", colorID = R.color.darkOrange),
        Category(name = "Travel", colorID = R.color.purple),
        Category(name = "Shopping", colorID = R.color.teal),
        Category(name = "Income", colorID = R.color.hotPink)
    )

    /**
     * Sets the app up for use after the onboarding activity has been completed.
     * Initialises the default category list, sets the budget to the user's choice
     * and marks "onboarding complete" as true
     */
    suspend fun initialiseFirstRun(initialBudget: Double)
    {
        // Convert the list of defaults to a typed array and spread it to the repo
        categoryRepo.insertAllCategories(*defaultCategories.toTypedArray())
        prefsRepo.updateBudget(initialBudget)
        prefsRepo.setOnboardingCompleted(true)
    }

    /**
     * Resets the application to its factory default settings. Removes all transactions
     * and categories, then resets the budget to 0.0 and updates "onboarding complete" to false
     */
    suspend fun factoryReset()
    {
        transactionRepo.deleteAll()
        categoryRepo.deleteAll()
        prefsRepo.updateBudget(0.0)
        prefsRepo.setOnboardingCompleted(false)
        // todo- return to main?
    }
}