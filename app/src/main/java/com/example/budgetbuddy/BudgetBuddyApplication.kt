package com.example.budgetbuddy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Class extending Application serving as the application-level dependency container with
 * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android#kts)
 *
 */
@HiltAndroidApp
class BudgetBuddyApplication : Application()