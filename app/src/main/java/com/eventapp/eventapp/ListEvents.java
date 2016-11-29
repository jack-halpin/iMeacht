package com.eventapp.eventapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import static com.google.android.gms.analytics.internal.zzy.f;
import static com.google.android.gms.analytics.internal.zzy.p;

public class ListEvents extends Fragment {

    //This is declared in the class so it can be accessed by the inner public class
    private EventAdapter eventLists;
    private FetchEventInfo fetch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fetch = new FetchEventInfo();
        Log.e("Prefs", getPrefString());



    }
@Override public void onResume(){
    super.onResume();
    fetch = new FetchEventInfo();
}
    public String getPrefString() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("storedPrefs", Context.MODE_PRIVATE);
        int size = sharedPref.getAll().size();

        String pref = "&keywords=";
        for (int i = 0; i < size; i++) {
            pref += sharedPref.getString("Pref_" + i, "");
            if (i != (size - 1)) {
                pref += "&";
            }
        }
        return pref;
    }




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



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.get_info){

            getEventInfo();
            return true;
        } else if (id == R.id.map_view){
            ArrayList<MapDetails> locations = new ArrayList();
            for (int i = 0; i < eventLists.getCount(); i++){
                locations.add(eventLists.getItem(i).returnMapDetails());
            }
            Context context = getActivity();
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra("locations", locations);
            startActivity(intent);
        } else if(id == R.id.Preferences){
            Intent intent = new Intent(getActivity(), Preferences.class);
            startActivity(intent);
        } else if(id == R.id.saved_events){
            Intent intent = new Intent(getActivity(), SavedEventsActivity.class);
            startActivity(intent);
            return true;
        }
        Log.e("trying", "to do it");
        return super.onOptionsItemSelected(item);

    }

    public void getEventInfo(){
        if (eventLists.isEmpty() == true){
            Log.e("E", "is empty");
            fetch = new FetchEventInfo();
            fetch.execute("test");
        }
        //Here we will create a new async FetchEventInfo class and tell it to do it's stuff
        Log.e("getEventInfo:", "called");

    }

    public EventAdapter getEventLists(){
        return this.eventLists;
    }
    @Override
    public void onStart(){
        Log.e("onStart", "called");
        super.onStart();
        getEventInfo();
    }


    //The class represents an asynchronous task to be carried out when the activity is loaded
    //In here we must define the URL, perform a JSON request with the parameters and parse it.
    //objects that have been defined in the parent class can be accessed here!
    public class FetchEventInfo extends AsyncTask<String, Void, EventListing[]> {

        private final String LOG_TAG = FetchEventInfo.class.getSimpleName();

        @Override
        protected EventListing[] doInBackground(String... params) {


            // Log.d("ListEvents", "Testing log message! First parameter listed is: " + params[0].toString());

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String eventJsonStr = null;




            try {

                //Querying test URL:

                // Original URL
                final String testurl = "http://api.eventful.com/json/events/search?app_key=p3tDfpd3dKGs2HBD&sort_order=popularity&image_sizes=block200&location=Dublin";

//                final String testurl = "http://api.eventful.com/json/events/search?app_key=p3tDfpd3dKGs2HBD&sort_order=popularity&image_sizes=block200&location=Dublin" + prefString.getPrefString();


                Uri builtUri = Uri.parse(testurl);

                URL url = new URL(builtUri.toString());

                //Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                //URL url = new URL("http://http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&mode=json&units=metric&ctn=7");

                // Create the request to Eventful, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                eventJsonStr = readStream(inputStream);

                //read


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
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


            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }


        public EventListing[] getEventInfoFromJson(String eventInfo) throws JSONException{
            JSONObject eventJson = new JSONObject(eventInfo);
            JSONObject eventlistings = eventJson.getJSONObject("events");
            JSONArray events = eventlistings.getJSONArray("event");
            EventListing[] resultStrs = new EventListing[events.length()];

            for(int i = 0; i < events.length(); i++) {

                String img_url;
                JSONObject currEvent = events.getJSONObject(i);
                EventListing newEvent = new EventListing();

                if (currEvent.isNull("image")){
                    //Log.v(LOG_TAG, "is null");
                    img_url = "";
                }
                else{
                    //Get the url for the image to be displayed
                    img_url = currEvent.getJSONObject("image").getJSONObject("block200").getString("url");

                    //Next get the event date, venue, and details
                    String title = currEvent.getString("title");
                    String date = currEvent.getString("start_time");
                    String venue = currEvent.getString("venue_name");
                    int allDay = currEvent.getInt("all_day");
                    double lat = currEvent.getDouble("latitude");
                    double lng = currEvent.getDouble("longitude");
                    String id = currEvent.getString("id");
                    String url = currEvent.getString("url");
                    String venueAdd = currEvent.getString("venue_address");
                    String name = currEvent.getJSONObject("performers").getJSONObject("performer").getString("name");
                    String description;
                    if (currEvent.getString("description") == "null") {
                        description = "No information available.";
                    }
                    else{
                        description = android.text.Html.fromHtml(currEvent.getString("description")).toString();
                    }
                    newEvent.setEventInfo(title, img_url, date, allDay, description, venue, venueAdd, lat, lng, id, url, name);


                }
                resultStrs[i] = newEvent;
            }
            return resultStrs;
        }

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

        @Override
        protected void onPostExecute(EventListing[] results) {


            if (results != null) {
                eventLists.clear();
                //mForecastAdapter.addAll(result);
                for (int i = 0; i < results.length; i++) {
                    eventLists.add(results[i]);
                }
                //Log.e(LOG_TAG, "First Item: " + eventLists.getItem(0));
            }

        }
    }
}
