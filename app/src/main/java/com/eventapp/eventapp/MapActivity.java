package com.eventapp.eventapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

    private ArrayList<MapEventListing> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetch intent extras and store them in an arrayList
        locations = (ArrayList<MapEventListing>) getIntent().getExtras().getSerializable("locations");
        setContentView(R.layout.activity_maps);
        // Create fragment of MapsFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MapsFragment mf = new MapsFragment();
        Bundle data = new Bundle();
        // Create a bundle and place the locations into the bundle
        data.putSerializable("locations", locations);
        // Set the arguments and replace map_placeholder with the fragment
        mf.setArguments(data);
        ft.replace(R.id.map_placeholder, mf);
        ft.commit();

        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }




    public boolean onOptionsItemSelected(MenuItem item){
        // Use this to allow the ActionBar in the activity
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void finish() {
        super.finish();
    }
}
