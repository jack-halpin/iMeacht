package com.eventapp.eventapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class DetailedEventActivity extends AppCompatActivity {

    private EventListing E;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String a = getIntent().getExtras().getString("EVENT_ID");
        Log.e("object title", a);
        setContentView(R.layout.activity_detailed_event);

        TextView v = (TextView) findViewById(R.id.textView2);
        v.setText(a);
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

    /** Save button listener method*/
    public void addSavedEvent(View view) {
        String a = getIntent().getExtras().getString("EVENT_ID");

        EventDbOpenHelper db = new EventDbOpenHelper(this);

        db.addEventID(a);
    }

}