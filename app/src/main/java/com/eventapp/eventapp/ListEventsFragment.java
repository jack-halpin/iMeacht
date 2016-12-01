package com.eventapp.eventapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class ListEventsFragment extends Fragment {

    private EventAdapter eventLists; //Adapter used for occupying fragment Listview
    private FetchEventInfo fetch; //AsyncTask used to get info from the EventFul API

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override public void onResume(){
        super.onResume();
    }

    //This method will access the apps shared preferences and use them to return
    //the url segment that is used to filter event categories based on users preferences
    public String getPrefString() {
        //Access the shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> selected = sharedPref.getStringSet(getResources().getString(R.string.pref_persistent_storage), null);
        String location = sharedPref.getString("location", null);
        if (selected != null) {
            //Add the list of preferences onto the url segment
            int size = selected.size();
            int count = 1;
            String user_pref = "&category=";
            for (String item : selected) {
                user_pref += item.toLowerCase();
                if (count < size) {
                    user_pref += ",";
                    count++;
                }
            }
            //Finally add the users location
            user_pref += "&location=" + location;

            return user_pref;
        } else {
            return "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("onCreateView: ", "Called");
        View rootView = inflater.inflate(R.layout.activity_list_events, container, false);

        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.activity_list);

        //Initialize the EventAdapter
        eventLists = new EventAdapter(getActivity(), R.layout.event_list_entry, new ArrayList<EventListing>());


        // Assign adapter to ListView
        listView.setAdapter(eventLists);

        //When a user clicks on an event in the listview we want to direct them to the DetailEventActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                Context context = getActivity();
                                                EventListing a = eventLists.getItem(position);
                                                Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
                                                intent.putExtra("EVENT_OBJ", a);
                                                startActivity(intent);

                                            }
        });
        return rootView;
    }



    //This method is used to determine what happens when the MenuItems in the actionbar are preseed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id) {
            case R.id.get_info:
                fetch = new FetchEventInfo();
                fetch.execute();
                return true;
            case R.id.Preferences:
                Intent intent = new Intent(getActivity(), EventPreferencesActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_map_event:
                //Check that the asynctask if finished
                if(fetch.getStatus() == AsyncTask.Status.FINISHED){
                    ArrayList<MapEventListing> locations = new ArrayList();
                    for (int i = 0; i < eventLists.getCount(); i++){
                        locations.add(eventLists.getItem(i).returnMapEventListing());
                    }
                    Context context = getActivity();
                    intent = new Intent(context, MapActivity.class);
                    intent.putExtra("locations", locations);
                    startActivity(intent);
                    break;
                }else{
                    //If it's not finished put up a Toast asking the user to wait until all the events have been loaded
                    Toast toast = Toast.makeText(getActivity(), "Events still loading. Please try again later.", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

        }
        return super.onOptionsItemSelected(item);

    }

    //This method is used to initialize the AsyncTask that will fetch event information from the eventful API
    //If the method is called but the list is already occupied, it will do nothing
    public void getEventInfo(){
        if (eventLists.isEmpty() == true){
            fetch = new FetchEventInfo();
            fetch.execute();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        getEventInfo();
    }


    //The class represents an asynchronous task to be carried out when the activity is loaded
    //In here we must define the URL, perform a JSON request with the parameters and parse it.
    //objects that have been defined in the parent class can be accessed here!
    public class FetchEventInfo extends AsyncTask<Void, Void, EventListing[]> {

        private final String LOG_TAG = FetchEventInfo.class.getSimpleName();


        @Override
        protected void onPreExecute(){
            //If there is currently anything in the evenLists object we want to delete them
            eventLists.clear();

            //Notify the user that the task is loading up the relevant information
            TextView status = (TextView) getActivity().findViewById(R.id.loading_text);
            status.setVisibility(View.VISIBLE);
            status.setText("Loading...");
        }

        @Override
        protected EventListing[] doInBackground(Void... params) {
            //NOTE: This code is based off of the Sunshine Part 3 tutorial

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String eventJsonStr = null;


            //Next need to construct the URL string used to query the API
            try {
                //Define the base url that contains the API key for the application
                String base_url = "http://api.eventful.com/json/events/search?app_key=p3tDfpd3dKGs2HBD&image_sizes=block200";
                String final_url;

                //If the fragments parent activity has been passed a URL segmant then we will use that to search for events
                if (getActivity().getIntent().hasExtra("url")) {
                    final_url = base_url + getActivity().getIntent().getStringExtra("url");

                }
                //Otherwise just use the URL that is generated when using the users preferences
                else {
                    final_url = "http://api.eventful.com/json/events/search?app_key=p3tDfpd3dKGs2HBD&sort_order=popularity&image_sizes=block200" + getPrefString();
                }

                Log.e("URL", final_url);


                Uri builtUri = Uri.parse(final_url);

                URL url = new URL(builtUri.toString());

                // Create the request to Eventful, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                eventJsonStr = readStream(inputStream);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the event data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getEventInfoFromJson(eventJsonStr);
            }
            catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data.
            return null;
        }

        //This method is used to read in the JSON String
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

        public EventListing[] getEventInfoFromJson(String eventInfo) throws JSONException{
            //Create the JSONObject from the string returned from the API query
            JSONObject eventJson = new JSONObject(eventInfo);
            JSONObject eventlistings = eventJson.getJSONObject("events");

            //Get a JSON array of all the events that are listed from the query
            JSONArray events = eventlistings.getJSONArray("event");

            //We're going to parse each event and store the information as an EventListing object
            //So they can be fed into the EventAdapter and displayed on the screen
            EventListing[] resultStrs = new EventListing[events.length()];
            //Iterate through each event in the JSONArray
            for(int i = 0; i < events.length(); i++) {
                //Create a new event object and a new EventListing Object to store the information in
                JSONObject currEvent = events.getJSONObject(i);
                EventListing newEvent = new EventListing();

                //Need to make sure that an image is supplied for the event
                //If no image is supplied a default image will be used
                String img_url;
                if (currEvent.isNull("image")) {
                    img_url = "";
                } else {
                    //Get the url for the image to be displayed
                    img_url = currEvent.getJSONObject("image").getJSONObject("block200").getString("url");
                }

                //Next get all the required event details required from the JSONObject
                String title = currEvent.getString("title");
                Log.e("title", title);
                String date = currEvent.getString("start_time");
                String endTime = currEvent.getString("stop_time");
                String venue = currEvent.getString("venue_name");
                int allDay = currEvent.getInt("all_day");
                double lat = currEvent.getDouble("latitude");
                double lng = currEvent.getDouble("longitude");
                String id = currEvent.getString("id");
                String url = currEvent.getString("url");
                String venueAdd = currEvent.getString("venue_address");

                //Some events do not have a performer field filled in and this field is used
                //When searching for music later on. If the field isn't supplied it will be
                //set to 'Unknown' in the EventListing object
                String name;
                if(currEvent.isNull("performers")){
                    name = "Unknown";
                } else{
                    name = currEvent.getJSONObject("performers").getJSONObject("performer").getString("name");
                }

                //If no description of the event is supplied just set it to "No Information available."
                String description;
                if (currEvent.getString("description") == "null") {
                    description = "No information available.";
                } else {
                    description = android.text.Html.fromHtml(currEvent.getString("description")).toString();
                }

                //Use the gathered variables to set the information for the current EventListing object
                newEvent.setEventInfo(title, img_url, date, allDay, description, venue, venueAdd, lat, lng, id, url, name, endTime);
                newEvent.setBitmapFromURL(img_url, getResources());

                //Add the object to eh array.
                resultStrs[i] = newEvent;
            }
            return resultStrs;
        }



        @Override
        protected void onPostExecute(EventListing[] results) {

            //If results were found
            if (results != null) {
                //Hide the loading text
                TextView screenText = (TextView) getActivity().findViewById(R.id.loading_text);
                screenText.setVisibility(View.GONE);

                //Occupy the EventAdapter with the results from the query
                for (int i = 0; i < results.length; i++) {
                    eventLists.add(results[i]);
                }
            }
            //Else set the loading TextView to display that no results have been found
            else{
                TextView screenText = (TextView) getActivity().findViewById(R.id.loading_text);
                screenText.setText("Sorry your search returned no results.");
            }

        }
    }
}
