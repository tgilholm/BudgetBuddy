package com.example.budgetbuddy.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.budgetbuddy.R
import com.example.budgetbuddy.ui.UIEvent
import com.example.budgetbuddy.ui.viewmodel.AddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add)
{
    // Get the ViewModel
    private val viewModel by viewModels<AddViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                // Handle events passed down via the ViewModel
                viewModel.events.collect { event ->
                    when (event)
                    {
                        is UIEvent.ShowToast ->
                        {
                            Toast.makeText(context, event.messageId, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}