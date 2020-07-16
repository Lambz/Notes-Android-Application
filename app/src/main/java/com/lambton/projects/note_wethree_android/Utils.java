package com.lambton.projects.note_wethree_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;

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

    /**
     * Method to get Title and Snippet in a formatted manner from Address Object
     * @param address - Address Object from which the Title and Snippet needs to be extracted
     * @return - String Array where title is at 0th index and snippet at 1st index
     */
    public static String[] getFormattedAddress(Address address)
    {
        StringBuilder title = new StringBuilder();
        StringBuilder snippet = new StringBuilder();
        if (address.getSubThoroughfare() != null)
        {
            title.append(address.getSubThoroughfare());
        }
        if (address.getThoroughfare() != null)
        {
            if (!title.toString().isEmpty())
            {
                title.append(", ");
            }
            title.append(address.getThoroughfare());
        }
        if (address.getPostalCode() != null)
        {
            if (!title.toString().isEmpty())
            {
                title.append(", ");
            }
            title.append(address.getPostalCode());
        }
        if (address.getLocality() != null)
        {
            snippet.append(address.getLocality());
        }
        if (address.getAdminArea() != null)
        {
            if (!snippet.toString().equals("") || !snippet.toString().equals(" "))
            {
                snippet.append(", ");
            }
            snippet.append(address.getAdminArea());
        }
        if(title.toString().isEmpty())
        {
            title.append("Note Creation Location");
        }
        return new String[]{title.toString(), snippet.toString()};
    }
}
