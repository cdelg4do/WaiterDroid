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
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownLoadImageTask;
import com.cdelg4do.waiterdroid.fragments.DishListFragment;
import com.cdelg4do.waiterdroid.model.Dish;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.net.URL;
import java.util.ArrayList;


// This class is the adapter needed by a RecyclerView to represent the list of available dishes.
// ----------------------------------------------------------------------------

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.DishViewHolder> implements BackgroundTaskListener {

    // Class attributes
    private final static int cellLayout = R.layout.cell_dish;

    // Object attributes
    private Context context;
    private ArrayList<Dish> dishList;
    private final int brokenImageResource;
    private String currency;
    private DishListFragment.OnDishSelectedListener onDishSelectedListener;

    //public RecyclerView.Adapter<DishListAdapter.DishViewHolder> AAAAA;


    public DishListAdapter(Context context, ArrayList<Dish> dishList, String currency, DishListFragment.OnDishSelectedListener listener, int brokenImageResource) {
        super();

        this.dishList = dishList;
        this.currency = currency;
        this.context = context;
        this.onDishSelectedListener = listener;
        this.brokenImageResource = brokenImageResource;
    }


    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(cellLayout, parent, false);
        return new DishViewHolder(view, this);
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


    // Methods inherited from the BackgroundTaskListener interface:

    public void onBackgroundTaskFinished(BackgroundTaskHandler taskHandler) {

        // Determine if the task was the load of the dish image
        if ( taskHandler.getTaskId() == DownLoadImageTask.taskId ) {

            Utils.showDownloadedImage(taskHandler,brokenImageResource);
        }
    }



    public class DishViewHolder extends RecyclerView.ViewHolder {

        // Reference to UI elements
        private ImageView imgDish;
        private TextView txtDish;
        private GridView gridAllergens;
        private TextView txtPrice;

        private View view;
        private BackgroundTaskListener listener;


        public DishViewHolder(View itemView, BackgroundTaskListener listener) {
            super(itemView);

            view = itemView;
            this.listener = listener;

            imgDish = (ImageView) view.findViewById(R.id.imgDish);
            txtDish = (TextView) view.findViewById(R.id.txtDish);
            gridAllergens = (GridView) view.findViewById(R.id.gridAllergens);
            txtPrice = (TextView) view.findViewById(R.id.txtDishPrice);

            view.setTag(this);
        }

        public void bindDish(Dish dish, Context context) {

            txtDish.setText(dish.name);
            txtPrice.setText(dish.price + " " + currency);

            // If the dish has some allergen, show the grid using an adapter.
            // Otherwise, just hide the grid.
            if (dish.allergens.size()>0) {

                AllergenGridAdapter adapter = new AllergenGridAdapter(context,dish.allergens,brokenImageResource);
                gridAllergens.setAdapter(adapter);
            }
            else
                gridAllergens.setVisibility(View.GONE);

            // Attempt to download the dish image in background
            Utils.downloadImageInBackground(dish.imageUrl, imgDish, listener);
        }

        public View getView() {
            return view;
        }
    }

}
