package com.eventapp.eventapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.name;

/**
 * Created by Jack on 07/11/2016.
 */

public class EventListing {
    //A really simple class for holding information related to an event that has been read in from
    //eventful API
    private String title;
    private String img_url;
    private Bitmap img;
    private String id;
    private String date;
    private String detail;
    private String venueName;
    private int lat;
    private int lng;


    public EventListing(){

    }

    public MapDetails returnMapDetails(){
        return new MapDetails(this.title, this.detail, this.lng, this.lat);
    }
    
    public String getDescription(){
        return this.detail;
    }
    public void setEventInfo(String title, String img_url, String date, String detail, String venue, int lat, int lng){
        this.title = title;
        this.img_url = img_url;
        this.date = date;
        this.detail = detail;
        this.venueName = venue;
        this.lat = lat;
        this.lng = lng;
        setBitmapFromURL(img_url);
    }

    public EventListing(String title){
        this.title = title;
    }
    public EventListing(String title, Bitmap img){
        this.title = title;
        this.img = img;
    }

    public Bitmap getImage(){
        return this.img;
    }

    public void setId(String eventId){
        this.id = eventId;
    }

    public String getId(){
        return this.id;
    }

    //Taken from http://stackoverflow.com/questions/18953632/how-to-set-image-from-url-for-imageview
    public void setBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            this.img = myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
        }
    }

    public String getTitle(){
        return this.title;
    }
}
