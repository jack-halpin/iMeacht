package com.eventapp.eventapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import java.io.IOException;

/**
 * Created by devin on 27/11/16.
 */

public class TutorialActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listings_menu, menu);
        return true;
    }

    public void searchByDetails(View view) {
        String location =  findViewById(R.id.textSearchLocation).toString();
        String category = findViewById(R.id.textSearchCategory).toString();
        String date = findViewById(R.id.textSearchDate).toString();
        StringBuilder url = new StringBuilder("?");
        url.append("location=");
        url.append(location);
        url.append("&");
        url.append("category=");
        url.append(category);
        url.append("&");
        url.append("date=");
        url.append(date);
        Intent intent = new Intent(this, ListEventsActivity.class);
        intent.putExtra("url", url.toString());
        startActivity(intent);
    }

    public void searchByRecommended(View view) throws IOException {
        Intent intent = new Intent(this, ListEventsActivity.class);
        intent.putExtra("url", "recommended");
        startActivity(intent);
    }


}
