package com.eventapp.eventapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetailedEventActivity extends AppCompatActivity {

    private EventListing E;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String a = getIntent().getExtras().getString("EVENT_ID");
        //Log.e("object title", a);
        setContentView(R.layout.activity_detailed_event);

        TextView v = (TextView) findViewById(R.id.textView2);
        v.setText(a);
    }
}