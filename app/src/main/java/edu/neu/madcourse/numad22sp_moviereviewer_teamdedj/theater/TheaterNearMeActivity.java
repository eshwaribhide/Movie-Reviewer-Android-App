package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

/**
 * A class for listing the Theaters near the user
 */
public class TheaterNearMeActivity extends AppCompatActivity {


    private static final String TAG = "TheaterNearMeActivity";
    private RecyclerView recyclerView;
    private TheaterRecyclerViewAdapter recyclerViewAdapter;

    /**
     * A class for the theater item
     */
    public static class TheaterItem {
        public final int theaterId;
        public final String theaterName;
        public final int theaterDistance;
        public final String theaterAddress;
        public final float theaterRating;

        public TheaterItem(int theaterId, String theaterName, int theaterDistance, String theaterAddress, float theaterRating) {
            this.theaterId = theaterId;
            this.theaterName = theaterName;
            this.theaterDistance = theaterDistance;
            this.theaterAddress = theaterAddress;
            this.theaterRating = theaterRating;
        }

        /**
         * Get the id of the theater
         * @return the id of the theater
         */
        public int getTheaterId() {
            return theaterId;
        }

        /**
         * Get the distance of the theater
         * @return the distance of the theater
         */
        public String getTheaterDistance() {
            return String.valueOf(theaterDistance);
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
         * Get the rating of the theater
         * @return the rating of the theater
         */
        public float getTheaterRating() {
            return theaterRating;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<TheaterNearMeActivity.TheaterItem> theaterItems = new ArrayList<>(Arrays.asList(
                new TheaterItem(1, "theater_1", 10, "address_1", 4.5f),
                new TheaterItem(2, "theater_2", 30, "address_2", 3.5f),
                new TheaterItem(3, "theater_2", 20, "address_3", 2.5f)
        ));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theater_list);

        recyclerView = findViewById(R.id.theaterListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        theaterItems.sort((Comparator.comparing(TheaterItem::getTheaterDistance)).reversed());

        recyclerViewAdapter = new TheaterRecyclerViewAdapter(TheaterNearMeActivity.this, theaterItems);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
