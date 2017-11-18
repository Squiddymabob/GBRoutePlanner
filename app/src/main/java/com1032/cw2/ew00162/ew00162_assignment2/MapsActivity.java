package com1032.cw2.ew00162.ew00162_assignment2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends ActionBarActivity implements LocationListener {

    // GoogleMap
    private GoogleMap googleMap;

    // Array of all myMarker objects
    public static ArrayList<myMarker> mapMarkers;

    // Initialising factory
    SQLiteDatabase.CursorFactory factory = null;

    // Initialising the database
    private MarkerDB markersDB = new MarkerDB(this, factory);

    // Location
    private LocationManager locationManager;
    private String provider;
    private Location location;

    // URL for directions
    public static String url = null;

    // For making sure a directions line is only drawn once
    public boolean lineDrawn = false;

    // Detecting orientation change
    // Default value is 3 as it is never used by getResources().getConfiguration().orientation but 0 is used
    int currentOri = 3;

    /**
     * Called when first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (checkIfDatabaseEmpty()) {
            markersDB.insertData();
        }

        mapMarkers = markersDB.getAllMarkers();

        // Get location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the location provider
        provider = locationManager.NETWORK_PROVIDER;
        location = locationManager.getLastKnownLocation(provider);


        // Verify interaction with Google Maps
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

            /* MAP_TYPE_NORMAL: Basic map with roads.
            MAP_TYPE_SATELLITE: Satellite view without roads.
            MAP_TYPE_TERRAIN: Terrain view without roads.
            MAP_TYPE_HYBRID: Satellite view with roads.
            */

            // Show a satellite map with roads
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Place dot on current location
            googleMap.setMyLocationEnabled(true);

            // Turns traffic layer on
            googleMap.setTrafficEnabled(true);

            // Enables indoor maps
            googleMap.setIndoorEnabled(true);

            // Turns on 3D buildings
            googleMap.setBuildingsEnabled(true);

            // Show Zoom buttons
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Enable scroll gestures
            googleMap.getUiSettings().setScrollGesturesEnabled(true);

            // View UK on start
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.709070, -4.375423), 6.0f));

            // Add all markers to the actual map
            addMarkersToMap();

            // If rotation then redraw the route
            if (getResources().getConfiguration().orientation != currentOri){
                currentOri = getResources().getConfiguration().orientation;
                lineDrawn = false;
            }

            // If a URL has been sent to MapsActivity then the route needs to be drawn as it has been requested
            if (url != null && lineDrawn == false){
                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

                // This stops the route being drawn forever, which would cause a crash as the device's memory would fill up
                lineDrawn = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Add all the markers to the map
     */
    public void addMarkersToMap() {

        for (myMarker tmp : mapMarkers) {

            googleMap.addMarker(new MarkerOptions().position(new LatLng(tmp.getLatitude(), tmp.getLongitude())).title(tmp.getTitle()));

        }
    }


    /**
     * Checking to see if the database is empty
     * This is done so that the pre-populated markers are only added once
     * Not checking will cause them to be re-added every time the app is opened
     * @return true/false
     */
    public boolean checkIfDatabaseEmpty() {

        ArrayList<myMarker> tmp = new ArrayList<myMarker>();

        // Adding all markers to the array
        tmp = markersDB.getAllMarkers();

        // If the array is empty = database is empty
        if (tmp.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get updates on startup
     */
    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    /**
     * Remove the location listener updates when Activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    /**
     * When location is changed get the new latitude and longitude
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.i("longitude", location.getLongitude() + "l");

        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());

        final Geocoder geocoder = new Geocoder(this.getApplicationContext());
        List<Address> addresses = null;
        try {
            Log.i("lat", lat + "lat");
            addresses = geocoder.getFromLocation(lat,
                    lng, 1);
            if (addresses.size() > 0) {
                Log.i(addresses.get(0).getCountryName(), "Tag");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    /**
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.menu_distance, menu);
        inflater.inflate(R.menu.menu_new_marker, menu);
        inflater.inflate(R.menu.menu_clear, menu);
        inflater.inflate(R.menu.menu_refresh, menu);
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

            case R.id.action_distance:

                //Start DistanceActivity
                Intent myIntent = new Intent(MapsActivity.this, DistanceActivity.class);
                MapsActivity.this.startActivity(myIntent);

                return true;

            case R.id.action_add_new_marker:

                newMarkerDialog();

                return true;

            case R.id.action_clear:

                // Clear the database then add pre-populated markers
                markersDB.clearData();
                markersDB.insertData();
                MapsActivity.mapMarkers = markersDB.getAllMarkers();

                // Refreshing the map
                googleMap.clear();
                addMarkersToMap();

                // Redraw a route if there is one otherwise it will disappear when new marker is added

                if (lineDrawn = true) {
                    lineDrawn = false;
                }

                if (url != null && lineDrawn == false){
                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);

                    // This stops the route being drawn forever, which would cause a crash as the device's memory would fill up
                    lineDrawn = true;
                }

                Toast.makeText(getApplicationContext(),"All custom markers have been cleared", Toast.LENGTH_LONG).show();

                return true;

            case R.id.action_refresh:

                // Refresh the map a.k.a clear a drawn route
                googleMap.clear();
                addMarkersToMap();

                // Make sure route will not be redrawn
                url = null;
                lineDrawn = false;

                Toast.makeText(getApplicationContext(),"The map has been refreshed", Toast.LENGTH_LONG).show();

            default:
                return false;
        }
    }

    /**
     * Adding the title to the new marker
     * @param newTitle
     */
    public void newMarkerAtLocation(String newTitle) {

        newMarker(location, newTitle);

    }

    /**
     * Creating a new myMarker object and then adding it to the database
     * @param location
     * @param title
     */
    public void newMarker(Location location, String title) {

        // Get the current latitude and longitude
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        // Create new myMarker object
        myMarker newMyMarker = new myMarker(0, title, latitude, longitude);

        // Add new myMarker to the database
        markersDB.addMarker(newMyMarker);

        // Update the array from the database
        mapMarkers = markersDB.getAllMarkers();

    }

    /**
     * The dialog that allows a new marker to be given a title
     */
    public void newMarkerDialog() {

        final Dialog d = new Dialog(this);

        d.setContentView(R.layout.add_dialog);
        d.setTitle("Custom Marker At Current Location");
        d.setCancelable(true);

        // Retrieving the text from the text boxes
        final EditText newTitle = (EditText) d.findViewById(R.id.newTitle);

        Button buttonCancel = (Button) d.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v3) {

                //Dismiss the dialog when cancel is clicked
                d.dismiss();

            }
        });

        Button buttonSubmit = (Button) d.findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v2) {

                // Adding a new marker at the current location
                newMarkerAtLocation(newTitle.getText().toString());

                // Refreshing the map
                googleMap.clear();
                addMarkersToMap();

                // Redraw a route if there is one otherwise it will disappear when new marker is added

                lineDrawn = false;

                if (url != null && lineDrawn == false){
                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);

                    // This stops the route being drawn forever, which would cause a crash as the device's memory would fill up
                    lineDrawn = true;
                }

                //Dismiss the dialog when submit is clicked
                d.dismiss();

            }
        });

        d.show();

    }

    /**
     * Download json data from url
     * Adapted from source
     * Source: http://stackoverflow.com/questions/28295199/android-how-to-show-route-between-markers-on-googlemaps [Accessed 25/5/15]
     * @param strUrl
     * @return data
     * @throws IOException
     */
    private String downloadUrl(String strUrl) throws IOException{
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = null;
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * Class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);

        }
    }
}