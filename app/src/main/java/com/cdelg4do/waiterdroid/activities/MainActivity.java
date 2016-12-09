package com.cdelg4do.waiterdroid.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Build;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownloadAvailableDishesTask;
import com.cdelg4do.waiterdroid.fragments.TableListFragment;
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
// ----------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity implements BackgroundTaskListener, TableListFragment.OnTableSelectedListener {

    // Object attributes
    private RestaurantManager restaurantMgr;


    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Device display info
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpWidth = (int) (width / metrics.density);
        int dpHeight = (int) (height / metrics.density);
        String model = Build.MODEL;
        int dpi = metrics.densityDpi;

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


    // Methods inherited from the TableListFragment.OnTableSelectedListener interface:

    @Override
    public void onTableSelected(Table table, int pos) {

        String title = "Mesa seleccionada";
        String msg = "Pos: " + pos + " (" + table.getName() + ")";

        Utils.showMessage(this, msg, Utils.MessageType.DIALOG, title);

        /*
        // Vamos a comprobar si ya tenemos un pager en nuestra interfaz
        FragmentManager fm = getFragmentManager();
        CityPagerFragment cityPagerFragment = (CityPagerFragment) fm.findFragmentById(R.id.fragment_city_pager);

        if (cityPagerFragment != null) {
            // Tenemos un pager, le decimos que se mueva a otra ciudad
            cityPagerFragment.showCity(position);
        }
        else {
            Intent intent = new Intent(this, CityPagerActivity.class);

            // Le pasamos la ciudad inicial a la actividad siguiente
            intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);

            startActivity(intent);
        }
        */
    }


    // Methods inherited from the BackgroundTaskListener interface:

    // This method is called when a background task finishes
    public void onBackgroundTaskFinshed(BackgroundTaskHandler taskHandler) {

        // Determine if the task was the download of the available dishes
        if ( taskHandler.operationId() == DownloadAvailableDishesTask.taskId ) {

            // If there were problems, show error message and finish
            if ( taskHandler.hasFailed() || taskHandler.getResult() == null ) {
                Log.d("MainActivity","ERROR: The data download has failed");
                Utils.showMessage(this,"The data download has failed!",Utils.MessageType.DIALOG,"ERROR");
                return;
            }

            // If everything went OK, keep the Restaurant Manager returned by the background task
            // and use it to load the activity fragment(s)
            restaurantMgr = (RestaurantManager) taskHandler.getResult();

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

        // Make sure there is space to load the table list
        if ( findViewById(R.id.fragment_table_list) != null) {

            if ( fm.findFragmentById(R.id.fragment_table_list) == null ) {

                TableListFragment tableListFragment = TableListFragment.newInstance( restaurantMgr.getTables() );

                fm.beginTransaction()
                        .add(R.id.fragment_table_list,tableListFragment)
                        .commit();
            }
        }

        /*
        // Preguntamos a ver si existe el hueco para CityPager
        if (findViewById(R.id.fragment_city_pager) != null) {
            // Existe el hueco, y habiendo hueco metemos el fragment del CityPager
            if (fm.findFragmentById(R.id.fragment_city_pager) == null) {
                fm.beginTransaction()
                        .add(R.id.fragment_city_pager, new CityPagerFragment())
                        .commit();
            }
        }
        */
    }
}
