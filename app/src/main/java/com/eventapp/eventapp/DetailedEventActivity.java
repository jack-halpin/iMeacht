package com.eventapp.eventapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DetailedEventActivity extends AppCompatActivity {

    private EventListing E;
    private ShareActionProvider mShareActionProvider;
    private RelativeLayout tutorialLayout;
    private TextView tutorialText;
    private SharedPreferences pref;
    private String[] event_tutorial_array;

    // Method to create an options menu in action bar
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

        E = getIntent().getExtras().getParcelable("EVENT_OBJ");

        setContentView(R.layout.activity_detailed_event);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        event_tutorial_array = getResources().getStringArray(R.array.event_tutorial_array);
        tutorialText = (TextView) findViewById(R.id.event_text_tutorial);
        tutorialLayout = (RelativeLayout) findViewById(R.id.event_tutorial_layout);

        ImageView img = (ImageView) findViewById(R.id.imageViewDetailed);
        img.setImageBitmap(E.getImage());

        TextView v2 = (TextView) findViewById(R.id.textView2);
        v2.setText(E.getTitle());
        TextView v3 = (TextView) findViewById(R.id.textView3);
        E.setDateObject();
        v3.setText(E.getDay() + ", " + E.getMonth() + " " + E.getDayOfMonth() + " " + E.getYear());
        TextView v4 = (TextView) findViewById(R.id.textView4);
        v4.setText(E.getVenueName());
        TextView v5 = (TextView) findViewById(R.id.textView5);
        v5.setText(E.getDetails());

        EventDbOpenHelper db = new EventDbOpenHelper(this);
        String id_check = E.getId();
        List<String> eventList = db.getAllSavedEventsIDS();
        for(String str: eventList) {
            if(str.trim().contains(id_check)) {
                Button button = (Button) findViewById(R.id.save_button);
                button.setText(getResources().getString(R.string.deleteevent));
            }
        }

        if (pref.getBoolean("firstTimeEvent", true)) {
            helpers.tutorial(event_tutorial_array, tutorialLayout, tutorialText, this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTimeEvent", false);
            editor.apply();
        }




        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public void finish() {
        super.finish();
    }


    /** Called when the user clicks the Calenadr button 
        Adds an event to the phone's default calenadr app */
    public void addCalendar(View view) throws ParseException{
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, E.getStartTime().getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, E.getEndTime())
                .putExtra(CalendarContract.Events.ALL_DAY, E.getAllDay())
                .putExtra(CalendarContract.Events.TITLE, E.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, E.getDetails())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, E.getVenueName())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    // Save button listener method
    public boolean addSavedEvent(View view) {
        // method adds the event to the database
        Button button = (Button) findViewById(R.id.save_button);
        EventDbOpenHelper db = new EventDbOpenHelper(this);

        String id_check = E.getId();
        List<String> eventList = db.getAllSavedEventsIDS();

        for(String str: eventList) {
            if(str.trim().contains(id_check)) {
                db.deleteEventById(id_check);
                Context context = getApplicationContext();
                CharSequence text = "Event Deleted from Favourites!";

                button.setText(getResources().getString(R.string.saveevent));

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                return false;
                }
            }
        db.addEvent(id_check, E.getTitle(), E.getImgUrl(), E.getDate(), E.getEndTimes(), E.getDetails(), E.getVenueName(), E.getUrl(), E.getArtistName(), E.getVenueAddress(), E.getAllDay(), E.getLat(), E.getLng());

        Context context = getApplicationContext();
        CharSequence text = "Event Saved!";
        button.setText(getResources().getString(R.string.deleteevent));
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return true;
    }

    public void addDetailedEventtoMap(View view) {
        ArrayList<MapEventListing> locations = new ArrayList();
        locations.add(E.returnMapEventListing());
        Log.e("location", String.valueOf(E.returnMapEventListing()));
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("locations", locations);
        startActivity(intent);
    }

    /** Checks which item from action bar menu has been selected
        Chooses an appropriate reaction in each caae */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_item_share:
                // Creates intent to share an event with messaging/email applications
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, E.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, E.getUrl());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.menu_item_play:
                playSearchArtist();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /** Method to search media applications for a performer 
        Only launched if an application can respond to the intent */
    public void playSearchArtist() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
        intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, E.getArtistName());
        intent.putExtra(SearchManager.QUERY, E.getArtistName());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "No media apps installed!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
