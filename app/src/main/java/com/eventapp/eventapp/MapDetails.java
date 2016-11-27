package com.eventapp.eventapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.Serializable;
import java.net.HttpURLConnection;

public class MapDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String snippet;
    private String date;
    private double lng;
    private double lat;
    private String img_url;
    private String venue;
    private String id;

    public MapDetails(String title, String snippet, String date, double lng, double lat, String img_url, String venue, String id) {
        this.title = title;
        this.snippet = snippet;
        this.date = date;
        this.lng = lng;
        this.lat = lat;
        this.img_url = img_url;
        this.venue = venue;
        this.id = id;
    }

    public String getTitle() { return title; }

    public String getSnippet() {
        return snippet;
    }

    public String getDate() { return date; }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getImg_url() { return img_url;}

    public String getVenue() { return venue;}

    public String getId() { return id; }

}
