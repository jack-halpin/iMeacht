package com.eventapp.eventapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailedEventActivity extends AppCompatActivity {

    private EventListing E;
    private ShareActionProvider mShareActionProvider;

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String a = getIntent().getExtras().getString("EVENT_ID");
//        Log.e("object title", a);

        E = getIntent().getExtras().getParcelable("EVENT_OBJ");

        setContentView(R.layout.activity_detailed_event);

        ImageView img = (ImageView) findViewById(R.id.imageViewDetailed);
        img.setImageBitmap(E.getImage());

        TextView v2 = (TextView) findViewById(R.id.textView2);
        v2.setText(E.getTitle());
        TextView v3 = (TextView) findViewById(R.id.textView3);
        v3.setText(E.getDate());
        TextView v4 = (TextView) findViewById(R.id.textView4);
        v4.setText(E.getVenueName());
        TextView v5 = (TextView) findViewById(R.id.textView5);
        v5.setText(E.getDetails());
//        TextView v6 = (TextView) findViewById(R.id.textView6);
//        v6.setText(E.getImgUrl());

    }

    /** Called when the user clicks the Send button */
    public void addCalendar(View view) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2016, 10, 16, 18, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 10, 16, 20, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Android Swotting")
                .putExtra(CalendarContract.Events.DESCRIPTION, "This is when I sit around trying to" +
                        "code in Android")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Boardroom Science Building")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "patrick.harney@gmail.com, patrick.harney@ucdconnect.ie");
        startActivity(intent);
    }

    // Save button listener method
    public boolean addSavedEvent(View view) {
//        String a = getIntent().getExtras().getString("EVENT_ID");

        EventDbOpenHelper db = new EventDbOpenHelper(this);

        String id_check = E.getId();
        List<String> eventList = db.getAllSavedEventsIDS();

        for(String str: eventList) {
            if(str.trim().contains(id_check)) {
                Context context = getApplicationContext();
                CharSequence text = "Event Has Already Been Saved!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
                }
            }
        db.addEvent(id_check, E.getTitle(), E.getDate(), E.getVenueName(), E.getDetails(), E.getImgUrl());

        Context context = getApplicationContext();
        CharSequence text = "Event Saved!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return true;
    }

    public void addDetailedEventtoMap(View view) {
        ArrayList<MapDetails> locations = new ArrayList();
        locations.add(E.returnMapDetails());
        Log.e("location", String.valueOf(E.returnMapDetails()));
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("locations", locations);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check it out";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}