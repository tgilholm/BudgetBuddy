package com.example.budgetbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.PieChartLegendItem;
import com.example.budgetbuddy.utility.ColorHandler;

import java.util.List;

/**
 * Connects to a <code>RecyclerView</code> to display a list of <code>PieChartLegendItem</code> objects
 */
public class PieChartLegendAdapter extends RecyclerView.Adapter<PieChartLegendAdapter.LegendViewHolder>
{
    private final Context context;
    private final List<PieChartLegendItem> legendItems;

    public PieChartLegendAdapter(List<PieChartLegendItem> legendItems, Context context)
    {
        this.legendItems = legendItems;
        this.context = context;
    }

    /**
     * Gets a <code>ViewHolder</code> from the layout resource
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A <code>ViewHolder</code> Object
     */
    @NonNull
    @Override
    public PieChartLegendAdapter.LegendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.legend_item, parent, false);
        return new LegendViewHolder(view);  // Inflate the layout for the view holder
    }

    /**
     * Sets the text field of the <code>TextField</code> to the Legend data
     *
     * @param holder   a <code>RecyclerViewAdapter.ViewHolder</code> object to bind the list to
     * @param position The position in the list to bind the <code>ViewHolder to</code>
     */
    @Override
    public void onBindViewHolder(@NonNull LegendViewHolder holder, int position)
    {

        // Set the name and percentage, and change the colour of the colourBlock to the category colour
        PieChartLegendItem item = legendItems.get(position);

        @ColorInt int backgroundColour = ColorHandler.getColorARGB(context, item.getColor());
        holder.txtLegendName.setText(item.getName());
        holder.txtLegendPercentage.setText(item.getPercentage());
        holder.colorBlock.setBackground(ColorHandler.createBackground(ContextCompat.getDrawable(context, R.drawable.colour_square), backgroundColour));
    }

    @Override
    public int getItemCount()
    {
        return legendItems.size();
    }

    /**
     * Provides the View for each RecyclerView "row"
     */
    public static class LegendViewHolder extends RecyclerView.ViewHolder
    {
        private final View colorBlock;
        private final TextView txtLegendName;
        private final TextView txtLegendPercentage;

        LegendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            colorBlock = itemView.findViewById(R.id.colorBlock);
            txtLegendName = itemView.findViewById(R.id.txtLegendName);
            txtLegendPercentage = itemView.findViewById(R.id.txtLegendPercentage);
        }
    }
}
