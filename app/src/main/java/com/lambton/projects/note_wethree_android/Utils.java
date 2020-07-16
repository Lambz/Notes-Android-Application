package com.lambton.projects.note_wethree_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.lang.reflect.Field;

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

    public static void dump(Object o)
    {
        if(o == null)
        {
            System.out.println("object is null");
            return;
        }
        Field[] fields = o.getClass().getDeclaredFields();
        if(fields.length == 0)
        {
            System.out.println("No fields");
        }
        for (int i=0; i<fields.length; i++)
        {
            try
            {
                System.out.println(fields[i].getName() + " - " + fields[i].get(o));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
