package com.eventapp.eventapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listings_menu, menu);
        return true;
    }

//    public void searchByDetails(View view) {
//        String location =  findViewById(R.id.textSearchLocation).toString();
//        String category = findViewById(R.id.textSearchCategory).toString();
//        String date = findViewById(R.id.textSearchDate).toString();
//        StringBuilder url = new StringBuilder("?");
//        url.append("location=");
//        url.append(location);
//        url.append("&");
//        url.append("category=");
//        url.append(category);
//        url.append("&");
//        url.append("date=");
//        url.append(date);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("url", url.toString());
//        startActivity(intent);
//    }

    public void searchByRecommended(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url", "recommended");
        startActivity(intent);
    }



}
