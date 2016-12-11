package com.cdelg4do.waiterdroid.activities;

import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.fragments.OrderDetailFragment;
import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.fragments.TablePagerFragment;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.util.ArrayList;

import static com.cdelg4do.waiterdroid.activities.TablePagerActivity.TABLE_LIST_KEY;

public class OrderDetailActivity extends AppCompatActivity {

    // Class attributes
    public static final String ORDER_KEY = "tableList";
    public static final String POS_KEY = "currentPos";

    // Object attributes
    private Order mOrder;
    private int mPos;


    // Methods inherited from AppCompatActivity:

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Set the toolbar as the action bar for this activity and show the 'back' button up on it
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the data from the intent passed by the previous activity
        mOrder = (Order) getIntent().getSerializableExtra(ORDER_KEY);
        mPos = getIntent().getIntExtra(POS_KEY,-1);

        if ( mOrder == null || mPos == -1 ) {
            Log.d("OrderDetailActivity","ERROR: Missing data provided by the intent");
            Utils.showMessage(this,"Missing data provided by the intent!",Utils.MessageType.DIALOG,"ERROR");
            return;
        }


        // Load the order detail fragment

        FragmentManager fm = getFragmentManager();

        // We need to add the fragment only if the activity does not have it yet
        // (if the activity was recreated in the past, it might have the fragment already).
        if ( fm.findFragmentById(R.id.fragment_order_detail) == null ) {

            OrderDetailFragment orderFragment = OrderDetailFragment.newInstance(mOrder,mPos);

            fm.beginTransaction()
                    .add(R.id.fragment_order_detail,orderFragment)
                    .commit();
        }
    }
}
