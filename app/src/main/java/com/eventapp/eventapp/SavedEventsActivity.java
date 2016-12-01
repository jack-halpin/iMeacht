package com.eventapp.eventapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by devcon90 on 11/14/16.
 */

public class SavedEventsActivity extends AppCompatActivity {

//    private EventAdapter eAd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);
        Log.e("object title", "Here where the saved events are!");

        EventDbOpenHelper db = new EventDbOpenHelper(this);

//        String ids = db.getAllSavedEvents().toString();
//        ListView listView = (ListView) findViewById(R.id.activity_list);
        ArrayList<EventListing> savedEvents = db.getAllSavedEvents();

        String all = "";

        for (int i = 0; i < savedEvents.size(); i++){
//            savedEvents.get(i).setBitmapFromURL(savedEvents.get(i).getImgUrl());
            all += savedEvents.get(i).getTitle() + "\n";
            Log.e("savedEVents", savedEvents.get(i).toString());
        }
//
//        eAd = new EventAdapter(this, R.layout.event_list_entry,savedEvents);
//        listView.setAdapter(eAd);

//        String all = savedEvents.toString();

        Log.e("saved events", all);

        getSupportActionBar().setTitle("Favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
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

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void finish() {
        super.finish();
    }

}
