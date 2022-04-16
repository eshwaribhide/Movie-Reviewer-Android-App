package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

/**
 * A class for listing the Theaters near the user.
 */
public class TheaterNearMeActivity extends AppCompatActivity {

    private static final String googleAPIUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String googleAPIkey = "AIzaSyC-2LHart9nHW5WxvYiGkHMc9jILr0kTpw";
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
            return theaterName;
        }

        /**
         * Get the address of the theater
         * @return the address of the theater
         */
        public String getTheaterAddress() {
            return theaterAddress;
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
         * Get the distance of the theater from the user
         */
        public String getDistance() {
            return "5.5 mi";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        handler = new Handler();
        workerThread workerThread = new workerThread();
        new Thread(workerThread).start();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theater_list);

    }

    /**
     * A class for the worker thread to get the data from the Google API
     */
    private class workerThread implements Runnable{
        @Override
        public void run() {
            findNearByTheaters(42.342991, -71.101021);
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

                // Get the name of the theater
                String theaterName = "";
                if (theater.has("name")) {
                    theaterName = theater.getString("name");
                }

                // Get the address of the theater
                String theaterAddress = "";
                if (theater.has("vicinity")) {
                    theaterAddress = theater.getString("vicinity");
                }

                // Get the icon image url
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

                // Get theater location
                JSONObject location = null;
                if (theater.has("geometry") && theater.getJSONObject("geometry").has("location")){
                     location = theater.getJSONObject("geometry").getJSONObject("location");
                }

                theaterItems.add(new TheaterItem(theaterId, theaterName, theaterAddress,
                        theaterIconImageURL, theaterBusinessStatus, theaterRating, theaterRatingCount, location));

            }

            setTheaterInRecyclerView(theaterItems);

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
           // ToDo: set the theater not found message
        });
    }

}
