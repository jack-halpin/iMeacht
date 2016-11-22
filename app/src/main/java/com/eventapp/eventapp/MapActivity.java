package com.eventapp.eventapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.app.FragmentManager;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

    private ArrayList<MapDetails> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<MapDetails> locations = (ArrayList<MapDetails>) getIntent().getExtras().getSerializable("locations");
        setContentView(R.layout.activity_maps);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MapsFragment mf = new MapsFragment();
        Bundle data = new Bundle();
        data.putSerializable("locations", locations);
        mf.setArguments(data);
        ft.replace(R.id.map_placeholder, mf);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listings_menu, menu);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
    }
}
