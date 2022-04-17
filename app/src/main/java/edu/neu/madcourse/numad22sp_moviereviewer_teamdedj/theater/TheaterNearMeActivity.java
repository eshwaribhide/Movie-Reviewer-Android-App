package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

/**
 * A class for listing the Theaters near the user.
 */
public class TheaterNearMeActivity extends AppCompatActivity implements LocationListener{

    private static final String googleAPIUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String googleAPIkey = "AIzaSyC-2LHart9nHW5WxvYiGkHMc9jILr0kTpw";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    String provider;
    private static Location userLocation = null;
    private static final String TAG = "TheaterNearMeActivity";
    private RecyclerView recyclerView;

    private Handler handler;
    private TheaterRecyclerViewAdapter recyclerViewAdapter;

    /**
     * A class for the theater item
     */
    public static class TheaterItem {

        private final String theaterId;
        private final String theaterName;
        private final String theaterAddress;
        private final String theaterIconImgUrl;
        private final Boolean theaterBusinessStatus;
        private final float theaterRating;
        private final int theaterUserReviewCount;
        private final JSONObject location;


        public TheaterItem(String theaterId, String theaterName, String theaterAddress,
                           String theaterIconImageURL, Boolean theaterBusinessStatus, Float theaterRating, int theaterUserReviewCount, JSONObject location) {
            this.theaterId = theaterId;
            this.theaterName = theaterName;
            this.theaterAddress = theaterAddress;
            this.theaterIconImgUrl = theaterIconImageURL;
            this.theaterBusinessStatus = theaterBusinessStatus;
            this.theaterRating = theaterRating;
            this.theaterUserReviewCount = theaterUserReviewCount;
            this.location = location;
        }

        /**
         * Get the id of the theater
         * @return the id of the theater
         */
        public String getTheaterId() {
            return theaterId;
        }

        /**
         * Get the name of the theater
         * @return the name of the theater
         */
        public String getTheaterName() {
            return theaterName.replace("\n", "").replace("\r", "");
        }

        /**
         * Get the address of the theater
         * @return the address of the theater
         */
        public String getTheaterAddress() {
            return theaterAddress.replace("\n", "").replace("\r", "");
        }

        /**
         * Get the icon image url of the theater
         * @return the icon image url of the theater
         */
        public String getTheaterIconImgUrl() {
            return theaterIconImgUrl;
        }

        /**
         * Get the business status of the theater
         * @return the business status of the theater
         */
        public Boolean getTheaterBusinessStatus() {
            return theaterBusinessStatus;
        }

        /**
         * Get the rating of the theater
         * @return the rating of the theater
         */
        public float getTheaterRating() {
            return theaterRating;
        }

        /**
         * Get the user rating count of the theater
         * @return the user rating count of the theater
         */
        public int getTheaterUserReviewCount() {
            return theaterUserReviewCount;
        }

        /**
         * Get the location of the theater
         * @return the location of the theater
         */
        public JSONObject getLocation() {
            return location;
        }

        /**
         * Get the distance of the theater from the user.
         */
        public String getDistance() {
            if (location != null && location.has("lat") && location.has("lng")) {

                double userLat = userLocation.getLatitude();
                double userLng = userLocation.getLongitude();

                double latitude = location.optDouble("lat");
                double longitude = location.optDouble("lng");

                return distance(userLat, userLng, latitude, longitude) + " mi";
            }
            return "";
        }


        /**
         * Calculate the distance between two points in latitude and longitude.
         * @param lat1 Users latitude
         * @param lon1 Users longitude
         * @param lat2 Theaters latitude
         * @param lon2 Theaters longitude
         * @return the distance between the two points in miles
         */
        private static double distance(double lat1, double lon1, double lat2, double lon2) {

            lon1 = Math.toRadians(lon1);
            lon2 = Math.toRadians(lon2);
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);

            // Haversine formula
            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            double a = Math.pow(Math.sin(dlat / 2), 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.pow(Math.sin(dlon / 2),2);

            double c = 2 * Math.asin(Math.sqrt(a));

            // Radius of earth in kilometers. Use 3956
            // for miles
            double r = 6371;

            // calculate the result
            double distance = c * r * 0.621371;
            return Math.round(distance * 100.0) / 100.0;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_list);
        handler = new Handler();

        if (checkLocationPermission()) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120 * 1000, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            setUserLocationAndGetTheatersNearby(location);
        }else{
            displayDenialOfLocationOnScreen();
        }
    }

    /**
     * Get the nearby theaters from the Google Places API using the user's
     * location and worker thread.
     */
    private void getNearestTheatersFromWorkerThread() {
        workerThread workerThread = new workerThread();
        new Thread(workerThread).start();
    }

    /**
     * A class for the worker thread to get the data from the Google API
     */
    private class workerThread implements Runnable{
        @Override
        public void run() {
            if (userLocation != null) {
                double userLocationLatitude = userLocation.getLatitude();
                double userLocationLongitude = userLocation.getLongitude();
                findNearByTheaters(userLocationLatitude, userLocationLongitude);
            } else {
                displayDenialOfLocationOnScreen();
            }

        }
    }

    /**
     * Private method to convert the input stream to a string
     * @param input the input stream
     * @return the string
     */
    private String ConvertInputString(InputStream input){
        Scanner s = new Scanner(input).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : ":";
    }

    /**
     * Find the theaters near the user using Google Places API.
     * @param latitude the latitude of the user
     * @param longitude the longitude of the user
     */
    private void findNearByTheaters(double latitude, double longitude) {

        StringBuilder sb = new StringBuilder(googleAPIUrl);
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&rankby=distance");
        sb.append("&keyword=cinema");
        sb.append("&key=" + googleAPIkey);


        HttpURLConnection urlConnection;
        URL url;
        try {
            url = new URL(sb.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream input = urlConnection.getInputStream();
            final String response = ConvertInputString(input);

            JSONObject jObj = new JSONObject(response);

            if (jObj.getString("status").equals("OK")){
                setDataInPlace(jObj);
            } else {
                setTheaterNotFound();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the data in the list.
     * @param jObj the json object that contains the data to be set from the api.
     */
    private void setDataInPlace(JSONObject jObj) {
        try {
            JSONArray results = jObj.getJSONArray("results");
            ArrayList<TheaterItem> theaterItems = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject theater = results.getJSONObject(i);
                String theaterId = theater.getString("place_id");

                // Name of the theater
                String theaterName = "";
                if (theater.has("name")) {
                    theaterName = theater.getString("name");
                }

                // Address of the theater
                String theaterAddress = "";
                if (theater.has("vicinity")) {
                    theaterAddress = theater.getString("vicinity");
                }

                // Image
                String theaterIconImageURL = "";
                if (theater.has("photos")) {
                    theaterIconImageURL = theater.getString("icon");
                }

                //Open/Close status
                boolean theaterBusinessStatus = false;
                if (theater.has("opening_hours") && theater.getJSONObject("opening_hours").has("open_now")){
                    theaterBusinessStatus = Boolean.parseBoolean(theater.getJSONObject("opening_hours").getString("open_now"));
                }

                //Rating
                float theaterRating = 0.0f;
                int theaterRatingCount = 0;
                if (!theater.getString("rating").equals("")){
                    theaterRating = Float.parseFloat(theater.getString("rating"));
                    theaterRatingCount = Integer.parseInt(theater.getString("user_ratings_total"));
                }

                // Location of the theater
                JSONObject location = null;
                if (theater.has("geometry") && theater.getJSONObject("geometry").has("location")){
                     location = theater.getJSONObject("geometry").getJSONObject("location");
                }

                theaterItems.add(new TheaterItem(theaterId, theaterName, theaterAddress,
                        theaterIconImageURL, theaterBusinessStatus, theaterRating, theaterRatingCount, location));

            }

            if (theaterItems.size() > 0) {
                setTheaterInRecyclerView(theaterItems);
            } else {
                setTheaterNotFound();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the theater data in the recycler view.
     * @param theaterItems the theater data from the API
     */
    private void setTheaterInRecyclerView(ArrayList<TheaterItem> theaterItems) {
        handler.post(() -> {
            recyclerView = findViewById(R.id.theaterListRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerViewAdapter = new TheaterRecyclerViewAdapter(TheaterNearMeActivity.this, theaterItems);
            recyclerView.setAdapter(recyclerViewAdapter);
        });
    }

    /**
     * Set the theater not found message.
     */
    private void setTheaterNotFound() {
        handler.post(() -> {
            TextView theaterNotFound = findViewById(R.id.no_results_text);
            theaterNotFound.setVisibility(View.VISIBLE);
        });
    }


    //===============================Location Permission============================================
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result array is empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permission granted
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    locationManager.requestLocationUpdates(provider, 120 * 1000, 0, this);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    setUserLocationAndGetTheatersNearby(location);
                }
            } else {
                displayDenialOfLocationOnScreen();
            }
        }
    }

    /**
     * Check if the user has granted the location permission.
     * @return true if the user has granted the location permission
     */
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Access Location")
                        .setMessage("Needs permission")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(TheaterNearMeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Set the location of the user and get the theaters nearby.
     * @param location The location of the user.
     */
    private void setUserLocationAndGetTheatersNearby(Location location){
        if (location != null) {
            userLocation = location;
            TextView theaterNotFound = findViewById(R.id.no_results_text);
            theaterNotFound.setVisibility(View.GONE);
            // Initialize the worker thread to get theaters from google places api
            getNearestTheatersFromWorkerThread();
        } else {
            Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Display the denial of location permission on screen.
     */
    private void displayDenialOfLocationOnScreen(){
        TextView theaterNotFound = findViewById(R.id.no_results_text);
        String message = "You have denied permission to your location. Please enable to " +
                "check your location.";
        theaterNotFound.setText(message);
        theaterNotFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        setUserLocationAndGetTheatersNearby(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}
