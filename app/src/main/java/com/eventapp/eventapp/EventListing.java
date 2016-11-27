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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jack on 07/11/2016.
 */

import android.content.Context;

public class EventListing implements Parcelable {
    //A really simple class for holding information related to an event that has been read in from
    //eventful API
    private String title;
    private String img_url;
    private Bitmap img;
    private String id;
    private String date;
    private String dayOfWeek;
    private String month;
    private String dayOfMonth;
    private String detail;
    private String venueName;
    private String url;
    private String name;
    private String venueAddress;
    private int allDay;
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
        allDay = in.readInt();
        url = in.readString();
        name = in.readString();
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
        return new MapDetails(this.title, this.detail, this.date, this.lng, this.lat, this.img_url, this.venueName, this.id, this.allDay, this.venueAddress, this.name, this.url);
    }
    
    public String getDescription(){
        return this.detail;
    }

    public void setEventInfo(String title, String img_url, String date, int allDay, String detail, String venue, String VenueAdd, double lat, double lng, String id, String url, String name){
        this.title = title;
        this.img_url = img_url;
        this.date = date;
        this.allDay = allDay;
        this.detail = detail;
        this.venueName = venue;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.url = url;
        this.name = name;
        this.venueAddress = VenueAdd;
        setBitmapFromURL(img_url);
    }

    public String getVenueAddress(){
        return this.venueAddress;
    }
    public EventListing(String title){
        this.title = title;
    }
    public EventListing(String title, Bitmap img){
        this.title = title;
        this.img = img;
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

    public void setDateParams(){
        
    }


    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title) { this.title = title; }

    public Bitmap getImage(){
        return this.img;
    }

    public void setImage(Bitmap img) {this.img = img; }

    public String getId(){
        return this.id;
    }

    public void setId(String id) { this.id = id; }

    public String getImgUrl() { return this.img_url; }

    public boolean getAllDay(){
        return (this.allDay > 0);
    }

    public Calendar getStartTime() throws ParseException{
        Calendar startTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime.setTime(sdf.parse(this.date));
        return startTime;
    }
    public void setImgUrl(String img_url) { this.img_url = img_url; }

    public String getDate() { return this.date; }

    public void setDate(String date) { this.date = date; }

    public String getDetails() { return this.detail; }

    public void setDetails(String detail) { this.detail = detail; }

    public String getVenueName() { return this.venueName; }

    public String getUrl() { return this.url; }

    public void setVenueName(String venue) { this.venueName = venue; }

    public Double getLat() { return this.lat; }

    public Double getLng() { return this.lng; }

    public  String getArtistName() { return this.name; }

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
        parcel.writeInt(allDay);
        parcel.writeString(url);
        parcel.writeString(name);
    }
}
