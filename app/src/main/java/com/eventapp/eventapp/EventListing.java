package com.eventapp.eventapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.name;

/**
 * Created by Jack on 07/11/2016.
 */

public class EventListing implements Parcelable {
    //A really simple class for holding information related to an event that has been read in from
    //eventful API
    private String title;
    private String img_url;
    private Bitmap img;
    private String id;
    private String date;
    private String detail;
    private String venueName;
    private double lat;
    private double lng;


    public EventListing(){

    }

    protected EventListing(Parcel in) {
        title = in.readString();
        img_url = in.readString();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        id = in.readString();
        date = in.readString();
        detail = in.readString();
        venueName = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<EventListing> CREATOR = new Creator<EventListing>() {
        @Override
        public EventListing createFromParcel(Parcel in) {
            return new EventListing(in);
        }

        @Override
        public EventListing[] newArray(int size) {
            return new EventListing[size];
        }
    };

    public MapDetails returnMapDetails(){
        return new MapDetails(this.title, this.detail, this.lng, this.lat);
    }
    
    public String getDescription(){
        return this.detail;
    }
    public void setEventInfo(String title, String img_url, String date, String detail, String venue, double lat, double lng, String id){
        this.title = title;
        this.img_url = img_url;
        this.date = date;
        this.detail = detail;
        this.venueName = venue;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
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

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            this.img = myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
        }
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(img_url);
        parcel.writeParcelable(img, i);
        parcel.writeString(id);
        parcel.writeString(date);
        parcel.writeString(detail);
        parcel.writeString(venueName);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
