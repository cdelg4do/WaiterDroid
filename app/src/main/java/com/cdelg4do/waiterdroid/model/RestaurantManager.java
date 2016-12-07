package com.cdelg4do.waiterdroid.model;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;


// This class contains and manages all the data in the app model.
// ----------------------------------------------------------------------------

public class RestaurantManager {

    // Class attributes
    private Date mMenuDate;
    private BigDecimal mMenuVersion;
    private String mCurrency;
    private BigDecimal mTaxRate;

    private ArrayList<Allergen> availableAllergens;
    private ArrayList<Dish> availableDishes;
    private ArrayList<Table> tables;


    // Class constructor
    // (initializes all attributes, but doesn't populate the allergen and dish lists)
    public RestaurantManager(Date menuDate, BigDecimal menuVersion, String currency, BigDecimal taxRate, int tableCount, String tablePrefix) {

        mMenuDate = menuDate;
        mMenuVersion = menuVersion;
        mCurrency = currency;
        mTaxRate = taxRate;
        this.availableAllergens = new ArrayList<Allergen>();
        this.availableDishes = new ArrayList<Dish>();
        this.tables = new ArrayList<Table>();

        for (int i=0; i<tableCount; i++)
            tables.add(new Table(tablePrefix + " " + i));
    }


    // Class getters:

    public Date getMenuDate() {
        return mMenuDate;
    }

    public BigDecimal getMenuVersion() {
        return mMenuVersion;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public BigDecimal getTaxRate() {
        return mTaxRate;
    }


    // Other methods:

    // Returns the count of available allergens
    public int allergenCount() {
        return availableAllergens.size();
    }

    // Returns the count of available dishes
    public int dishCount() {
        return availableAllergens.size();
    }

    // Returns the count of tables
    public int tableCount() {
        return tables.size();
    }

    // Adds a new allergen to the list of available allergens
    public boolean addAllergen(Allergen newAllergen) {
        return availableAllergens.add(newAllergen);
    }

    // Adds a new dish to the list of available dishes
    public boolean addDish(Dish newDish) {
        return availableDishes.add(newDish);
    }

    // Returns the available allergen with a given id, or null if id doesn't exist
    public Allergen getAllergenById(int searchId) {

        for (Allergen a: availableAllergens) {

            if (a.id == searchId) {
                return a;
            }
        }

        return null;
    }

    // Returns the available dish at a given position, or null if pos is out of range
    public Dish getDishAtPosition(int pos) {

        if (pos < 0 || pos >= availableDishes.size() )
            return null;

        return availableDishes.get(pos);
    }

    // Returns the table at a given position, or null if pos is out of range
    public Table getTableAtPosition(int pos) {

        if (pos < 0 || pos >= tables.size() )
            return null;

        return tables.get(pos);
    }

    // Assigns to a dish the allergens corresponding to a given array of ids
    // (if some id doesn't correspond to any available allergen, it is ignored)
    public void setDishAllergens(Dish dish, int[] allergenIds) {

        for (int id: allergenIds) {

            Allergen allergen = getAllergenById(id);

            if (allergen != null)
                dish.addAllergen(allergen);
            else
                Log.d("RestaurantManager", "WARNING: no available allergen with id: " + id);
        }
    }

    // Gets the final price for all the orders of a given table (including taxes)
    public BigDecimal finalPriceForTable(Table table) {

        BigDecimal base = table.priceBeforeTax();
        BigDecimal taxes = base.multiply(mTaxRate);

        return base.add(taxes);
    }
}
