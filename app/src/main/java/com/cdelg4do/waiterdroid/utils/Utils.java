package com.cdelg4do.waiterdroid.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cdelg4do.waiterdroid.adapters.AllergenListAdapter;

import java.util.Random;

/**
 * Created by Carlos on 08/12/2016.
 */

public abstract class Utils {

    public static enum MessageType {

        DIALOG,
        TOAST,
        NONE
    }


    // Shows the user a message of the given type (toast, dialog or none)
    // If the type is dialog, a title must be provided.
    public static void showMessage(Context ctx, String msg, MessageType type, String title) {

        if ( type==MessageType.NONE ) {
            return;
        }

        else if ( type==MessageType.TOAST ) {
            Toast.makeText(ctx,msg, Toast.LENGTH_LONG).show();
        }

        else if ( type==MessageType.DIALOG ) {

            AlertDialog dialog = new AlertDialog.Builder(ctx).create();
            dialog.setTitle(title);
            dialog.setMessage(msg);

            // OK buton
            dialog.setButton(
                ctx.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
            );

            dialog.show();
        }
    }


    public static int randomInt(int min, int max) {

        if (min==max)
            return min;

        if (min > max) {
            int newMin = max;
            max = min;
            min = newMin;
        }

        Random rnd = new Random();
        int num = rnd.nextInt((max-min)+1) + min;
        return num;
    }


    // This method is useful when using a ListView inside an ScrollView
    // (https://kennethflynn.wordpress.com/2012/09/12/putting-android-listviews-in-scrollviews/)
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        AllergenListAdapter listAdapter = (AllergenListAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



}