package com.cdelg4do.waiterdroid.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownloadAvailableDishesTask;
import com.cdelg4do.waiterdroid.fragments.TableListFragment;
import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.fragments.TablePagerFragment;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
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
// - TableOrdersFragment.TableOrdersFragmentListener: in order to do some action when an order is selected.
// ----------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity implements BackgroundTaskListener, TableListFragment.OnTableSelectedListener, TableOrdersFragment.TableOrdersFragmentListener {

    // Class attributes
    private static final int REQUEST_EDIT_ORDER = 1;
    private static final int REQUEST_ADD_ORDER = 2;
    private static final int REQUEST_SHOW_PAGER = 3;

    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to the UI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set icon for the toolbar and set it as the toolbar for this activity
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        // If we already downloaded the data from the server, don't do anything
        if ( RestaurantManager.isSingletonReady() )
            return;

        // Before loading any fragment, try to download the dishes from the server
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Por favor espere");
        pDialog.setMessage("Descrgando el menú del servidor...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        DownloadAvailableDishesTask downloadDishes = new DownloadAvailableDishesTask("http://www.mocky.io/v2/5848fa091100002e11590b72");

        new BackgroundTaskHandler(downloadDishes,this,pDialog).execute();


        // The fragment(s) will be loaded in onBackgroundTaskFinished(),
        // if the remote data were retrieved successfully.

    }

    // This method is called when another activity called with startActivityForResult() sends response back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If coming back from the Pager Activity
        // (no need to get data returned, just update the table list in case something changed)
        if (requestCode == REQUEST_SHOW_PAGER) {

            // Try to refresh the table list fragment, if it exists (it always should)
            TableListFragment listFragment = (TableListFragment) getFragmentManager().findFragmentById(R.id.fragment_table_list);

            if ( listFragment != null ) {
                listFragment.syncView();
            }
        }

        // If coming back from the Order Detail Activity
        if (requestCode == REQUEST_EDIT_ORDER) {

            if (resultCode == Activity.RESULT_OK) {

                // Reference to the pager fragment, if it exists
                TablePagerFragment pagerFragment = (TablePagerFragment) getFragmentManager().findFragmentById(R.id.fragment_table_pager);

                if ( pagerFragment == null )
                    return;

                // Get the data returned by the Detail Activity
                int tablePos = data.getIntExtra(OrderDetailActivity.TABLE_POS_KEY, -1);
                int orderPos = data.getIntExtra(OrderDetailActivity.ORDER_POS_KEY, -1);

                if (tablePos == -1 || orderPos == -1)
                    return;

                // Update the Fragment view
                pagerFragment.syncView(tablePos);
            }
        }

        // If coming back from the Dish List Activity
        if (requestCode == REQUEST_ADD_ORDER) {

            if (resultCode == Activity.RESULT_OK) {

                // Try to refresh the pager fragment, if it exists
                TablePagerFragment pagerFragment = (TablePagerFragment) getFragmentManager().findFragmentById(R.id.fragment_table_pager);

                if ( pagerFragment != null ) {

                    // Get the data returned by the Dish List Activity
                    int tablePos = data.getIntExtra(DishListActivity.TABLE_POS_KEY, -1);

                    if (tablePos == -1)
                        return;

                    // Update the Fragment view
                    pagerFragment.syncView(tablePos);
                }

                // Try to refresh the table list fragment, if it exists (it always should)
                TableListFragment listFragment = (TableListFragment) getFragmentManager().findFragmentById(R.id.fragment_table_list);

                if ( listFragment != null ) {
                    listFragment.syncView();
                }

            }
        }
    }


    // Methods inherited from the TableListFragment.OnTableSelectedListener interface:

    // This method indicates what to do when a row in the table list is selected
    @Override
    public void onTableSelected(int pos) {

        TablePagerFragment pagerFragment = null;

        // Check if there is room for the table pager fragment right now
        if (findViewById(R.id.fragment_table_pager) != null)
            pagerFragment = (TablePagerFragment) getFragmentManager().findFragmentById(R.id.fragment_table_pager);

        // If the activity already had a TablePager fragment, just update it with the selected table
        if ( pagerFragment != null ) {
            pagerFragment.moveToPosition(pos);
        }

        // If the activity did not have a TablePager fragment, then call to a TablePagerActivity
        else {

            Intent intent = new Intent(this, TablePagerActivity.class);
            intent.putExtra(TablePagerActivity.INITIAL_POS_KEY, pos);
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_SHOW_PAGER);
        }

    }


    // Methods inherited from the TableOrdersFragment.TableOrdersFragmentListener interface:

    // What to do when a row in the order list is selected
    // (launch the order detail activity, and wait for some response back)
    @Override
    public void onOrderSelected(int orderPos, int tablePos) {

        Intent intent = new Intent(this, OrderDetailActivity.class);

        intent.putExtra(OrderDetailActivity.ORDER_POS_KEY, orderPos);
        intent.putExtra(OrderDetailActivity.TABLE_POS_KEY, tablePos);

        startActivityForResult(intent, REQUEST_EDIT_ORDER);
    }

    // What to do when a row in the order list is selected
    // (launch the dish list activity to choose a dish, and wait for some response back)
    @Override
    public void onAddOrderClicked(int tablePos) {

        Intent intent = new Intent(this, DishListActivity.class);

        intent.putExtra(DishListActivity.TABLE_POS_KEY, tablePos);

        startActivityForResult(intent, REQUEST_ADD_ORDER);
    }


    // Methods inherited from the BackgroundTaskListener interface:

    // This method is called when a background task finishes
    public void onBackgroundTaskFinished(BackgroundTaskHandler taskHandler) {

        // Determine if the task was the download of the available dishes
        if ( taskHandler.getTaskId() == DownloadAvailableDishesTask.taskId ) {

            // If there were problems, show error message and finish
            if ( taskHandler.hasFailed() ) {
                Log.d("MainActivity","ERROR: The data download has failed!");
                Utils.showMessage(this,"Ha fallado la descarga de datos, compruebe su conexión a internet y vuelva a intentarlo.",Utils.MessageType.DIALOG,"ERROR");
                return;
            }

            // If everything went OK, log the data contained in the RestaurantManager
            Log.d("MainActivity",RestaurantManager.contentToString());
            Utils.showMessage(this,"Datos descargados. La carta de hoy contiene " + RestaurantManager.dishCount() + " platos.",Utils.MessageType.DIALOG,"INFO");


            // Now we can proceed to load the fragment(s) contained in the activity
            loadActivityFragments();
        }

    }


    // Auxiliary methods:

    // This method is called to manually load the fragments of the activity
    private void loadActivityFragments() {

        // In case this method was called before loading the remote data, do nothing
        if ( !RestaurantManager.isSingletonReady() )
            return;


        FragmentManager fm = getFragmentManager();

        // Make sure there is space to load the table list (this should be always true)
        if ( findViewById(R.id.fragment_table_list) != null) {

            // We need to add the fragment only if the activity does not have it yet
            // (if the activity was recreated in the past, it might have the fragment already).
            if ( fm.findFragmentById(R.id.fragment_table_list) == null ) {

                TableListFragment tableListFragment = TableListFragment.newInstance( RestaurantManager.getTables() );

                fm.beginTransaction()
                        .add(R.id.fragment_table_list,tableListFragment)
                        .commit();
            }
        }

        // Make sure there is space for the TablePager
        // (only when we are on a big screen and orientation is landscape)
        if (findViewById(R.id.fragment_table_pager) != null) {

            if (fm.findFragmentById(R.id.fragment_table_pager) == null) {

                TablePagerFragment tablePagerFragment = TablePagerFragment.newInstance(0);

                fm.beginTransaction()
                        .add(R.id.fragment_table_pager, tablePagerFragment)
                        .commit();
            }
        }
    }
}
