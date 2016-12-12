package com.cdelg4do.waiterdroid.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownloadAvailableDishesTask;
import com.cdelg4do.waiterdroid.fragments.DishListFragment;
import com.cdelg4do.waiterdroid.fragments.TableListFragment;
import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.fragments.TablePagerFragment;
import com.cdelg4do.waiterdroid.model.Dish;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.util.ArrayList;


public class DishListActivity extends AppCompatActivity implements DishListFragment.OnDishSelectedListener {

    // Class attributes
    public static final String TABLE_POS_KEY = "orderTable";  // The table's position in the restaurant's table list


    // Object attributes
    private Table mTable;
    private int mTablePos;

    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        // Set the toolbar as the action bar for this activity and show the 'back' button up on it
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        // Get the data from the intent passed by the previous activity
        mTablePos = getIntent().getIntExtra(TABLE_POS_KEY,-1);

        if ( mTablePos == -1 ) {
            String msg = "Missing data provided by the intent!";
            Log.d("DishListActivity","ERROR: " + msg);
            Utils.showMessage(this,msg,Utils.MessageType.DIALOG,"ERROR");
            return;
        }

        // Get a reference to the Table object
        mTable = RestaurantManager.getTableAtPos(mTablePos);

        if ( mTable == null ) {
            String msg = "Wrong table index! (" + mTablePos + ")";
            Log.d("OrderDetailActivity","ERROR: " + msg);
            Utils.showMessage(this,msg,Utils.MessageType.DIALOG,"ERROR");
            return;
        }


        // Load the dish list fragment

        FragmentManager fm = getFragmentManager();

        // We need to add the fragment only if the activity does not have it yet
        // (if the activity was recreated in the past, it might have the fragment already).
        if ( fm.findFragmentById(R.id.fragment_dish_list) == null ) {

            ArrayList<Dish> dishList = RestaurantManager.getDishes();
            String currency = RestaurantManager.getCurrency();

            DishListFragment fragment = DishListFragment.newInstance(dishList,currency);

            fm.beginTransaction()
                    .add(R.id.fragment_dish_list,fragment)
                    .commit();
        }
    }


    // Methods inherited from the DishListFragment.OnDishSelectedListener interface:

    // This method indicates what to do when an item in the dish list is selected
    public void onDishSelected(int position, Dish dish, View view) {

        // Add the selected dish to the table orders
        String initialNote = "";
        mTable.addOrder( new Order(dish,initialNote) );

        // Notify the previous activity that a new order was added to the table
        int resultCode = RESULT_OK;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TABLE_POS_KEY, mTablePos);
        setResult(resultCode, returnIntent);
        finish();
    }

}
