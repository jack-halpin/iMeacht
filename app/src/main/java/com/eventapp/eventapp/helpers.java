package com.eventapp.eventapp;

import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by devin on 29/11/16.
 */

public class helpers {

    public static void tutorial(final String[] tutStringArray, final RelativeLayout tutorialLayout, final TextView tutorialText, final Context context) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        // Fetch tutorial layout and make it visible
        if (tutStringArray.length > 0) {
            tutorialLayout.setVisibility(View.VISIBLE);
            // Fetch our text layout and make it visible
            tutorialText.setText(tutStringArray[0]);
            tutorialText.setVisibility(View.VISIBLE);
            // Set it to be clickable so clicks do not go past the layout
            tutorialLayout.setClickable(true);
            // Set the click event
            tutorialLayout.setOnClickListener(new View.OnClickListener() {
                private int count = 1;
                private double lastClickTime = 0;

                @Override
                public void onClick(View v) {
                    // Check if the elapsed system time minus the last click is greater than 1000ms
                    if (SystemClock.elapsedRealtime() - lastClickTime > 500) {
                        // If it is, reassign the lastClickTime
                        lastClickTime = SystemClock.elapsedRealtime();
                        // If the count is less than the length fo the array, set the text to count index of array
                        if (count < tutStringArray.length) {
                            tutorialText.setText(tutStringArray[count]);
                            // Increment count
                            count++;
                        } else {
                            // If count is equal to or greater than size of string array, make tut invisible
                            tutorialText.setVisibility(View.INVISIBLE);
                            tutorialLayout.setVisibility(View.INVISIBLE);
                            // Set clickable to false so clicks can pass through again.
                            tutorialLayout.setClickable(false);
                            // If it is the first time boot, bring user to customize events
                            if (pref.getBoolean("firstTimeBoot", true)) {
                                Intent intent = new Intent(context, EventPreferencesActivity.class);
                                context.startActivity(intent);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("firstTimeBoot", false);
                                editor.apply();
                            }
                        }
                    }
                }
            });
        }
    }
}
