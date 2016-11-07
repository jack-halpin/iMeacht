package com.eventapp.eventapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jack on 07/11/2016.
 */

public class EventListing {
    //A really simple class for holding information related to an event that has been read in from
    //eventful API
    private String title;
    private Bitmap img;

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
