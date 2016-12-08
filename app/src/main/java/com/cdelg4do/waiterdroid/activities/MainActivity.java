package com.cdelg4do.waiterdroid.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskHandler;
import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskListener;
import com.cdelg4do.waiterdroid.backgroundtasks.DownloadAvailableDishesTask;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.utils.Utils;

public class MainActivity extends AppCompatActivity implements BackgroundTaskListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Test the data download
        DownloadAvailableDishesTask downloadDishes = new DownloadAvailableDishesTask("http://www.mocky.io/v2/5848fa091100002e11590b72");

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Downloading available dishes");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        BackgroundTaskHandler taskHandler = new BackgroundTaskHandler(downloadDishes,this,pDialog);

        taskHandler.execute();
    }



    // Methods inherited from the BackgroundTaskListener interface:

    public void onBackgroundTaskFinshed(BackgroundTaskHandler taskHandler) {

        if ( taskHandler.getTask().operationId() == DownloadAvailableDishesTask.taskId ) {

            if ( taskHandler.hasFailed() ) {
                Log.d("MainActivity","ERROR: The data download has failed");

                Utils.showMessage(this,"The data download has failed!",Utils.MessageType.DIALOG,"ERROR");
            }

            else {

                RestaurantManager restaurantMgr = (RestaurantManager) taskHandler.getTask().getResult();

                if ( restaurantMgr != null )
                {
                    Log.d("MainActivity",restaurantMgr.toString());

                    Utils.showMessage(this,restaurantMgr.toString(),Utils.MessageType.DIALOG,"SUCCESS");
                }


            }


        }
    }


}
