package com.example.budgetbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.R
import com.example.budgetbuddy.domain.Result
import com.example.budgetbuddy.domain.entities.CategoryError
import com.example.budgetbuddy.domain.usecase.AddCategoryUseCase
import com.example.budgetbuddy.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel()
{
    // _ for private vals, omit for public
    // _events is the internal event pipeline, immutable
    // events is the public SharedFlow observed by the fragment to respond to UI events
    private val _events = MutableSharedFlow<UIEvent>()
    val events = _events.asSharedFlow()


    fun addTransaction()

    fun onAddCategory(name: String, colorID: Int)
    {
        viewModelScope.launch {
            // Delegate to the UseCase for validation & adding
            when (val result = addCategoryUseCase(name, colorID))
            {
                // Dispatch the error or success message to the fragment
                is Result.Success ->
                {
                    _events.emit(UIEvent.ShowToast(R.string.category_added))
                }

                is Result.Failure ->
                {
                    _events.emit(UIEvent.ShowToast(mapCategoryError(result.error)))
                }
            }
        }
    }

    private fun mapCategoryError(error: CategoryError): Int = when (error)
    {
        CategoryError.EmptyName -> R.string.err_empty_name
        CategoryError.AlreadyExists -> R.string.err_duplicate_name
        CategoryError.TooLong -> R.string.err_too_long
        CategoryError.NotSelected -> R.string.err_none_selected

    }

}