package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.os.Bundle;

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
 * A class for listing the Theaters near the user
 */
public class TheaterNearMeActivity extends AppCompatActivity {

    private static final String googleAPIUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String googleAPIkey = "AIzaSyC-2LHart9nHW5WxvYiGkHMc9jILr0kTpw";
    private static final String TAG = "TheaterNearMeActivity";
    private RecyclerView recyclerView;
    private TheaterRecyclerViewAdapter recyclerViewAdapter;

    /**
     * A class for the theater item
     */
    public static class TheaterItem {

        public final String theaterId;
        public final String theaterName;
        public final String theaterAddress;
        public final String theaterIconImgUrl;
        public final String theaterBusinessStatus;
        public final float theaterRating;


        public TheaterItem(String theaterId, String theaterName, String theaterAddress,
                           String theaterIconImageURL, String theaterBusinessStatus, Float theaterRating) {
            this.theaterId = theaterId;
            this.theaterName = theaterName;
            this.theaterAddress = theaterAddress;
            this.theaterIconImgUrl = theaterIconImageURL;
            this.theaterBusinessStatus = theaterBusinessStatus;
            this.theaterRating = theaterRating;
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
        public String getTheaterBusinessStatus() {
            return theaterBusinessStatus;
        }

        /**
         * Get the rating of the theater
         * @return the rating of the theater
         */
        public float getTheaterRating() {
            return theaterRating;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        workerThread workerThread = new workerThread();
        new Thread(workerThread).start();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theater_list);


        ArrayList<TheaterItem> theaterItems = new ArrayList<>(Arrays.asList(
                new TheaterItem("1", "ABC1", "Park drive", "dxx", "Operational", (float) 4.5),
                new TheaterItem("2", "ABC2", "Park drive", "dxx", "Operational", (float) 4.5),
                new TheaterItem("3", "ABC3", "Park drive", "dxx", "Operational", (float) 4.5)
        ));



        recyclerView = findViewById(R.id.theaterListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new TheaterRecyclerViewAdapter(TheaterNearMeActivity.this, theaterItems);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private class workerThread implements Runnable{
        @Override
        public void run() {
            findNearByTheaters(42.342991, -71.101021);
        }
    }

    private String ConvertInputString(InputStream input){
        Scanner s = new Scanner(input).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : ":";
    }

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

    private void setDataInPlace(JSONObject jObj) {
        try {
            JSONArray results = jObj.getJSONArray("results");
            ArrayList<TheaterItem> theaterItems = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject theater = results.getJSONObject(i);
                String theaterId = theater.getString("place_id");
                String theaterName = theater.getString("name");
                String theaterAddress = theater.getString("vicinity");
                String theaterIconImageURL = theater.getString("icon");
                String theaterBusinessStatus = theater.getString("business_status");
                Float theaterRating = 0.0f;
                if (!theater.getString("rating").equals("")){
                    theaterRating = Float.parseFloat(theater.getString("rating"));
                }

                theaterItems.add(new TheaterItem(theaterId, theaterName, theaterAddress,
                        theaterIconImageURL, theaterBusinessStatus, theaterRating));

                System.out.println(theaterName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTheaterNotFound() {
    }


}
