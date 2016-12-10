package com.cdelg4do.waiterdroid.activities;

import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.fragments.TablePagerFragment;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.util.ArrayList;

public class TablePagerActivity extends AppCompatActivity implements TableOrdersFragment.OnOrderSelectedListener {

    // Class attributes
    public static final String TABLE_LIST_KEY = "tableList";
    public static final String INITIAL_POS_KEY = "currentPos";

    // Object attributes
    private ArrayList<Table> tableList;
    private int initialPos;


    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_pager);

        // Reference to the UI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set the toolbar as the action bar for this activity and show the 'back' button up on it
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the data from the intent passed by the previous activity
        tableList = (ArrayList<Table>) getIntent().getSerializableExtra(TABLE_LIST_KEY);
        initialPos = getIntent().getIntExtra(INITIAL_POS_KEY,-1);

        if ( tableList == null || initialPos == -1 ) {
            Log.d("TablePagerActivity","ERROR: Missing data provided by the intent");
            Utils.showMessage(this,"Missing data provided by the intent!",Utils.MessageType.DIALOG,"ERROR");
            return;
        }


        // Load the pager fragment

        FragmentManager fm = getFragmentManager();

        // We need to add the fragment only if the activity does not have it yet
        // (if the activity was recreated in the past, it might have the fragment already).
        if ( fm.findFragmentById(R.id.fragment_table_pager) == null ) {

            TablePagerFragment pagerFragment = TablePagerFragment.newInstance(tableList,initialPos);

            fm.beginTransaction()
                    .add(R.id.fragment_table_pager,pagerFragment)
                    .commit();
        }
    }


    // Methods inherited from the TableListFragment.OnOrderSelectedListener interface:

    @Override
    public void onOrderSelected(Order order, int pos) {

        String title = "Pedido seleccionado";
        String msg = "Pos: " + pos + " (" + order.getDishDescription() + ")";

        Utils.showMessage(this, msg, Utils.MessageType.DIALOG, title);
    }
}
