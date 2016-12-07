package com.cdelg4do.waiterdroid.model;

import java.math.BigDecimal;
import java.util.ArrayList;


// This class represents a table of the restaurant, with all its orders.
// ----------------------------------------------------------------------------

public class Table {

    // Class attributes
    private String mName;
    private ArrayList<Order> mOrders;


    // Class constructor
    // (creates a new table with name and with no orders)
    public Table(String name) {

        mName = name;
        mOrders = new ArrayList<Order>();
    }


    // Class getters:

    public String getName() {
        return mName;
    }


    // Other methods:

    // Returns the count of orders
    public int orderCount() {
        return mOrders.size();
    }

    // Returns the order at a given position, or null if pos is out of range
    public Order getOrderAtPosition(int pos) {

        if (pos < 0 || pos >= mOrders.size() )
            return null;

        return mOrders.get(pos);
    }

    // Adds a new order to the table
    public boolean addOrder(Order newOrder) {
        return mOrders.add(newOrder);
    }

    // Removes an order from the table
    public boolean removeOrder(Order order) {
        return mOrders.remove(order);
    }

    // Gets the price for all the table orders (before taxes)
    public BigDecimal priceBeforeTax() {

        BigDecimal total = new BigDecimal(0);

        for (Order order: mOrders)
            total = total.add( order.price() );

        return total;
    }
}
