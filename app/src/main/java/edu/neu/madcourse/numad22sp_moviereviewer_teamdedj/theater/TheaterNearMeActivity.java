package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

public class TheaterNearMeActivity extends AppCompatActivity {
    private static final String TAG = "TheaterNearMeActivity";
    private RecyclerView recyclerView;
    private TheaterNearMeRecyclerViewAdapter recyclerViewAdapter;


    public static class TheaterItem {
        public final int userId;
        public final String userName;
        public final int userReviewCount;
        public final int badgeLevel;

        public TheaterItem(int userId, String userName, int userReviewCount, int badgeLevel) {
            this.userName = userName;
            this.userReviewCount = userReviewCount;
            this.badgeLevel = badgeLevel;
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        public int getUserReviewCount() {
            return userReviewCount;
        }

        public String getUserName() {
            return userName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // static data in place for testing
        ArrayList<TheaterNearMeActivity.TheaterItem> theaterItems = new ArrayList<>(Arrays.asList(
                new TheaterItem(1, "user_123", 10, 1),
                new TheaterItem(2, "user_456", 30, 3),
                new TheaterItem(3, "user_789", 20, 2))
        );
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theater_list);

        recyclerView = findViewById(R.id.theaterListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        theaterItems.sort((Comparator.comparing(TheaterItem::getUserReviewCount)).reversed());

        recyclerViewAdapter = new TheaterNearMeRecyclerViewAdapter(TheaterNearMeActivity.this, theaterItems);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
