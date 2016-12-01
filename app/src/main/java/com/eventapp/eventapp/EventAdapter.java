package com.eventapp.eventapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jack on 07/11/2016.
 *
 * This is a custom adapter that is used to display event information
 * as a list
 */

public class EventAdapter extends ArrayAdapter<EventListing> {
    private int layoutResource;

    //Constructor
    public EventAdapter(Context context, int layoutResource, List<EventListing> listOfEvents){
        super(context, layoutResource, listOfEvents);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        //If the view is null then inflate it
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        //For each item in the listOfEvents object set the content of the view
        EventListing event = getItem(position);

        if (event != null) {
            //Get the various views from the layout
            ImageView eventThumbnail = (ImageView) view.findViewById(R.id.event_thumb);
            TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
            TextView eventDate = (TextView) view.findViewById(R.id.event_date);
            TextView eventVenue = (TextView) view.findViewById(R.id.event_venue);

            //Populate them based on information stored in the currevent EventListing object
            if (eventThumbnail != null) {
                if (event.getImgUrl().equals("")){
                    eventThumbnail.setImageResource(R.drawable.no_image);
                }
                else {
                    eventThumbnail.setImageBitmap(event.getImage());
                }
            }

            if (eventTitle != null) {
                eventTitle.setText(event.getTitle());

                eventDate.setText(event.getDay() + ", " + event.getMonth() + " " + event.getDayOfMonth() + " " + event.getYear());
                eventVenue.setText(event.getVenueName());
            }
        }

        return view;
    }

}
