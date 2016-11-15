package com.eventapp.eventapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailedEventActivity extends AppCompatActivity {

    private EventListing E;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String a = getIntent().getExtras().getString("EVENT_ID");
        Log.e("object title", a);
        setContentView(R.layout.activity_detailed_event);

    }
}
