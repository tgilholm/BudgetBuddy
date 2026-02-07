package com.example.budgetbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.data.entities.Category;
import com.example.budgetbuddy.data.entities.Transaction;
import com.example.budgetbuddy.data.entities.TransactionWithCategory;
import com.example.budgetbuddy.enums.TransactionType;
import com.example.budgetbuddy.utility.ColorHandler;
import com.example.budgetbuddy.utility.Converters;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends <code>RecyclerView.Adapter</code>to display the layout for each <code>TransactionWithCategory</code>
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    @LayoutRes
    protected final int resource;
    protected final List<TransactionWithCategory> _transactions = new ArrayList<>(); // Internal list

    /**
     * Constructs a new adapter
     *
     * @param transactions a list of <code>TransactionWithCategory</code> objects
     * @param resource     the ID of the layout xml to load
     */
    public RecyclerViewAdapter(List<TransactionWithCategory> transactions, @LayoutRes int resource)
    {
        this._transactions.addAll(transactions);    // Instantiate list with transactions
        this.resource = resource;                   // Instantiate layout
    }

    /**
     * Load the layout resource
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return The ViewHolder connected to the layout
     */
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Bind the <code>TransactionWithCategory</code> at the passed <code>position</code> to the <code>ViewHolder</code>
     *
     * @param holder   the <code>ViewHolder</code> object
     * @param position the position in the list to select
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        TransactionWithCategory transaction = _transactions.get(position);    // Get the object at the current position
        holder.bind(transaction);   // Set the data in the view elements
    }

    /**
     * @return the number of items in the list
     */
    @Override
    public int getItemCount()
    {
        return _transactions.size();
    }

    /**
     * Takes the current list and the new list and passes them to <code>DiffUtil.DiffResult</code>
     * to update only the changed items in the list, then dispatches to the internal list
     *
     * @param transactions the new list of <code>TransactionWithCategory</code> objects
     */
    public void updateTransactions(List<TransactionWithCategory> transactions)
    {
        // Compare the list held in the Adapter with the new list
        final TransactionDiffCallback diffCallback = new TransactionDiffCallback(this._transactions, transactions);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this._transactions.clear();
        this._transactions.addAll(transactions);           // Add the new list to the Adapter
        diffResult.dispatchUpdatesTo(this);        // Pass the diffResult to the internal list
    }


    /**
     * Extends <code>RecyclerView.ViewHolder</code> to provide a custom layout for list items
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textCategory;
        private final TextView textDateTime;
        private final TextView textAmount;

        /**
         * Constructs a new <code>ViewHolder</code>
         *
         * @param itemView the <code>View</code> object to construct from
         */
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textDateTime = itemView.findViewById(R.id.textDateTime);
            textCategory = itemView.findViewById(R.id.textCategory);
            textAmount = itemView.findViewById(R.id.textAmount);
        }


        /**
         * Sets the layout to the data in <code>transactionWithCategory</code>
         * Sets colour for negative/positive transactions and updates text fields
         *
         * @param transactionWithCategory an item from the internal list
         */
        public void bind(@NonNull TransactionWithCategory transactionWithCategory)
        {
            Transaction t = transactionWithCategory.transaction;
            Category c = transactionWithCategory.category;

            // Set the category and amount fields
            textCategory.setText(c.getName());
            textAmount.setText(Converters.doubleToCurrencyString(t.getAmount()));

            // Positive transactions are green
            if (t.getType() == TransactionType.INCOMING)
            {
                textAmount.setText(Converters.doubleToCurrencyString(t.getAmount()));
                textAmount.setTextColor(ColorHandler.resolveColorID(itemView.getContext(), R.color.brightGreen));
            } else
            {
                // Negative transactions are red with a minus sign
                textAmount.setText(String.format("-%s", Converters.doubleToCurrencyString(t.getAmount())));
                textAmount.setTextColor(ColorHandler.resolveColorID(itemView.getContext(), R.color.brightRed));
            }
            textDateTime.setText(t.getDateTimeString());
        }
    }
}