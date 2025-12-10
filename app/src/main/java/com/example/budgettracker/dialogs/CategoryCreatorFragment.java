package com.example.budgettracker.dialogs;


// A dialog fragment that appears when the user clicks the
// "Add category" chip.
// It allows users to select a category name and a colour from a preset list

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.budgettracker.R;

public class CategoryCreatorFragment extends DialogFragment {

    private final Context context;

    public CategoryCreatorFragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_creator, container, false);
    }
}
