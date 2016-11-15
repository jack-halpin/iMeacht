package com.eventapp.eventapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MapDetails> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = (ArrayList<MapDetails>) getIntent().getExtras().getSerializable("map");
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (MapDetails loc : locations) {
            LatLng latlngs = new LatLng(loc.getLat(), loc.getLng());
            mMap.addMarker(new MarkerOptions().position(latlngs)
                .title(loc.getTitle()).snippet(loc.getSnippet()));
        }
        LatLng latlngs = new LatLng(0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlngs));
    }
}