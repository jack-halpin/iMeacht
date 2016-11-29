package com.eventapp.eventapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by devin on 22/11/16.
 */

public class SearchActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner1 = (Spinner) findViewById(R.id.sortby_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.sortby_options, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);

        final Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (findViewById(R.id.location_editText).toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter a location.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    searchByDetails();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listings_menu, menu);
        return true;
    }

    public void searchByDetails() {
        EditText medit = (EditText)findViewById(R.id.location_editText);
        String location =  medit.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        String category = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        Spinner spinner1 = (Spinner) findViewById(R.id.sortby_spinner);
        String sortby = spinner1.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

        StringBuilder url = new StringBuilder("&");

        url.append("location=");
        url.append(location);
        url.append("&");
        url.append("category=");
        url.append(category);
        url.append("&");
        url.append("sort_order=");
        url.append(sortby);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url", url.toString());
        startActivity(intent);
    }

    public void searchByRecommended(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url", "recommended");
        startActivity(intent);
    }



}
