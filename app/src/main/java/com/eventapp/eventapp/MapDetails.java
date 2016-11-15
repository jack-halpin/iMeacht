package com.eventapp.eventapp;

import java.io.Serializable;

public class MapDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String snippet;
    private double lng;
    private double lat;

    public MapDetails(String title, String snippet, double lng, double lat) {
        this.title = title;
        this.snippet = snippet;
        this.lng = lng;
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

}
