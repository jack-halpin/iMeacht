package com.eventapp.eventapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

    private ArrayList<MapDetails> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<MapDetails> locations = (ArrayList<MapDetails>) getIntent().getExtras().getSerializable("locations");
        Bundle data = new Bundle();
        data.putSerializable("locations", locations);
        MapFragment s = new MapFragment();
        s.setArguments(data);
        setContentView(R.layout.activity_maps);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
