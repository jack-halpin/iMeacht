package com.eventapp.eventapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

        private GoogleMap mMap;
        private MapView mMapView;
        private ArrayList<MapEventListing> locations;
        private ArrayList<EventListing> eventLocations;
        private Map<String, EventListing> markerMap;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                // Inflate layout
                View view = inflater.inflate(R.layout.activity_maps, container, false);
                // Fetch locations from bundle that would have been passed by MapActivity
                locations = (ArrayList<MapEventListing>) getArguments().getSerializable("locations");
                // Get a mapview from the map_placeholder found in activity_maps.xml and create it
                mMapView = (MapView) view.findViewById(R.id.map_placeholder);
                mMapView.onCreate(savedInstanceState);

                mMapView.onResume();
                MapsInitializer.initialize(getActivity().getApplicationContext());
                // Call getMapAsync on fragment to async task the loading of the map
                mMapView.getMapAsync(this);

                return view;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
                // When map is ready, perform checks on permission - set location if enabled
                mMap = googleMap;
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                }
                // Create new hashmap for markers
                markerMap = new HashMap<>();
                // Call async task to populate map with markers - it is done this way so we can fetch
                // Images if the user clicks on a map marker to go to detailed event page
                findDetails find = new findDetails(mMap, markerMap);
                find.execute();
                // Set an onclick event to call new activity of going to DetailedEventActivity for that marker
                mMap.setOnInfoWindowClickListener(
                        new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                        String markerTitle = marker.getTitle();
                                        // Fetch the event from the markerMap hashmap
                                        EventListing event = markerMap.get(markerTitle);
                                        Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
                                        intent.putExtra("EVENT_OBJ", event);
                                        startActivity(intent);
                                }
                        }
                );
                // Zoom in on the first location in the list
                LatLng latlngs = new LatLng(locations.get(0).getLat(), locations.get(0).getLng());
                CameraUpdate loc = CameraUpdateFactory.newLatLngZoom(latlngs, 13);
                mMap.animateCamera(loc);
        }

        public class findDetails extends AsyncTask<Void, EventListing, Boolean> {

                private final GoogleMap gMap;
                private Map<String, EventListing> markerMap;
                // Create instance of gMap and markerMap
                public findDetails(GoogleMap gmap, Map<String, EventListing> mMap) {
                        this.gMap = gmap;
                        this.markerMap = mMap;
                }
                // drawMarker populates the map with a single marker
                private void drawMarker(GoogleMap gmap, EventListing event) {
                        // Fetch latlngs
                        LatLng latlngs = new LatLng(event.getLat(), event.getLng());
                        // Create MarketOptions with details of title/date/latlngs
                        MarkerOptions markOp = new MarkerOptions();
                        markOp.position(latlngs)
                                .title(event.getTitle())
                                .snippet(event.getDate());
                        // Add the marker and add it to the markerMap hashmap to aide with directing to DetailedEventActivity
                        gmap.addMarker(markOp);
                        markerMap.put(event.getTitle(), event);

                }

                @Override
                protected Boolean doInBackground(Void... params) {
                        // For each location in MapEventListing, create an EventListing object and fill it
                        for (MapEventListing loc : locations) {
                                EventListing event = new EventListing();
                                event.setEventInfo(loc.getTitle(), loc.getImg_url(), loc.getDate(), loc.getAllDay(), loc.getSnippet(), loc.getVenue(), loc.getVenueAdd(), loc.getLat(), loc.getLng(), loc.getId(), loc.getUrl(), loc.getName(), loc.getEndTime());
                                // Use setBitmapFromUrl to get the image in case of user clicking on makrer
                                event.setBitmapFromURL(event.getImgUrl(), getResources());
                                // Publish progress to do this one at a time
                                publishProgress(event);
                        }
                        // Always return true
                        return true;
                }

                @Override
                protected void onProgressUpdate(EventListing... event) {
                        // On each progress update, call drawMarker to add the marker to the map
                        drawMarker(this.gMap, event[0]);
                }

        }


}