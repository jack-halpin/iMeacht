package com.eventapp.eventapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
                            selectedItems.add(items[indexSelected]);
                        } else if (selectedItems.contains(indexSelected)) {
                            // otherwise remove it
//                            selectedItems.remove(Integer.valueOf(indexSelected));
                            // original code ^, not sure about this
                            selectedItems.remove(items[indexSelected]);
                        }
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // code to save 'selectedItems' preferences to DB goes in here
                        saveArray(selectedItems);
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });

        dialog = builder.create();
        dialog.show();


    }
    public boolean saveArray(ArrayList PrefArray) {

        SharedPreferences preferences = getSharedPreferences("storedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear().apply();

        for (int i = 0; i < PrefArray.size(); i++) {
            editor.putString("Pref_" + i, PrefArray.get(i).toString());
        }

        editor.commit();

//        Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
        // // TODO: 19/11/2016 Remove testing code!
        Toast.makeText(this, "Query string: " + getPrefString(), Toast.LENGTH_LONG).show();

        return editor.commit();

    }

    public String getPrefString(){
        SharedPreferences sharedPref = getSharedPreferences("storedPrefs", Context.MODE_PRIVATE);
        int size = sharedPref.getAll().size();

        String pref = "&keywords=";
        for(int i = 0; i < size; i++){
            pref += sharedPref.getString("Pref_" + i, "");
            if(i != (size-1)){
                pref += "&";
            }
        }
        return pref;
    }
}
