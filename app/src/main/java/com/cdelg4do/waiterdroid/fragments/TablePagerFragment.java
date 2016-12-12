package com.cdelg4do.waiterdroid.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.adapters.TablePagerAdapter;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.model.Table;

import java.util.ArrayList;


// This class represents the fragment with the view pager
// to scroll over the details of all the existing tables.
// Each page will correspond to an instance of TableOrdersFragment.
// ----------------------------------------------------------------------------

public class TablePagerFragment extends Fragment {

    // Class attributes
    //private static final String MODEL_KEY = "model";
    private static final String INDEX_KEY = "index";

    // Object attributes
    private ArrayList<Table> tableList;  // Model for this fragment
    private int initialTableIndex;       // Index of the initial table
    private ViewPager viewPager;


    // Class constructor:

    public static TablePagerFragment newInstance(int tableIndex) {

        // Create the new fragment (using the default constructor)
        TablePagerFragment fragment = new TablePagerFragment();

        // We do not keep the parameters here, just passing them in a bundle to setArguments()
        // (they will be recovered later, in the onCreate() method)
        Bundle arguments = new Bundle();
        arguments.putInt(INDEX_KEY, tableIndex);
        fragment.setArguments(arguments);

        // Return the new fragment
        return fragment;
    }


    // Methods inherited from Fragment:

    // This method is called for the initial creation of the fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Indicates if this fragment would populate a menu (true) by calling onCreateOptionsMenu()
        setHasOptionsMenu(true);

        // Try to get the passed arguments of newInstance()
        if (getArguments() != null)
            initialTableIndex = getArguments().getInt(INDEX_KEY);

        // Get a reference to the table list
        tableList = RestaurantManager.getTables();

        setHasOptionsMenu(true);
    }

    // This method is called to have the fragment instantiate its user interface view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Get the root view of the fragment based on its corresponding layout
        // It will be attached to container (which is the corresponding activity view), but not yet
        View rootView = inflater.inflate(R.layout.fragment_table_pager, container, false);

        // Reference to UI elements
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);

        // Define a listener for the ViewPager
        // (it must implement the interface ViewPager.OnPageChangeListener)
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // When a new page is selected, update the action bar title with the new table name
            @Override
            public void onPageSelected(int position) {

                updateActionBarTitle( tableList.get(position).getName() );
            }

            // When the current page is scrolled (by the user or programmatically), do nothing
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // When the scroll state changes (by the user or programmatically), do nothing
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Create the adapter to load the pages into the view, and assign it to the View Pager
        TablePagerAdapter adapter = new TablePagerAdapter(getFragmentManager(),tableList);
        viewPager.setAdapter(adapter);

        // Set the currently selected page to the table at the initial index
        viewPager.setCurrentItem(initialTableIndex);

        // Set the action bar title (the name of the table at the initial index)
        updateActionBarTitle( tableList.get(initialTableIndex).getName() );

        // Return the root view of the fragment
        return rootView;
    }


    // Auxiliar methods:

    // Gives a title to the action bar of the activity containing this fragment
    // (works only if the container activity inherits from AppCompatActivity and has an action bar)
    private void updateActionBarTitle(String newTitle) {

        if (getActivity() instanceof AppCompatActivity) {

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (actionBar != null)
                actionBar.setTitle(newTitle);
        }
    }

    // Updates the pager with the table in a given position
    public void moveToPosition(int position) {
        viewPager.setCurrentItem(position);
    }

    //
    public void syncView(int tablePos) {

        tableList = RestaurantManager.getTables();

        TablePagerAdapter adapter = new TablePagerAdapter(getFragmentManager(),tableList);
        viewPager.setAdapter(adapter);

        moveToPosition(tablePos);
    }

}
