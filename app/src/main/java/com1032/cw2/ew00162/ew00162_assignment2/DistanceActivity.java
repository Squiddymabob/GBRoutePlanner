package com1032.cw2.ew00162.ew00162_assignment2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistanceActivity extends ActionBarActivity {

    // Array to hold the names of all the markers
    ArrayList<String> markerNames = new ArrayList<String>();

    // Array to hold the actual myMarker objects
    ArrayList<myMarker> markerObjects = new ArrayList<myMarker>();

    // Initialising factory
    SQLiteDatabase.CursorFactory factory = null;

    // Initialising the database
    private MarkerDB markersDB = new MarkerDB(this, factory);

    // TextViews
    private TextView distanceField;
    private TextView timeField;


    /**
     * Called when first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        // TextViews
        distanceField = (TextView) findViewById(R.id.textView4);
        timeField = (TextView) findViewById(R.id.textView9);

        // The two spinners
        Spinner startSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner endSpinner = (Spinner) findViewById(R.id.spinner2);

        // Filling the array with all the myMarker objects from the database
        markerObjects = markersDB.getAllMarkers();

        // Getting the names of the myMarker objects and adding them to the markerNames array
        for (myMarker tmp : markerObjects) {

            String markerName = null;

            markerName = tmp.getTitle().toString();

            markerNames.add(markerName);

        }

        // ArrayAdapter for the spinners
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, markerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);

        // Listener for the calculate distance button
        Button button_calculate = (Button) findViewById(R.id.button_calculate);
        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner startSpinner = (Spinner) findViewById(R.id.spinner);
                Spinner endSpinner = (Spinner) findViewById(R.id.spinner2);

                // Get selections
                String startSelection = (String) startSpinner.getSelectedItem();
                String endSelection = (String) endSpinner.getSelectedItem();

                // Process selections
                Double lat1 = null;
                Double lng1 = null;
                Double lat2 = null;
                Double lng2 = null;

                // Get the latitude and longitude using the marker title
                for (myMarker tmp1 : markerObjects) {
                    if (tmp1.getTitle() == startSelection) {
                        lat1 = tmp1.getLatitude();
                        lng1 = tmp1.getLongitude();
                    }
                }

                for (myMarker tmp2 : markerObjects) {
                    if (tmp2.getTitle() == endSelection) {
                        lat2 = tmp2.getLatitude();
                        lng2 = tmp2.getLongitude();
                    }
                }

                // Set distanceField text to be the distance in miles
                if (lat1 != null && lng1 != null && lat2 != null && lng2 != null) {
                    Double distance = null;
                    Double time = null;
                    distance = calculateDistance(lat1, lng1, lat2, lng2);
                    time =  walkingTime(distance);
                    distanceField.setText(distance.toString() + " Miles");
                    timeField.setText(time.toString() + " Hours");
                }

            }
        });

        // Listener for the show route button
         Button button_route = (Button) findViewById(R.id.button_route);
        button_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {

                Spinner startSpinner = (Spinner) findViewById(R.id.spinner);
                Spinner endSpinner = (Spinner) findViewById(R.id.spinner2);

                // Get selections
                String startSelection = (String) startSpinner.getSelectedItem();
                String endSelection = (String) endSpinner.getSelectedItem();

                // Process selections
                Double lat1 = null;
                Double lng1 = null;
                Double lat2 = null;
                Double lng2 = null;

                // Get the latitude and longitude using the marker title
                for (myMarker tmp1 : markerObjects) {
                    if (tmp1.getTitle() == startSelection) {
                        lat1 = tmp1.getLatitude();
                        lng1 = tmp1.getLongitude();
                    }
                }

                for (myMarker tmp2 : markerObjects) {
                    if (tmp2.getTitle() == endSelection) {
                        lat2 = tmp2.getLatitude();
                        lng2 = tmp2.getLongitude();
                    }
                }

                // Set distanceField text to be the distance in miles
                if (lat1 != null && lng1 != null && lat2 != null && lng2 != null) {
                    calculateRouteUrl(lat1, lng1, lat2, lng2);
                }
            }
        });
}

    /**
     * Preparing the options menu by inflating all the menus
     * @param menu
     * @return super.onPrepareOptionsMenu(menu);
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Clearing the view of the previous menu
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Switch for what happens when each menu item is clicked
     * @param item
     * @return true/false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_view_map:

                //Start MapsActivity
                Intent myIntent2 = new Intent(DistanceActivity.this, MapsActivity.class);
                DistanceActivity.this.startActivity(myIntent2);

                return true;

            default:
                return false;
        }
    }

    /**
     * Method for calculating distance between two points, in meters, if latitude and longitude are known
     * Then converted to miles
     * Edited from source
     * Source: http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java [Accessed 23/5/15]
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return Math.round(distance)
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        // Converting meters to miles
        distance = distance * 0.000621371192;

        // Rounded to be more user-friendly
        return Math.round(distance);
    }

    /**
     * Creating the URL for the chosen route
     * @param startLat
     * @param startLng
     * @param endLat
     * @param endLng
     */
    public void calculateRouteUrl(Double startLat, Double startLng, Double endLat, Double endLng ){

        // Origin of route
        String str_origin = "origin="+startLat+","+startLng;

        // Destination of route
        String str_dest = "destination="+endLat+","+endLng;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        // Sending the URL to MapsActivity
        MapsActivity.url = url;

        // Going to MapsActivity to show the route on the map
        // The route shown will be the shortest path as Google Maps automatically does this
        Intent myIntent = new Intent(DistanceActivity.this, MapsActivity.class);
        DistanceActivity.this.startActivity(myIntent);

    }

    /**
     * Method to calculate average time to walk the distance
     * @param distance
     * @return Math.round(time)
     */
    public double walkingTime(double distance){

        double time = 0.00;

        // Distance / average walking speed in mph
        time = distance / 3.1;

        // Rounded to be more user-friendly
        return Math.round(time);

    }

}
