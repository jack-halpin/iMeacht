package com.eventapp.eventapp;

import android.animation.LayoutTransition;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

        private GoogleMap mMap;
        private MapView mMapView;
        private ArrayList<MapDetails> locations;

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
                LatLng latlngs;
                latlngs = new LatLng(0, 0);
                for (MapDetails loc : locations) {
                        latlngs = new LatLng(loc.getLat(), loc.getLng());
                        mMap.addMarker(new MarkerOptions().position(latlngs)
                        .title(loc.getTitle()).snippet(loc.getSnippet()));
                }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlngs).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
}