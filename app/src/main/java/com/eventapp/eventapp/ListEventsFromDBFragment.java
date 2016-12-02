package com.eventapp.eventapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzng.fe;

/**
 * Created by Jack on 29/11/2016.
 */

public class ListEventsFromDBFragment  extends Fragment {

    private EventAdapter eventLists;
    private FetchEventInfoFromDB fetch; //AsyncTask used to get info from the EventFul API

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("onCreateView: ", "Called");
        View rootView = inflater.inflate(R.layout.activity_list_events, container, false);

        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.activity_list);

        eventLists = new EventAdapter(getActivity(), R.layout.event_list_entry, new ArrayList<EventListing>());


        // Assign adapter to ListView
        listView.setAdapter(eventLists);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Context context = getActivity();
                EventListing a = eventLists.getItem(position);
                Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
//                                                intent.putExtra("EVENT_ID", a.getId());
                intent.putExtra("EVENT_OBJ", a);
                startActivity(intent);

            }
        });
        return rootView;
    }

    public void getEventInfo(){
        fetch = new FetchEventInfoFromDB();
        fetch.execute();

    }

    @Override
    public void onStart(){
        super.onStart();
        getEventInfo();
    }

    public class FetchEventInfoFromDB extends AsyncTask<Void, Void, EventListing[]> {

        private final String LOG_TAG = ListEventsFragment.FetchEventInfo.class.getSimpleName();

        @Override
        protected void onPreExecute(){
            eventLists.clear();
        }

        @Override
        protected EventListing[] doInBackground(Void... params) {
            //Read in the event objects from the database
            EventDbOpenHelper db = new EventDbOpenHelper(getActivity());

            //Get an ArrayList of Objects from the database
            ArrayList<EventListing> savedEvents = db.getAllSavedEvents();
            EventListing[] events = new EventListing[savedEvents.size()];

            for (int i = 0; i < savedEvents.size(); i++) {
                String url = savedEvents.get(i).getImgUrl();
                savedEvents.get(i).setBitmapFromURL(url, getResources());
                savedEvents.get(i).setDateObject();
                events[i] = savedEvents.get(i);
            }

            return events;
        }

        @Override
        protected void onPostExecute(EventListing[] results) {
            //For each object in the array, add them ot eventLists

            for (int i = 0; i < results.length; i++) {
                eventLists.add(results[i]);
            }
        }
    }
}
