package com.eventapp.eventapp;

import android.content.res.Resources;
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


//This class is used to store each event that is used by the ListEventFragment. 
// It also parcelable in order for an object to be passed on to further activities
//Such as MapActivity or DetailedEventActivity
public class EventListing implements Parcelable {
    
    private String title;                   //The title of the event
    private String img_url;                 //The URL of the event image
    private Bitmap img;                     //The event image stored as a bitmap
    private String id;                      //The event ID as supplied by the eventful API
    private String date;                    //The date as a string returned in the format: yyyy-MM-dd HH:mm:ss
    private Calendar eventCalendarObject;   //A calender Object that is used to retrieve the specific day, week, and month of the event
    private String endTime;                 //The time the event is supposed to finish
    private int dayOfWeek;                  //The day of the week as an integer from 1-7
    private int month;                      //The month represented as an integer from 1-12
    private int dayOfMonth;                 //The day of the month as an integer from 1-31
    private int year;                       //The year the event is taking place
    private String detail;                  //The description of the event as provided by the API
    private String venueName;               //The name of the venue
    private String url;                     //The url to the event listing on the eventful website
    private String nameOfArtist;            //Name of the performing artist
    private String venueAddress;            //Address of the venue
    private int allDay;                     // Whether the event is an all day event or not (0 is not all day)
    private double lat;                     //Latitude
    private double lng;                     //Logitude

    //Default Constructor
    public EventListing(){
    }

    //This method is used to set all the event variables
    //When they have been recieved from the API
    public void setEventInfo(String title, String img_url, String date, int allDay, String detail, String venue, String VenueAdd, double lat, double lng, String id, String url, String nameOfArtist, String endTime){
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
        this.endTime = endTime;
    }

    //This method uses the class calender object to set the
    //Date parameters for the event
    public void setDateObject() {

        //Set the instance of the calender and set the time using
        //formatted date
        this.eventCalendarObject = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.eventCalendarObject.setTime(sdf.parse(this.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Set the object date parameters using the calender object
        this.dayOfWeek = this.eventCalendarObject.get(Calendar.DAY_OF_WEEK);
        this.month = this.eventCalendarObject.get(Calendar.MONTH);
        this.dayOfMonth = this.eventCalendarObject.get(Calendar.DAY_OF_MONTH);
        this.year = this.eventCalendarObject.get(Calendar.YEAR);
    }

    //This method is used to return the string representation
    //of the classes dayOfWeek variable.
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

    //This method is used to return the string representation
    //of the classes month variable.
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

    //Taken from http://stackoverflow.com/questions/18953632/how-to-set-image-from-url-for-imageview
    //This function is used to use an events image url and store it as a bitmap
    public void setBitmapFromURL(String src, Resources res) {
        try {
            //If the image_url does not exist then just set the image to the default no_iamge logo
            if (src.equals("")){
                Bitmap myBitmap = BitmapFactory.decodeResource(res, R.drawable.no_image);
            }
            else {
                //Construct the URL and make a HTTP connection.
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                //Get the inputstream and store it as a bitmap
                InputStream input = connection.getInputStream();
                this.img = BitmapFactory.decodeStream(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method is used to return a MapEventListing object. This object
    //is similar to a EventListing object but does not contain as many parameters.
    //The purpose for this is to easily pass event information to the map activity
    public MapEventListing returnMapEventListing(){
        return new MapEventListing(this.title, this.detail, this.date, this.lng, this.lat, this.img_url, this.venueName, this.id, this.allDay, this.venueAddress, this.nameOfArtist, this.url, this.endTime);
    }

    // Creates a Calendar object representing the start time of an event
    public Calendar getStartTime() throws ParseException{
        Calendar startTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime.setTime(sdf.parse(this.date));
        return startTime;
    }

    // Returns either the event end time or the event start time + 3hrs (in ms) if no end time is given
    public long getEndTime() throws ParseException{
        if (!this.endTime.equals("null")) {
            Calendar eventEndTime = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            eventEndTime.setTime(sdf.parse(this.endTime));
            return eventEndTime.getTimeInMillis();
        }
        else {
            return (this.getStartTime().getTimeInMillis() + (3*60*60*1000));
        }
    }

    //NOTE: All the following methods are getter and setter methods for the class variables.
    public String getDescription(){
        return this.detail;
    }

    public String getTitle(){ return this.title; }

    public void setTitle(String title) { this.title = title; }

    public Bitmap getImage(){ return this.img; }

    public void setImage(Bitmap img) {this.img = img; }

    public String getId(){ return this.id; }

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

    public int getDayOfMonth(){ return this.dayOfMonth; }

    public int getYear(){ return this.year; }

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

    public void setLng(String lng) { this.lng = Double.valueOf(lng); }

    public  String getArtistName() { return this.nameOfArtist; }

    public void setArtistName(String artist) { this.nameOfArtist = artist; }


    //These functions are included to make the class Parcelable which allows
    //it to be passed between activities
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
        parcel.writeString(endTime);
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
        endTime = in.readString();
    }
}
