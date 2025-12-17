package com.example.budgetbuddy.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.TransactionWithCategory;

import java.util.List;

// Extends the RecyclerViewAdapter to support removing transactions
public class EditRecyclerViewAdapter extends RecyclerViewAdapter{

    // Define a publicly accessible onDeleteClicked method through an interface
    public interface OnDeleteClickListener {
        void onDeleteClicked(TransactionWithCategory transaction);
    }
    private final OnDeleteClickListener onDeleteClickListener;

    public EditRecyclerViewAdapter(List<TransactionWithCategory> transactions, OnDeleteClickListener onDeleteClickListener, int resource)
    {
        super(transactions, resource);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    // Override the onCreateViewHolder method to create the ViewHolder defined in this class
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(resource, parent, false));
    }

    // Override the onBindViewHolder method to use the ViewHolder defined here instead
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
    {
        if (holder instanceof ViewHolder)   // Check if the holder is using the ViewHolder defined here
        {
            holder.bind(_transactions.get(position));
        }
        else {
            super.onBindViewHolder(holder, position);
        }
    }

    // Extends the RecyclerViewAdapter.ViewHolder class by adding a delete button
    public class ViewHolder extends RecyclerViewAdapter.ViewHolder
    {
        ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        @Override
        public void bind(TransactionWithCategory transaction) {
            super.bind(transaction);        // Call the bind method in RecyclerViewAdapter


            // Extend the bind method by setting a click listener on the deleteButton
            // Removes the transaction when the button is clicked
            setDeleteButton(deleteButton);

        }

        private void setDeleteButton(ImageButton deleteButton) {
            deleteButton.setOnClickListener(view ->
            {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION  && onDeleteClickListener != null)
                {
                    onDeleteClickListener.onDeleteClicked(_transactions.get(position));
                    Log.v("EditRecyclerViewAdapter", "Deleting transaction " + _transactions.get(position).transaction.getId());

                    // Remove from RecyclerView's internal list
                    _transactions.remove(position);

                    // Refresh the RecyclerView
                    notifyItemRemoved(position);
                }
            });
        }

    }
}
