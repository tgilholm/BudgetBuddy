package com.example.budgettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgettracker.R;
import com.example.budgettracker.Transaction;

import java.util.Objects;

// The adapter for the RecyclerView in OverviewFragment
// Extends ListAdapter to improve performance- does not need to rewrite entire list on update
public class RecyclerViewAdapter extends ListAdapter<Transaction, RecyclerViewAdapter.ViewHolder>
{
    public RecyclerViewAdapter()
    {
        super(DIFF_CALLBACK);
    }

    // DIFF_CALLBACK calculates the difference between old and new lists
    // This reduces overhead by not having to update the entire list
    private static final DiffUtil.ItemCallback<Transaction> DIFF_CALLBACK = new DiffUtil.ItemCallback<>()
    {

        // Compares Transactions by their IDs
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem)
        {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem)
        {
            return oldItem.equals(newItem);
        }
    };


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Transaction transaction = getItem(position);
        holder.bind(transaction);
    }

    // ViewHolder for the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textCategory;
        private final TextView textAmount;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Find the textViews from the layout
            textCategory = itemView.findViewById(R.id.textCategory);
            textAmount = itemView.findViewById(R.id.textAmount);
        }

        // Bind the data from a transaction to the textViews on the layout
        public void bind(Transaction transaction)
        {
            textCategory.setText(transaction.getCategory());
            textAmount.setText(String.valueOf(transaction.getAmount()));

        }
    }
}