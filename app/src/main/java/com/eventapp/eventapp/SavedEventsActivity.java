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
import android.widget.Toast;

/**
 * Created by devcon90 on 11/14/16.
 */

public class SavedEventsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);
        Log.e("object title", "Here where the saved events are!");

        EventDbOpenHelper db = new EventDbOpenHelper(this);

        String ids = db.getAllSavedEvents().toString();

        Log.e("saved events", ids);

        TextView v = (TextView) findViewById(R.id.textViewSavedEvents);
        v.setText(ids);
    }

    public void deleteSavedEvents(View view) {
        EventDbOpenHelper db = new EventDbOpenHelper(this);

        db.deleteDatabase();

        Context context = getApplicationContext();
        CharSequence text = "Events Deleted";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
