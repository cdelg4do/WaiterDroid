package com.cdelg4do.waiterdroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.model.Dish;
import com.cdelg4do.waiterdroid.model.RestaurantManager;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


// This class is the adapter needed by a ListView to represent the invoice of a table.
// ----------------------------------------------------------------------------

public class InvoiceListAdapter extends BaseAdapter {

    // Class attributes
    private final static int headerlayout = R.layout.row_invoice_header;
    private final static int footerlayout = R.layout.row_invoice_footer;
    private final static int itemlayout = R.layout.row_invoice_item;

    // Object attributes
    private final HashMap<Dish,Integer> tableMap;
    private final String tableName;
    private final BigDecimal priceBeforeTax;
    private final int rowCount;
    private LayoutInflater inflater;


    // Class constructor
    public InvoiceListAdapter(Context context, Table table) {

        tableMap = table.getOrdersMap();;
        tableName = table.getName();
        priceBeforeTax = table.priceBeforeTax();
        rowCount = tableMap.size() + 2;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // Methods inherited from BaseAdapter:

    // Total elements on the list
    @Override
    public int getCount()
    {
        return rowCount;
    }

    // Gets the item at a given position
    @Override
    public Map.Entry getItem(int pos)
    {
        if ( pos == 0 || pos == rowCount-1 )
            return null;

        Map.Entry entry = null;
        Iterator iterator = tableMap.entrySet().iterator();
        int i = 0;

        while (iterator.hasNext() && i < pos) {
            entry = (Map.Entry) iterator.next();
            i++;
        }

        return entry;
    }

    // Gets the table id at a given position (not used)
    @Override
    public long getItemId(int pos)
    {
        return (long) pos;
    }

    // Gets the row view for a given position
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        // Header
        if (pos == 0) {

            if(convertView == null)
                convertView = inflater.inflate(headerlayout, parent, false);

            // Reference to UI elements
            TextView txtTableName = (TextView) convertView.findViewById(R.id.txtTableName);
            TextView txtInvoiceDate = (TextView) convertView.findViewById(R.id.txtInvoiceDate);
            TextView txtDishCountHeader = (TextView) convertView.findViewById(R.id.txtDishCountHeader);
            TextView txtDishPriceHeader = (TextView) convertView.findViewById(R.id.txtDishPriceHeader);
            TextView txtDishNameHeader = (TextView) convertView.findViewById(R.id.txtDishNameHeader);

            // Sync view
            txtTableName.setText(tableName);
            txtInvoiceDate.setText( new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) );
            txtDishCountHeader.setText("Cant.");
            txtDishPriceHeader.setText("Precio");
            txtDishNameHeader.setText("Pedido");
        }

        // Footer
        else if (pos == rowCount-1) {

            if(convertView == null)
                convertView = inflater.inflate(footerlayout, parent, false);

            // Reference to UI elements
            TextView txtSubtotal = (TextView) convertView.findViewById(R.id.txtSubtotal);
            TextView txtSubtotalPrice = (TextView) convertView.findViewById(R.id.txtSubtotalPrice);
            TextView txtTax = (TextView) convertView.findViewById(R.id.txtTax);
            TextView txtTaxPrice = (TextView) convertView.findViewById(R.id.txtTaxPrice);
            TextView txtTaxPercentage = (TextView) convertView.findViewById(R.id.txtTaxPercentage);
            TextView txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
            TextView txtTotalPrice = (TextView) convertView.findViewById(R.id.txtTotalPrice);

            // BigDecimal - String conversions
            String txtPriceBefore = Utils.getMoneyString(priceBeforeTax, RestaurantManager.getCurrency());

            BigDecimal taxAmount = priceBeforeTax.multiply(RestaurantManager.getTaxRate());
            String txtTaxAmount = Utils.getMoneyString(taxAmount, RestaurantManager.getCurrency());

            BigDecimal taxPercent = RestaurantManager.getTaxRate().multiply(new BigDecimal(100));
            String txtTaxPercent = Utils.getMoneyString(taxPercent,"%");

            BigDecimal priceAfter = priceBeforeTax.add(taxAmount);
            String txtPriceAfter = Utils.getMoneyString(priceAfter, RestaurantManager.getCurrency());

            // Sync view
            txtSubtotal.setText("Subtotal");
            txtSubtotalPrice.setText(txtPriceBefore);
            txtTax.setText("IVA");
            txtTaxPercentage.setText(txtTaxPercent);
            txtTaxPrice.setText(txtTaxAmount);
            txtTotal.setText("TOTAL");
            txtTotalPrice.setText(txtPriceAfter);
        }

        // Some dish
        else {

            if(convertView == null)
                convertView = inflater.inflate(itemlayout, parent, false);

            // Reference to UI elements
            TextView txtDishCount = (TextView) convertView.findViewById(R.id.txtDishCount);
            TextView txtDishName = (TextView) convertView.findViewById(R.id.txtDishName);
            TextView txtDishPrice = (TextView) convertView.findViewById(R.id.txtDishPrice);

            // Get the dish info
            Dish dish = (Dish) getItem(pos).getKey();
            Integer count = (Integer) getItem(pos).getValue();
            String price = Utils.getMoneyString(dish.price, RestaurantManager.getCurrency());

            // Sync view
            txtDishCount.setText(count.toString());
            txtDishName.setText(dish.name);
            txtDishPrice.setText(price);
        }


        return convertView;
    }

}
