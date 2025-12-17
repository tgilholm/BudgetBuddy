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

/**
 * Extends RecyclerViewAdapter to provide transaction deletion functionality
 */
public class EditRecyclerViewAdapter extends RecyclerViewAdapter{

    /**
     * Classes using <code>EditRecyclerViewAdapter</code> must define behaviour for the delete button
      */
    public interface OnDeleteClickListener {
        void onDeleteClicked(TransactionWithCategory transaction);
    }
    private final OnDeleteClickListener onDeleteClickListener;

    /**
     * Constructs a new <code>EditRecyclerViewAdapter</code>
     * @param transactions A <code>List</code> of <code>TransactionWithCategory</code> objects
     * @param onDeleteClickListener The behaviour for the delete button
     * @param resource The layout.xml file for the items
     */
    public EditRecyclerViewAdapter(List<TransactionWithCategory> transactions, OnDeleteClickListener onDeleteClickListener, int resource)
    {
        super(transactions, resource);
        this.onDeleteClickListener = onDeleteClickListener;
    }


    /**
     * Gets a <code>ViewHolder</code> from the layout resource
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A <code>ViewHolder</code> Object
     */
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(resource, parent, false));
    }

    /**
     * Overrides <code>onBindViewHolder</code> in <code>RecyclerViewAdapter</code> to load the layout passed to this class instead
     * @param holder a <code>RecyclerViewAdapter.ViewHolder</code> object to bind the transaction list to
     * @param position The position in the list to bind the <code>ViewHolder to</code>
     */
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

    /**
     * Extends <code>RecyclerViewAdapter.ViewHolder</code> to inherit base functionality and implement the delete button
     */
    public class ViewHolder extends RecyclerViewAdapter.ViewHolder
    {
        private final ImageButton deleteButton;

        /**
         * Constructs a new <code>ViewHolder</code> for this object
         * @param itemView the <code>View</code> to create the <code>ViewHolder</code> from
         */
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        /**
         * Passes <code>transaction</code> to parent method.
         * Passes <code>deleteButton</code> to <code>setDeleteButton</code>
         * @param transaction a <code>TransactionWithCategory</code> object
         */
        @Override
        public void bind(TransactionWithCategory transaction) {
            super.bind(transaction);
            setDeleteButton(deleteButton);
        }

        /**
         * Defines the behaviour of the delete button on each view.
         * Removes the item from the internal list and notifies observers
         * @param deleteButton the <code>ImageButton</code> objects
         */
        private void setDeleteButton(@NonNull ImageButton deleteButton) {
            deleteButton.setOnClickListener(view ->
            {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION  && onDeleteClickListener != null)
                {
                    onDeleteClickListener.onDeleteClicked(_transactions.get(position));
                    Log.v("EditRecyclerViewAdapter", "Deleted transaction " + _transactions.get(position).transaction.getId());
                    _transactions.remove(position);             // Remove from RecyclerView's internal list
                    notifyItemRemoved(position);                // Notify the RecyclerView observer
                }
            });
        }
    }
}
