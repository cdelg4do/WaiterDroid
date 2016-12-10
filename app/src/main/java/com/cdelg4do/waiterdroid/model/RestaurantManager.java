package com.cdelg4do.waiterdroid.model;

import android.util.Log;

import com.cdelg4do.waiterdroid.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;


// This class contains and manages all the data in the app model.
// ----------------------------------------------------------------------------

public class RestaurantManager {

    // Object attributes
    private Date mMenuDate;
    private BigDecimal mMenuVersion;
    private String mCurrency;
    private BigDecimal mTaxRate;

    private ArrayList<Allergen> availableAllergens;
    private ArrayList<Dish> availableDishes;
    private ArrayList<Table> tables;


    // Class constructor
    // (initializes all attributes, but doesn't populate the allergen and dish lists)
    public RestaurantManager(Date menuDate, String currency, BigDecimal taxRate, int tableCount, String tablePrefix) {

        mMenuDate = menuDate;
        mCurrency = currency;
        mTaxRate = taxRate;
        this.availableAllergens = new ArrayList<Allergen>();
        this.availableDishes = new ArrayList<Dish>();
        this.tables = new ArrayList<Table>();

        for (int i=1; i<=tableCount; i++)
            tables.add(new Table(tablePrefix + " " + i));
    }


    // Class getters:

    public Date getMenuDate() {
        return mMenuDate;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public BigDecimal getTaxRate() {
        return mTaxRate;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }


    // Other methods:

    // Returns the count of available allergens
    public int allergenCount() {
        return availableAllergens.size();
    }

    // Returns the count of available dishes
    public int dishCount() {
        return availableDishes.size();
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

    // Returns the available allergen with a given taskId, or null if taskId doesn't exist
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
    // (if some taskId doesn't correspond to any available allergen, it is ignored)
    public void setDishAllergens(Dish dish, ArrayList<Integer> allergenIds) {

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

    // Returns a string with the significant data of this restaurant manager (for debugging)
    @Override
    public String toString() {

        String res = "Date: " + getMenuDate();
        res += "\nCurrency: " + getCurrency();
        res += "\nTax rate: " + getTaxRate();
        res += "\nTables: " + tableCount();
        res += "\nTotal Allergens: " + allergenCount();
        res += "\nTotal Dishes: " + dishCount();

        for (int i=0; i<dishCount(); i++) {
            res += "\n" + getDishAtPosition(i);
        }

        return res;
    }

    // Generates test data
    public void generateRandomOrders() {

        Log.d("RestaurantManager","\nGenerando datos de prueba...\n");

        if (tables.size() < 1 || availableDishes.size() < 1 )
            return;

        ArrayList<String> possibleNotes = new ArrayList<String>();

        possibleNotes.add("");
        possibleNotes.add("Con sacarina.");
        possibleNotes.add("Agitado, no batido.");
        possibleNotes.add("Con dos piedras de hielo y en copa ancha.");
        possibleNotes.add("Que esté bien hecho, el cliente detesta la carne cruda.");
        possibleNotes.add("Servido a la mitad en dos platos, para los niños.");
        possibleNotes.add("Sin cebolla.");
        possibleNotes.add("Sin mayonesa.");
        possibleNotes.add("Con dos gotas de brandy.");

        // How many tables will have some order
        int tablesOrdering = Utils.randomInt(1, tables.size() );

        for (int i=0; i<tablesOrdering-1; i++) {

            Table table = tables.get(i);

            // How many orders will have this table
            int orderCount = Utils.randomInt(1, availableDishes.size() );

            for (int j=1; j<orderCount; j++) {

                // Dish to order
                int dishIndex = Utils.randomInt(1, availableDishes.size() ) - 1;
                int noteIndex = Utils.randomInt(1, possibleNotes.size()) - 1;

                Order newOrder = new Order(availableDishes.get(dishIndex), possibleNotes.get(noteIndex));
                table.addOrder(newOrder);
            }
        }
    }
}
