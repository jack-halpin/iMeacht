package com.eventapp.eventapp;

import java.io.Serializable;

public class MapDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String snippet;
    private int lng;
    private int lat;

    public MapDetails(String title, String snippet, int lng, int lat) {
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

    public int getLng() {
        return lng;
    }

    public int getLat() {
        return lat;
    }

}
