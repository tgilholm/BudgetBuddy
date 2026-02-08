package com.example.budgetbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.domain.entities.CategoryName
import com.example.budgetbuddy.domain.Result
import com.example.budgetbuddy.domain.usecase.AddCategoryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel()
{
    /*
    _uiState - Mutable, internal Channel for updating messages - _private
    uiState  - Immutable Channel observed by listeners to receive messages - public

    A Channel is used for these messages as they are only received once
     */
    private val _uiState = Channel<CategoryError>()

    val uiState = _uiState.receiveAsFlow()    // Immutable stateFlow exposed publicly

    fun addTransaction()

    fun onAddCategory(name: String, colorID: Int)
    {
        val categoryName: CategoryName

        // Attempt to parse to CategoryName object
        when (val nameResult = CategoryName.create(name))   // A ValidationResult object
        {
            // If name validation failed, dispatch the error message
            is Result.Failure ->
            {
                viewModelScope.launch { _uiState.send(nameResult.error) }
            }

            // If succeeded, proceed
            is Result.Success ->
            {
                viewModelScope
            }
        }
    }

}