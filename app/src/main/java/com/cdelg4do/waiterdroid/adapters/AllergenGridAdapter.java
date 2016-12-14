package com.cdelg4do.waiterdroid.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownLoadImageTask;
import com.cdelg4do.waiterdroid.model.Allergen;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


// This class is the adapter needed by a GridView to represent the allergens of a dish.
// ----------------------------------------------------------------------------

public class AllergenGridAdapter extends BaseAdapter implements BackgroundTaskListener {

    // Object attributes
    private Context context;
    private final ArrayList<Allergen> allergenList;
    private int brokenImageResource;


    // Class constructor
    public AllergenGridAdapter(Context context, ArrayList<Allergen> allergenList, int brokenImageResource) {

        this.context = context;
        this.allergenList = allergenList;
        this.brokenImageResource = brokenImageResource;
    }


    // Methods inherited from BaseAdapter:

    // Total elements on the list
    @Override
    public int getCount()
    {
        if ( modelIsEmpty() )
            return 1;

        return allergenList.size();
    }

    // Gets the order at a given position
    @Override
    public Allergen getItem(int pos)
    {
        if ( !modelIsEmpty() )
            return allergenList.get(pos);

        return null;
    }

    // Determines if a row of the list can be selected
    @Override
    public boolean isEnabled(int position) {

        // Any row can be selected only if there are orders to show in the list.
        // Otherwise, no one can be selected.
        return ( !modelIsEmpty() );
    }

    // Gets the allergen id at a given position (not used)
    @Override
    public long getItemId(int pos)
    {
        return (long) pos;
    }

    // Gets the row view for a given position
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {

            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
        }
        else {
            imageView = (ImageView) convertView;
        }

        // Attempt to download the allergen image in background
        Utils.downloadImageInBackground(allergenList.get(pos).imageUrl, imageView, this);

        return imageView;
    }


    // Methods inherited from the BackgroundTaskListener interface:

    public void onBackgroundTaskFinished(BackgroundTaskHandler taskHandler) {

        // Determine if the task was the load of the dish image
        if ( taskHandler.getTaskId() == DownLoadImageTask.taskId ) {

            Utils.showDownloadedImage(taskHandler,brokenImageResource);
        }
    }


    // Auxiliary methods/classes:

    // Determines if the data model for the adapter has no items
    private boolean modelIsEmpty() {

        if ( allergenList != null && allergenList.size() > 0)
            return false;

        return true;
    }


    // This class is just a container with references to the row UI elements
    public class ViewHolder {

        public ImageView dishImage;
        public TextView dishName;
        public TextView orderNotes;
    }

}
