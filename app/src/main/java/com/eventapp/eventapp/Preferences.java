package com.eventapp.eventapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

/**
 * Created by Brendan on 17/11/2016.
 */

public class Preferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        AlertDialog dialog;
        final CharSequence[] items = {" Music ", " Books ", " Art "," Culinary "," Sport ", " Festivals "};
        // placeholder keywords for now, ideally we would use a complete list from Eventful.

        final ArrayList selectedItems=new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select event preferences");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of selected items
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            // add to array
                            selectedItems.add(indexSelected);
                        } else if (selectedItems.contains(indexSelected)) {
                            // otherwise remove it
                            selectedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // code to save 'selectedItems' preferences to DB goes in here
                        saveArray(selectedItems);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  code to cancel here

                    }
                });

        dialog = builder.create();
        dialog.show();


    }
    public boolean saveArray(ArrayList PrefArray) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.putInt("Status_size", PrefArray.size());

        for (int i = 0; i < PrefArray.size(); i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, PrefArray.get(i).toString());
        }

        return mEdit1.commit();

    }
}
