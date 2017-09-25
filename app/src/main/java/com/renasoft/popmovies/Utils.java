package com.renasoft.popmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/*
 * Created by Mohamed Shanan on 19/04/2016.
 */
public class Utils {
    private static ProgressDialog progress;

    public static void showAlertDialog(final Context context, String title, String message, final View rootView) {

        AlertDialog mDialog = new AlertDialog.Builder(context).create();
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setIcon(R.drawable.ic_info_black_24dp);
        mDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mDialog.show();
    }

    public static void showProgressDialog(final Context context, String title, String Message, boolean b) {
        progress = ProgressDialog.show(context, title, Message, b);
    }

    public static void dismissProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    public static String parseTrailersUrl(String jsonStr) {
        return "";
    }

    public static String parseReviews(String jsonStr) {
        return "";
    }
}
