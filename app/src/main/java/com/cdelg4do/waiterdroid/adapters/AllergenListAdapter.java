package com.cdelg4do.waiterdroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.model.Allergen;

import java.util.ArrayList;


// This class is the adapter needed by a ListView to represent the list of allergens of a dish.
// ----------------------------------------------------------------------------

public class AllergenListAdapter extends BaseAdapter {

    // Class attributes
    private final static int rowlayout = R.layout.row_allergen;

    // Object attributes
    private final ArrayList<Allergen> allergenList;
    private LayoutInflater inflater;


    // Class constructor
    public AllergenListAdapter(Context context, ArrayList<Allergen> allergenList) {

        this.allergenList = allergenList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // Methods inherited from BaseAdapter:

    // Total elements on the list
    @Override
    public int getCount()
    {
        return allergenList.size();
    }

    // Gets the allergen at a given position
    @Override
    public Allergen getItem(int pos)
    {
        return allergenList.get(pos);
    }

    // Determines if a row of the list can be selected
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    // Gets the allergen id at a given position (not used)
    @Override
    public long getItemId(int pos)
    {
        return (long) allergenList.get(pos).id;
    }

    // Gets the row view for a given position
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(rowlayout, parent, false);

        // Reference to UI elements
        ImageView allergenImage = (ImageView) convertView.findViewById(R.id.imgAllergenImage);
        TextView allergenName  = (TextView) convertView.findViewById(R.id.txtAllergenName);

        // Sync the view with the allergen list data
        allergenName.setText( allergenList.get(pos).name );
        // allergenImage.setImageBitmap(...);

        return convertView;
    }

}
