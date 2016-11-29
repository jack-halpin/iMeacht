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

public class EventListing implements Parcelable {
    //A really simple class for holding information related to an event that has been read in from
    //eventful API
    private String title;
    private String img_url;
    private Bitmap img;
    private String id;
    private String date;
    private Calendar eventDate;
    private int dayOfWeek;
    private int month;
    private int dayOfMonth;
    private int year;
    private String detail;
    private String venueName;
    private String url;
    private String nameOfArtist;
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
        nameOfArtist = in.readString();
    }

    public String getDay(){
        switch(this.dayOfWeek){
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default: return "Undefined";
        }
    }

    public int getDayOfMonth(){
        return this.dayOfMonth;
    }

    public int getYear(){
        return this.year;
    }
    public String getMonth(){
        switch(this.month){
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "Undefined";

        }
    }
    public void setDateObject() {
        this.eventDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.eventDate.setTime(sdf.parse(this.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.dayOfWeek = this.eventDate.get(Calendar.DAY_OF_WEEK);
        this.month = this.eventDate.get(Calendar.MONTH);
        this.dayOfMonth = this.eventDate.get(Calendar.DAY_OF_MONTH);
        this.year = this.eventDate.get(Calendar.YEAR);
        Log.e("num", Integer.toString(this.dayOfWeek));
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
        return new MapDetails(this.title, this.detail, this.date, this.lng, this.lat, this.img_url, this.venueName, this.id, this.allDay, this.venueAddress, this.nameOfArtist, this.url);
    }

    public String getDescription(){
        return this.detail;
    }

    public void setEventInfo(String title, String img_url, String date, int allDay, String detail, String venue, String VenueAdd, double lat, double lng, String id, String url, String nameOfArtist){
        this.title = title;
        this.img_url = img_url;
        this.date = date;
        setDateObject();
        this.allDay = allDay;
        this.detail = detail;
        this.venueName = venue;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.url = url;
        this.nameOfArtist = nameOfArtist;
        this.venueAddress = VenueAdd;
        setBitmapFromURL(img_url);

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

    public void setAllDay(String val) {
        if (val == "true") {
            this.allDay = 1;
        }
        else {
            this.allDay = 0;
        }
    }

    public Calendar getStartTime() throws ParseException{
        return this.eventDate;
    }
    public void setImgUrl(String img_url) { this.img_url = img_url; }

    public String getDate() { return this.date; }

    public void setDate(String date) { this.date = date; }

    public String getDetails() { return this.detail; }

    public void setDetails(String detail) { this.detail = detail; }

    public String getVenueName() { return this.venueName; }

    public void setVenueName(String venue) { this.venueName = venue; }

    public String getVenueAddress(){
        return this.venueAddress;
    }

    public void setVenueAddress(String venueAddress) { this.venueAddress = venueAddress; }

    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }

    public Double getLat() { return this.lat; }

    public void setLat(String lat) { this.lat = Double.valueOf(lat); }

    public Double getLng() { return this.lng; }

    public void setLng(String lng) { this.lat = Double.valueOf(lng); }

    public  String getArtistName() { return this.nameOfArtist; }

    public void setArtistName(String artist) { this.nameOfArtist = artist; }

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
        parcel.writeString(nameOfArtist);
    }
}
