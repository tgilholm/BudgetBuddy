package com.example.budgetbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.data.PreferenceRepository
import com.example.budgetbuddy.domain.services.MaintenanceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * ViewModel corresponding to StartupActivity executed before the
 * MainActivity is launched. Handles firstRun flag and interfaces
 * with the MaintenanceService to initialise the app data.
 */
@HiltViewModel
class StartupViewModel @Inject constructor(
    private val maintenanceService: MaintenanceService,
    private val prefsRepo: PreferenceRepository
) : ViewModel()
{
    /**
     * Accesses the PreferenceRepository to determine whether the app
     * has been launched before. Returns true if not, false otherwise
     */
    fun isFirstRun(): Boolean = runBlocking {
        !prefsRepo.isOnboardingCompleted.first() // Extract the element from the flow
    }

    fun startApp(budgetInput)


}