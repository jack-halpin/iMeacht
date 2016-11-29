package com.eventapp.eventapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by devcon90 on 11/14/16.
 */

public class EventDbOpenHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "saved_events";
    final static String ACT_ID = "id";
    final static String ACT_NAME = "name";
    final static String ACT_DATE = "date";
    final static String ACT_VENUE = "venue";
    final static String ACT_DETAILS = "details";
    final static String ACT_URL = "url";
    final static String [] columns = { ACT_ID, ACT_NAME, ACT_DATE, ACT_VENUE, ACT_DETAILS, ACT_URL };

    final private static String CREATE_CMD =

        "CREATE TABLE saved_events (" + ACT_ID
            + " TEXT NOT NULL, "
            + ACT_NAME + " TEXT NOT NULL, "
            + ACT_DATE + " TEXT NOT NULL, "
            + ACT_VENUE + " TEXT NOT NULL, "
            + ACT_DETAILS + " TEXT NOT NULL, "
            + ACT_URL + " TEXT NOT NULL)";

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

    public void addEvent(String id, String name, String date, String venue, String details, String url) {
        Log.e("addEventID", id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(ACT_ID, id);
        value.put(ACT_NAME, name);
        value.put(ACT_DATE, date);
        value.put(ACT_VENUE, venue);
        value.put(ACT_DETAILS, details);
        value.put(ACT_URL, url);

        db.insert(TABLE_NAME, null, value);

        db.close();
    }

    public ArrayList<EventListing> getAllSavedEvents() {

        ArrayList<EventListing> list = new ArrayList<EventListing>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                EventListing e = new EventListing();
                e.setId(cursor.getString(0));
                e.setTitle(cursor.getString(1));
                e.setDate(cursor.getString(2));
                e.setVenueName(cursor.getString(3));
                e.setDetails(cursor.getString(4));
                e.setImgUrl(cursor.getString(5));
                list.add(e);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.e("getAllSavedEvents()", list.toString());

        return list;
    }

    public List<String> getAllSavedEventsIDS() {
        List<String> savedEventIDS = new LinkedList<String>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String id = null;
        if (cursor.moveToFirst()) {
            do {
                id = new String(cursor.getString(0));
                savedEventIDS.add(id);
            } while (cursor.moveToNext());
        }
        Log.e("getAllSavedEventIDS()", savedEventIDS.toString());

        return savedEventIDS;
    }

    void deleteDatabase() { mContext.deleteDatabase(NAME); }
}
