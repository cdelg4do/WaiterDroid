package com.cdelg4do.waiterdroid.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
import com.cdelg4do.waiterdroid.fragments.TableListFragment;
import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.fragments.TablePagerFragment;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;


// This class represents the main activity of the app, used to represent either the table list
// or both the table list and the table detail view (depending on the device screen and orientation).
//
// Implements the following interfaces:
//
// - BackgroundTaskListener: in order to throw tasks in background using a BackgroundTaskHandler.
//
// - TableListFragment.OnTableSelectedListener: in order to do some action when a table is selected.
//
// - TableOrdersFragment.OnOrderSelectedListener: in order to do some action when an order is selected.
// ----------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity implements BackgroundTaskListener, TableListFragment.OnTableSelectedListener, TableOrdersFragment.OnOrderSelectedListener {

    // Class attributes
    private static final int REQUEST_EDIT_ORDER = 1;

    // Object attributes
    private RestaurantManager restaurantMgr;


    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Device display info
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;                    // Width in pixels
        int height = metrics.heightPixels;                  // Height in pixels
        int dpWidth = (int) (width / metrics.density);      // Width in dp
        int dpHeight = (int) (height / metrics.density);    // Height in dp
        int dpi = metrics.densityDpi;                       // Density in dpi
        String model = Build.MODEL;                         // Device model

        // Reference to the UI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set icon for the toolbar and set it as the toolbar for this activity
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);


        // Before loading any fragment, try to download the dishes from the server
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Downloading available dishes");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        DownloadAvailableDishesTask downloadDishes = new DownloadAvailableDishesTask("http://www.mocky.io/v2/5848fa091100002e11590b72");

        new BackgroundTaskHandler(downloadDishes,this,pDialog).execute();


        // The fragment(s) will be loaded in onBackgroundTaskFinshed(),
        // if the remote data were retrieved successfully.

    }

    // This method is called when another activity called with startActivityForResult() sends response back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If coming back from the Order Detail Activity
        if (requestCode == REQUEST_EDIT_ORDER) {

            if (resultCode == Activity.RESULT_OK) {

                // Refresh the pager view in case something changed
                //int pos = data.getIntExtra(OrderDetailActivity.ORDER_KEY, -1);
                // ...
            }
        }
    }


    // Methods inherited from the TableOrdersFragment.OnOrderSelectedListener interface:

    // What to do when a row in the order list is selected
    // (launch the order detail activity, and wait for some response back)
    @Override
    public void onOrderSelected(Order order, int pos) {

        Intent intent = new Intent(this, OrderDetailActivity.class);

        intent.putExtra(OrderDetailActivity.ORDER_KEY, order);
        intent.putExtra(OrderDetailActivity.POS_KEY, pos);

        startActivityForResult(intent, REQUEST_EDIT_ORDER);
    }


    // Methods inherited from the TableListFragment.OnTableSelectedListener interface:

    // This method indicates what to do when a row in the table list is selected
    @Override
    public void onTableSelected(Table table, int pos) {

        TablePagerFragment pagerFragment = null;

        // Check if there is room for the table pager fragment right now
        if (findViewById(R.id.fragment_table_pager) != null)
            pagerFragment = (TablePagerFragment) getFragmentManager().findFragmentById(R.id.fragment_table_pager);

        // If the activity already had a TablePager fragment, just update it with the selected table
        if ( pagerFragment != null ) {
            pagerFragment.showTable(pos);
        }

        // If the activity did not have a TablePager fragment, then call to a TablePagerActivity
        else {

            Intent intent = new Intent(this, TablePagerActivity.class);

            intent.putExtra(TablePagerActivity.TABLE_LIST_KEY, restaurantMgr.getTables() );
            intent.putExtra(TablePagerActivity.INITIAL_POS_KEY, pos);

            startActivity(intent);
        }

    }


    // Methods inherited from the BackgroundTaskListener interface:

    // This method is called when a background task finishes
    public void onBackgroundTaskFinshed(BackgroundTaskHandler taskHandler) {

        // Determine if the task was the download of the available dishes
        if ( taskHandler.getTaskId() == DownloadAvailableDishesTask.taskId ) {

            // If there were problems, show error message and finish
            if ( taskHandler.hasFailed() || taskHandler.getTaskProduct() == null ) {
                Log.d("MainActivity","ERROR: The data download has failed");
                Utils.showMessage(this,"The data download has failed!",Utils.MessageType.DIALOG,"ERROR");
                return;
            }

            // If everything went OK, keep the Restaurant Manager returned by the background task
            // and use it to load the activity fragment(s)
            restaurantMgr = (RestaurantManager) taskHandler.getTaskProduct();

            Log.d("MainActivity",restaurantMgr.toString());
            Utils.showMessage(this,restaurantMgr.toString(),Utils.MessageType.DIALOG,"SUCCESS");


            // Now we can proceed to load the fragment(s)
            loadActivityFragments();
        }

    }


    // Auxiliary methods:

    // This method is called to manually load the fragments of the activity
    private void loadActivityFragments() {

        // In case this method was called before loading the remote data, do nothing
        if ( restaurantMgr == null )
            return;


        FragmentManager fm = getFragmentManager();

        // Make sure there is space to load the table list (this should be always true)
        if ( findViewById(R.id.fragment_table_list) != null) {

            // We need to add the fragment only if the activity does not have it yet
            // (if the activity was recreated in the past, it might have the fragment already).
            if ( fm.findFragmentById(R.id.fragment_table_list) == null ) {

                TableListFragment tableListFragment = TableListFragment.newInstance( restaurantMgr.getTables() );

                fm.beginTransaction()
                        .add(R.id.fragment_table_list,tableListFragment)
                        .commit();
            }
        }

        // Make sure there is space for the TablePager
        // (only when we are on a big screen and orientation is landscape)
        if (findViewById(R.id.fragment_table_pager) != null) {

            if (fm.findFragmentById(R.id.fragment_table_pager) == null) {

                TablePagerFragment tablePagerFragment = TablePagerFragment.newInstance(restaurantMgr.getTables(), 0);

                fm.beginTransaction()
                        .add(R.id.fragment_table_pager, tablePagerFragment)
                        .commit();
            }
        }
    }
}
