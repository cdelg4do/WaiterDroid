package com.cdelg4do.waiterdroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.model.Order;

import java.util.ArrayList;


// This class is the adapter needed by a ListView to represent the list of orders for a table.
// ----------------------------------------------------------------------------

public class OrderListAdapter extends BaseAdapter {

    // Class attributes
    private final static int rowlayout = R.layout.order_row;
    private final static int rowlayout_empty = R.layout.order_row_empty;

    // Object attributes
    private final ArrayList<Order> orderList;
    private LayoutInflater inflater;


    // Class constructor
    public OrderListAdapter(Context context, ArrayList<Order> orderList) {

        this.orderList = orderList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // Methods inherited from BaseAdapter:

    // Total elements on the list
    @Override
    public int getCount()
    {
        if ( modelIsEmpty() )
            return 1;

        return orderList.size();
    }

    // Gets the order at a given position
    @Override
    public Order getItem(int pos)
    {
        if ( !modelIsEmpty() )
            return orderList.get(pos);

        return null;
    }

    // Gets the order id at a given position (not used)
    @Override
    public long getItemId(int pos)
    {
        return (long) pos;
    }

    // Gets the row view for a given position
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        // If there are no orders to list, just show a row with an informative text
        if ( modelIsEmpty() ) {

            if (convertView == null)
                convertView = inflater.inflate(rowlayout_empty, parent, false);

            // Reference to UI elements
            TextView txtNoOrders  = (TextView) convertView.findViewById(R.id.txtNoOrders);

            // Tell the user the table has no orders yet
            txtNoOrders.setText("Esta mesa no ha ordenado ningún plato aún.");

            // The user cannot select this row
            convertView.setEnabled(false);
        }

        // If there are orders to show in the model, construct the row view as usual
        else {

            if (convertView == null)
                convertView = inflater.inflate(rowlayout, parent, false);

            // Reference to UI elements
            ImageView dishImage = (ImageView) convertView.findViewById(R.id.imgDishImage);
            TextView dishName  = (TextView) convertView.findViewById(R.id.txtDishName);
            TextView orderNotes  = (TextView) convertView.findViewById(R.id.txtOrderNotes);

            // Sync the view with the table data
            dishName.setText( orderList.get(pos).getDishName() );
            orderNotes.setText( orderList.get(pos).getNotes() );

            // dishImage.setImageBitmap(...);

            // The user can select this row
            convertView.setEnabled(true);
        }


        return convertView;
    }


    // Auxiliary methods:

    // Determines if the data model for the adapter has no items
    private boolean modelIsEmpty() {

        if ( orderList != null && orderList.size() > 0)
            return false;

        return true;
    }

}
