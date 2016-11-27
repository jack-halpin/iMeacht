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
 */

public class EventAdapter extends ArrayAdapter<EventListing> {
    private int layoutResource;
    public EventAdapter(Context context, int layoutResource, List<EventListing> listOfEvents){
        super(context, layoutResource, listOfEvents);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }


        EventListing event = getItem(position);

        if (event != null) {
            ImageView eventThumbnail = (ImageView) view.findViewById(R.id.event_thumb);
            TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
            TextView eventDate = (TextView) view.findViewById(R.id.event_date);
            TextView eventVenue = (TextView) view.findViewById(R.id.event_venue);



            if (eventThumbnail != null) {
                eventThumbnail.setImageBitmap(event.getImage());
            }

            if (eventTitle != null) {
                eventTitle.setText(event.getTitle());
                eventDate.setText(event.getDate());
                eventVenue.setText(event.getVenueName());
            }
        }

        return view;
    }

}
