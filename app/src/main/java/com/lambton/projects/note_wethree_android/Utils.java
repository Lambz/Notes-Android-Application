package com.lambton.projects.note_wethree_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils
{
    public static void showError(String title, String message, Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Okay", (dialog, which) -> dialog.dismiss())
                .create().show();
    }
}
