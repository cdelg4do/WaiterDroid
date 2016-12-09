package com.cdelg4do.waiterdroid.model;


// This class represents a dish ordered by some customer.
// Implements the Serializable interface so that it can be passed inside a bundle object.
// ----------------------------------------------------------------------------

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

public class Order implements Serializable {

    // Object attributes
    private Dish mDish;
    private String mNotes;


    // Class constructor
    public Order(Dish dish, String notes) {

        mDish = dish;
        mNotes = notes;
    }


    // Class setters:

    public void setNotes(String newNotes) {
        mNotes = newNotes;
    }


    // Class getters:

    public String getNotes() {
        return mNotes;
    }


    // Other methods:

    // Gets the dish name
    public String getName() {
        return mDish.name;
    }

    // Gets the dish description
    public String getDescription() {
        return mDish.description;
    }

    // Gets the url for the dish image
    public URL getImageUrl() {
        return mDish.imageUrl;
    }

    // Gets the dish price
    public BigDecimal price() {
        return mDish.price;
    }

    // Gets the allergen list
    public ArrayList<Allergen> getAllergens() {
        return mDish.allergens;
    }
}
