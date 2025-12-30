package com.example.budgetbuddy.adapters;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.utility.ColorHandler;

import java.util.List;

/**
 * Extends <code>RecyclerView.Adapter</code> to create a grid layout <code>RecyclerView</code>.
 * Handles selection logic for items and sets colour.
 */
public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ColorViewHolder>
{

    /**
     * Classes that construct a <code>ColorPickerAdapter</code> must define the behaviour of clicking a list item.
     */
    public interface OnItemClickListener
    {
        void onItemClick(int color);
    }

    private final Context context;
    private final List<Integer> colorList;
    private final OnItemClickListener onItemClick;
    private int selectedPosition = -1;  // Default pos, updated when a new item is selected


    /**
     * Constructs a new <code>ColorPickerAdapter</code>
     *
     * @param context     a <code>Context</code> object (from an Activity/Fragment/etc)
     * @param colorList   the list of colours from which to create a grid
     * @param onItemClick the <code>OnItemClickListener</code> implementation.
     */
    public ColorPickerAdapter(Context context, List<Integer> colorList, OnItemClickListener onItemClick)
    {
        this.context = context;
        this.colorList = colorList;
        this.onItemClick = onItemClick;
    }


    /**
     * Inflates the layout for each of the <code>ColorViewHolder</code> objects
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return the <code>ColorViewHolder</code> object
     */
    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Load the layout for each item
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.colour_picker_item, parent, false);
        return new ColorViewHolder(view);
    }

    /**
     * Bind each of the colours to a position in the RecyclerView. Passes <code>position == selectedPosition</code>
     * to the <code>bind</code> method to remove checkmarks on unselected objects.
     *
     * @param holder   a <code>ColorViewHolder</code> object
     * @param position the position to bind to
     */
    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position)
    {
        int colour = colorList.get(position);

        // Remains selected if the position has not changed
        holder.bind(colour, position == selectedPosition);
    }

    /**
     * Returns the amount of items in <code>colorList</code>
     *
     * @return an int value of the number of items
     */
    @Override
    public int getItemCount()
    {
        return colorList.size();
    }

    /**
     * Extends <code>RecyclerView.ViewHolder</code> to provide the view logic for each of the items in the list
     */
    public class ColorViewHolder extends RecyclerView.ViewHolder
    {
        private final View colorView;   // Colour block
        private final View checkmarkView;   // Checkmark icon

        /**
         * Constructs the <code>ViewHolder</code>, instantiates the <code>View</code> objects
         *
         * @param itemView the <code>View</code> object for this entity
         */
        public ColorViewHolder(@NonNull View itemView)
        {
            super(itemView);
            colorView = itemView.findViewById(R.id.colorItem);
            checkmarkView = itemView.findViewById(R.id.checkmark);
        }

        /**
         * Gets the <code>Drawable</code> colour_square and checkmark icon. Hides checkmark icon if
         * <code>isSelected</code> is false. Defines the <code>onClick</code> behaviour for each item
         *
         * @param colour     the ID of the colour
         * @param isSelected true if selected by user, false otherwise
         */
        public void bind(final int colour, boolean isSelected)
        {
            @ColorInt int backgroundColour = ColorHandler.getColorARGB(context, colour);

            colorView.setBackground(ColorHandler.createBackground(ContextCompat.getDrawable(context, R.drawable.colour_square), backgroundColour));

            // Adjust checkmark colour on background luminance
            checkmarkView.setBackground(ColorHandler.createBackground(ContextCompat.getDrawable(context, R.drawable.checkmark), ColorHandler.resolveForegroundColor(context, backgroundColour)));

            // Show checkmark if item selected
            checkmarkView.setVisibility(isSelected ? View.VISIBLE : View.GONE);


            itemView.setOnClickListener(v ->
            {
                // Update the selected position
                int previousPosition = selectedPosition;
                selectedPosition = getAbsoluteAdapterPosition();

                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);

                if (onItemClick != null)
                {
                    onItemClick.onItemClick(colour);
                }
            });
        }


    }
}
