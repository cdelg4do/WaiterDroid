package com.cdelg4do.waiterdroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.fragments.DishListFragment;
import com.cdelg4do.waiterdroid.model.Dish;

import java.util.ArrayList;


// This class is the adapter needed by a RecyclerView to represent the list of available dishes.
// ----------------------------------------------------------------------------

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.DishViewHolder> {

    // Class attributes
    private final static int cellLayout = R.layout.cell_dish;

    // Object attributes
    private Context context;
    private ArrayList<Dish> dishList;
    private String currency;
    private DishListFragment.OnDishSelectedListener onDishSelectedListener;

    public DishListAdapter(Context context, ArrayList<Dish> dishList, String currency, DishListFragment.OnDishSelectedListener listener) {
        super();

        this.dishList = dishList;
        this.currency = currency;
        this.context = context;
        this.onDishSelectedListener = listener;
    }


    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(cellLayout, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, final int position) {

        holder.bindDish(dishList.get(position), context);

        holder.getView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (onDishSelectedListener != null) {
                    onDishSelectedListener.onDishSelected(position, dishList.get(position), view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    protected class DishViewHolder extends RecyclerView.ViewHolder {

        // Reference to UI elements
        private ImageView imgDish;
        private TextView txtDish;
        private GridView gridAllergens;
        private TextView txtPrice;
        private View view;

        public DishViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            imgDish = (ImageView) view.findViewById(R.id.imgDish);
            txtDish = (TextView) view.findViewById(R.id.txtDish);
            gridAllergens = (GridView) view.findViewById(R.id.gridAllergens);
            txtPrice = (TextView) view.findViewById(R.id.txtDishPrice);
        }

        public void bindDish(Dish dish, Context context) {

            txtDish.setText(dish.name);
            txtPrice.setText(dish.price + " " + currency);

            // gridAllergen
            // ...

            // imgDish
            // ...
        }

        public View getView() {
            return view;
        }
    }

}
