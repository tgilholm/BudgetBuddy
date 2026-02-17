package com.example.budgetbuddy.ui

import androidx.annotation.StringRes

sealed interface UIEvent
{
    data class ShowToast(@StringRes val messageId : Int) : UIEvent
}