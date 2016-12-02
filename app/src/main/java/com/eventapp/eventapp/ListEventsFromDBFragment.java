package com.eventapp.eventapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jack on 29/11/2016.
 */

public class ListEventsFromDBFragment  extends Fragment {

    private EventAdapter eventLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("onCreateView: ", "Called");
        View rootView = inflater.inflate(R.layout.activity_list_events, container, false);

        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.activity_list);

        // Defined Array values to show in ListView


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        eventLists = new EventAdapter(getActivity(), R.layout.event_list_entry, new ArrayList<EventListing>());


        // Assign adapter to ListView
        listView.setAdapter(eventLists);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("Logger:", Integer.toString(position));

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
        if (eventLists.isEmpty() == true){
            Log.e("E", "is empty");
            FetchEventInfoFromDB fetch = new FetchEventInfoFromDB();
            fetch.execute();
        }
        //Here we will create a new async FetchEventInfo class and tell it to do it's stuff
        Log.e("getEventInfo:", "called");

    }

    @Override
    public void onStart(){
        Log.e("onStart", "called");
        super.onStart();
        getEventInfo();
    }

    public class FetchEventInfoFromDB extends AsyncTask<Void, Void, EventListing[]> {

        private final String LOG_TAG = ListEventsFragment.FetchEventInfo.class.getSimpleName();

        @Override
        protected EventListing[] doInBackground(Void... params) {
            //Read in the event objects from the database
            EventDbOpenHelper db = new EventDbOpenHelper(getActivity());

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
