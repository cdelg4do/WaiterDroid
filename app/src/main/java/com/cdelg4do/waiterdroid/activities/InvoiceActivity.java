package com.cdelg4do.waiterdroid.activities;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.adapters.InvoiceListAdapter;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;


// This class represents the activity used to represent the invoice of a table.
// ----------------------------------------------------------------------------

public class InvoiceActivity extends AppCompatActivity {

    // Class attributes
    public static final String TABLE_POS_KEY = "orderTable";  // The table's position in the restaurant's table list


    // Object attributes
    private Table mTable;
    private int mTablePos;


    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Set the toolbar as the action bar for this activity and show the 'back' button up on it
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the data from the intent passed by the previous activity
        mTablePos = getIntent().getIntExtra(TABLE_POS_KEY,-1);

        if ( mTablePos == -1 ) {
            String msg = "Missing data provided by the intent!";
            Log.d("InvoiceActivity","ERROR: " + msg);
            Utils.showMessage(this,msg,Utils.MessageType.DIALOG,"ERROR");
            return;
        }

        // Get a reference to the table object
        mTable = RestaurantManager.getTableAtPos(mTablePos);

        if ( mTable == null ) {
            String msg = "Wrong table index! (" + mTablePos + ")";
            Log.d("InvoiceActivity","ERROR: " + msg);
            Utils.showMessage(this,msg,Utils.MessageType.DIALOG,"ERROR");
            return;
        }

        // Reference to UI elements
        ListView invoiceList = (ListView) findViewById(R.id.invoiceList);

        // If the table contains some order, show the list using an adapter.
        // Otherwise, just hide the list.
        if (mTable.getOrders().size()>0) {

            InvoiceListAdapter adapter = new InvoiceListAdapter(this,mTable);
            invoiceList.setAdapter(adapter);
            //Utils.setListViewHeightBasedOnChildren(invoiceList);
        }
        else {
            invoiceList.setVisibility(View.GONE);

            String msg = "This table does not have any order";
            Log.d("InvoiceActivity","INFO: " + msg);
            Utils.showMessage(this,msg,Utils.MessageType.DIALOG,"INFO");
            return;
        }
    }
}
