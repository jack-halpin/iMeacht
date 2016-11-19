package com.eventapp.eventapp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by devcon90 on 11/14/16.
 */

public class SavedEventsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);
        Log.e("object title", "Here where the saved events are!");

        EventDbOpenHelper db = new EventDbOpenHelper(this);

        Log.e("saved events", db.getAllSavedEvents().toString());
    }
}
