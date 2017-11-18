package com1032.cw2.ew00162.ew00162_assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class MarkerDB extends SQLiteOpenHelper {

    //Version of the database
    private static final int DB_VERSION = 1;

    //Name of the database
    private static final String DATABASE_NAME = "map";

    //Items table name
    private static final String TABLE_MARKERS = "markers";

    /**
     * Constructor for the database
     * @param context
     * @param factory
     */
    MarkerDB(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DB_VERSION);
    }

    /**
     * Called when first created
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("testing", "onCreate DATABASE");
        createTable(db);
    }

    /**
     * Create the table of markers in the database
     * @param db
     */
    private void createTable(SQLiteDatabase db) {
        String createSQL =
                "CREATE TABLE " + "markers" + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title STRING, " + "latitude STRING, " + "longitude STRING);";
        db.execSQL(createSQL);
    }

    /**
     * Upgrading the database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropSQL = "DROP TABLE IF EXISTS " + "markers" + ";";
        db.execSQL(dropSQL);

        //Create new table
        createTable(db);
        Log.d("testing", "onUpgrade");
    }


    //Marker table column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_LONGITUDE,KEY_LATITUDE};

    /**
     * Clear all the data in the table - drop the table
     */
    public void clearData() {
        SQLiteDatabase dbTasks = this.getWritableDatabase();
        String dropSQL = "DROP TABLE IF EXISTS " + TABLE_MARKERS + ";";
        dbTasks.execSQL(dropSQL);
        createTable(dbTasks);
        this.close();
    }

    /**
     * Insert pre-set data into the database
     */
    public void insertData() {
        //Returns the database associated with this helper
        SQLiteDatabase db = this.getWritableDatabase();
        String insertSQL = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'York' AS Title, '53.960038' AS Latitude,'-1.087233' AS Longitude";
        String insertSQL2 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'London' AS Title, '51.507276' AS Latitude,'-0.127708' AS Longitude";
        String insertSQL3 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Yeovil' AS Title, '50.942089' AS Latitude,'-2.633316' AS Longitude";
        String insertSQL4 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Guildford' AS Title, '51.236386' AS Latitude,'-0.570279' AS Longitude";
        String insertSQL5 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Cardiff' AS Title, '51.481942' AS Latitude,'-3.179099' AS Longitude";
        String insertSQL6 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Edinburgh' AS Title, '55.953266' AS Latitude,'-3.188214' AS Longitude";
        String insertSQL7 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'University of Surrey' AS Title, '51.24293611111111' AS Latitude,'-0.5894222222222223' AS Longitude";
        String insertSQL8 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Colchester' AS Title, '51.895963' AS Latitude,'0.891850' AS Longitude";
        String insertSQL9 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Hastings' AS Title, '50.85426' AS Latitude,'0.573406' AS Longitude";
        String insertSQL10 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Inverness' AS Title, '57.477947' AS Latitude,'-4.224667' AS Longitude";
        String insertSQL11 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Horsham' AS Title, '51.061569' AS Latitude,'-0.350361' AS Longitude";
        String insertSQL12 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch' AS Title, '53.224599' AS Latitude,'-4.198008' AS Longitude";
        String insertSQL13 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Sherwood Forest' AS Title, '53.205195' AS Latitude,'-1.085276' AS Longitude";
        String insertSQL14 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Manchester' AS Title, '53.479177' AS Latitude,'-2.248639' AS Longitude";
        String insertSQL15 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Scarborough' AS Title, '54.283179' AS Latitude,'-0.399770' AS Longitude";
        String insertSQL16 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Lands End' AS Title, '50.066190' AS Latitude,'-5.714738' AS Longitude";
        String insertSQL17 = "INSERT INTO " + TABLE_MARKERS + " (Title, Latitude, Longitude) " + "SELECT 'Dunnet Head' AS Title, '58.671460' AS Latitude,' -3.376532' AS Longitude";
        db.execSQL(insertSQL);
        db.execSQL(insertSQL2);
        db.execSQL(insertSQL3);
        db.execSQL(insertSQL4);
        db.execSQL(insertSQL5);
        db.execSQL(insertSQL6);
        db.execSQL(insertSQL7);
        db.execSQL(insertSQL8);
        db.execSQL(insertSQL9);
        db.execSQL(insertSQL10);
        db.execSQL(insertSQL11);
        db.execSQL(insertSQL12);
        db.execSQL(insertSQL13);
        db.execSQL(insertSQL14);
        db.execSQL(insertSQL15);
        db.execSQL(insertSQL16);
        db.execSQL(insertSQL17);
        this.close();
    }

    /**
     * Add an marker to the database
     * @param marker
     */
    public void addMarker (myMarker marker){

        Log.d("addmarker", marker.toString());

        //Get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Create ContentValues
        ContentValues values = new ContentValues();

        //Getting the String values for the Item object's attributes so these can be added to the database
        values.put(KEY_TITLE, marker.getTitle());
        values.put(KEY_LONGITUDE, marker.getLongitude());
        values.put(KEY_LATITUDE, marker.getLatitude());

        //Insert
        db.insert(TABLE_MARKERS, null, values);

        db.close();
    }

    /**
     * Get the list of all Items in the database
     * @return markers
     */
    public ArrayList<myMarker> getAllMarkers() {
        ArrayList<myMarker> markers = new ArrayList<myMarker>();

        //Create the query
        String query = "SELECT  * FROM " + TABLE_MARKERS;

        //Get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //Create items and add them to the list
        myMarker marker = null;
        if (cursor.moveToFirst()) {
            do {

                String lat = cursor.getString(2);
                String lng = cursor.getString(3);

                Double latitude = Double.parseDouble(lat);
                Double longitude = Double.parseDouble(lng);

                myMarker newMarker = new myMarker(Integer.parseInt(cursor.getString(0)), cursor.getString(1), latitude, longitude);

                //Add the new Item to items
                markers.add(newMarker);
            } while (cursor.moveToNext());
        }

        Log.d("testing", "getAllMarkers()");

        //Return items
        return markers;
    }

    /**
     * Deleting an item from the database
     * @param marker
     */
    public void deleteMarker(myMarker marker) {

        //Get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the item based on it's ID
        String deleteItem = "DELETE FROM " + TABLE_MARKERS + " WHERE " + KEY_TITLE + " = '" + marker.getTitle()+"';";
        db.execSQL(deleteItem);

        db.close();

        Log.d("testing", "deleteMarker");

    }
}
