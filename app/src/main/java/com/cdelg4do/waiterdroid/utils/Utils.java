package com.cdelg4do.waiterdroid.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.Toast;

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



}
