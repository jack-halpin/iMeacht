package com.eventapp.eventapp;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.Map;
import java.util.HashMap;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

        private GoogleMap mMap;
        private MapView mMapView;
        private ArrayList<MapDetails> locations;
        private ArrayList<EventListing> eventLocations;
        private Map<String, EventListing> markerMap;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.activity_maps, container, false);
                locations = (ArrayList<MapDetails>) getArguments().getSerializable("locations");
                mMapView = (MapView) view.findViewById(R.id.map_placeholder);
                mMapView.onCreate(savedInstanceState);

                mMapView.onResume();
                MapsInitializer.initialize(getActivity().getApplicationContext());

                mMapView.getMapAsync(this);

                return view;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                }
                markerMap = new HashMap<>();
                findDetails find = new findDetails(mMap, markerMap);
                find.execute();
                mMap.setOnInfoWindowClickListener(
                        new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                        String markerTitle = marker.getTitle();
                                        EventListing event = markerMap.get(markerTitle);
                                        Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
                                        intent.putExtra("EVENT_OBJ", event);
                                        startActivity(intent);
                                }
                        }
                );
                LatLng latlngs = new LatLng(locations.get(0).getLat(), locations.get(0).getLng());
                CameraUpdate loc = CameraUpdateFactory.newLatLngZoom(latlngs, 13);
                mMap.animateCamera(loc);
        }

        public class findDetails extends AsyncTask<Void, EventListing, Boolean> {

                private final GoogleMap gMap;
                private Map<String, EventListing> markerMap;

                public findDetails(GoogleMap gmap, Map<String, EventListing> mMap) {
                        this.gMap = gmap;
                        this.markerMap = mMap;
                }

                private void drawMarker(GoogleMap gmap, EventListing event) {
                        LatLng latlngs = new LatLng(event.getLat(), event.getLng());
                        MarkerOptions markOp = new MarkerOptions();
                        markOp.position(latlngs)
                                .title(event.getTitle())
                                .snippet(event.getDate());

                        gmap.addMarker(markOp);
                        markerMap.put(event.getTitle(), event);

                }

                @Override
                protected Boolean doInBackground(Void... params) {

                        for (MapDetails loc : locations) {
                                EventListing event = new EventListing();
                                event.setEventInfo(loc.getTitle(), loc.getImg_url(), loc.getDate(), loc.getAllDay(), loc.getSnippet(), loc.getVenue(), loc.getVenueAdd(), loc.getLat(), loc.getLng(), loc.getId(), loc.getUrl(), loc.getName(), loc.getEndTime());
                                publishProgress(event);
                        }

                        return true;
                }

                @Override
                protected void onProgressUpdate(EventListing... event) {
                        drawMarker(this.gMap, event[0]);
                }

        }


}