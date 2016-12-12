package com.cdelg4do.waiterdroid.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.cdelg4do.waiterdroid.fragments.TableOrdersFragment;
import com.cdelg4do.waiterdroid.model.Table;

import java.util.ArrayList;


// This class is the adapter needed by a ViewPager to iterate between the existing tables.
// Each page of the associated ViewPager will correspond to an instance of TableOrdersFragment.
// ----------------------------------------------------------------------------

public class TablePagerAdapter extends FragmentPagerAdapter {

    // Object attributes
    private final ArrayList<Table> tableList;


    // Class constructor
    public TablePagerAdapter(FragmentManager fm, ArrayList<Table> tableList) {
        super(fm);

        this.tableList = tableList;
    }


    // Methods inherited from FragmentPagerAdapter:

    // Get the TableOrdersFragment for a given position
    @Override
    public Fragment getItem(int position) {

        Table table = tableList.get(position);
        TableOrdersFragment fragment = TableOrdersFragment.newInstance(table.getOrders(),position);

        return fragment;
    }

    // Get the total count of tables
    @Override
    public int getCount() {
        return tableList.size();
    }

    /*
    // Get the page title for a given position
    // (necessary if a PagerTabStrip or a PagerTitleStrip is used inside the ViewPager layout)
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);

        Table table = tableList.get(position);
        return table.getName();
    }
    */

}
