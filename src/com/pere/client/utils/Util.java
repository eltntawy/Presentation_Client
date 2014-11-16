package com.pere.client.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by eltntawy on 14/11/14.
 */
public class Util {

    public Util () {

    }
    public static void AlertBox(final Activity activity,String title, String message) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message + " Press OK to exit.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.finish();
                    }
                }).show();
    }
}
