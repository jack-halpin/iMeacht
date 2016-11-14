package com.eventapp.eventapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by devcon90 on 11/14/16.
 */

public class EventDbOpenHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "saved_events";
    final static String ACT_NAME = "name";
    final static String _ID = "_id";
    final static String [] columns = { _ID, ACT_NAME };

    final private static String CREATE_CMD =

        "CREATE TABLE saved_events (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACT_NAME + " TEXT NOT NULL)";

    final private static String NAME = "event_db";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public EventDbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_CMD); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EventDbOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void deleteDatabase() { mContext.deleteDatabase(NAME); }
}
