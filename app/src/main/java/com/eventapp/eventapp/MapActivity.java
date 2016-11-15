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
        Bundle data = new Bundle();
        MapDetails d = new MapDetails("d", "d", 1, 1);
        data.putSerializable("locations", d);
        MapFragment s = new MapFragment();
        s.setArguments(data);
        setContentView(R.layout.activity_maps);
    }


}
