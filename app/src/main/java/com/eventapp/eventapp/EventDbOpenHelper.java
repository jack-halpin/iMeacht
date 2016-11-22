package com.eventapp.eventapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by devcon90 on 11/14/16.
 */

public class EventDbOpenHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "saved_events";
    final static String ACT_NAME = "name";
    final static String ACT_ID = "id";
    final static String [] columns = { ACT_ID, ACT_NAME };

    final private static String CREATE_CMD =

        "CREATE TABLE saved_events (" + ACT_ID
            + " TEXT NOT NULL, "
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

    public void addEvent(String id, String name) {
        Log.e("addEventID", id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(ACT_ID, id);
        value.put(ACT_NAME, name);

        db.insert(TABLE_NAME, null, value);

        db.close();
    }

    public List<String> getAllSavedEvents() {
        List<String> savedEvents = new LinkedList<String>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String id = null;
        if (cursor.moveToFirst()) {
            do {
                id = new String(cursor.getString(0));
                savedEvents.add(id);
            } while (cursor.moveToNext());
        }
        Log.e("getAllSavedEvents()", savedEvents.toString());

        return savedEvents;
    }

    void deleteDatabase() { mContext.deleteDatabase(NAME); }
}
