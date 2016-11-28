package com.eventapp.eventapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("firstTimeBoot", true)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("firstTime", true);
            startActivity(intent);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTimeBoot", false);
            editor.apply();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("firstTime", false);
            startActivity(intent);
        }
        finish();

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }
}
