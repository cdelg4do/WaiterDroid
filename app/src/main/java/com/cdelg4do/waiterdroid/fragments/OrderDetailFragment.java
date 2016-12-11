package com.cdelg4do.waiterdroid.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cdelg4do.waiterdroid.R;
import com.cdelg4do.waiterdroid.adapters.AllergenListAdapter;
import com.cdelg4do.waiterdroid.adapters.TableListAdapter;
import com.cdelg4do.waiterdroid.model.Order;
import com.cdelg4do.waiterdroid.model.Table;
import com.cdelg4do.waiterdroid.utils.Utils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.cdelg4do.waiterdroid.utils.Utils.setListViewHeightBasedOnChildren;


// This class represents the fragment showing the list of orders from a table.
// ----------------------------------------------------------------------------

public class OrderDetailFragment extends Fragment {

    // Class attributes
    private static final String MODEL_KEY = "model";
    private static final String POS_KEY = "pos";

    // Object attributes
    private Order mOrder;                         // Model for this fragment
    private int mPos;


    // Class "constructor":

    public static OrderDetailFragment newInstance(Order model, int pos) {

        // Create the new fragment (using the default constructor)
        OrderDetailFragment fragment = new OrderDetailFragment();

        // We do not keep the model here, just passing it in a bundle to setArguments()
        // (it will be recovered later, in the onCreate() method)
        Bundle arguments = new Bundle();
        arguments.putSerializable(MODEL_KEY, model);
        arguments.putSerializable(POS_KEY, pos);
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

        // Try to get the model from the passed arguments (see the newInstance() method)
        if (getArguments() != null) {

            mOrder = (Order) getArguments().getSerializable(MODEL_KEY);
            mPos = getArguments().getInt(POS_KEY,0);
        }
    }


    // This method is called to have the fragment instantiate its user interface view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Get the root view of the fragment based on its corresponding layout
        // It will be attached to container (which is the corresponding activity view), but not yet
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        // Reference to UI elements
        TextView txtOrderPos = (TextView) rootView.findViewById(R.id.txtOrderPos);
        TextView txtDishName = (TextView) rootView.findViewById(R.id.txtDishName);
        ImageView imgDishImage = (ImageView) rootView.findViewById(R.id.imgDishImage);
        TextView txtDishDescription = (TextView) rootView.findViewById(R.id.txtDishDescription);
        ListView listAllergens = (ListView) rootView.findViewById(R.id.listAllergens);
        TextView txtCaptionNotes = (TextView) rootView.findViewById(R.id.txtCaptionNotes);
        final EditText txtNotes = (EditText) rootView.findViewById(R.id.txtNotes);
        Button btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        Button btnOk = (Button) rootView.findViewById(R.id.btnOk);

        // Update the views with the model data
        txtOrderPos.setText("Orden: " + mPos);
        txtDishName.setText( mOrder.getDishName() );
        // imgDishImage
        txtDishDescription.setText( mOrder.getDishDescription() );
        txtCaptionNotes.setText("Notas adicionales:");
        txtNotes.setText( mOrder.getNotes() );

        // If the dish contains some allergen, show the list using an adapter.
        // Otherwise, just hide the list.
        if (mOrder.getAllergens().size()>0) {

            AllergenListAdapter adapter = new AllergenListAdapter(getActivity(), mOrder.getAllergens());
            listAllergens.setAdapter(adapter);
            Utils.setListViewHeightBasedOnChildren(listAllergens);
        }
        else
            listAllergens.setVisibility(View.GONE);

        // Buttons behavior
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                int resultCode = RESULT_CANCELED;
                getActivity().setResult(resultCode, returnIntent);
                getActivity().finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Update the order with possible changes made on this fragment
                mOrder.setNotes( txtNotes.getText().toString() );

                Intent returnIntent = new Intent();
                int resultCode = RESULT_OK;
                getActivity().setResult(resultCode, returnIntent);
                getActivity().finish();
            }
        });

        // Return the root view of the fragment
        return rootView;
    }

}
