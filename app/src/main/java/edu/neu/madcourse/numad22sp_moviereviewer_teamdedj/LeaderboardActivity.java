package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String TAG = "LeaderboardActivity";
    private RecyclerView recyclerView;
    private LeaderboardRecyclerViewAdapter recyclerViewAdapter;

    // private ArrayList<LeaderboardActivity.LeaderboardItem> userItems = new ArrayList<>();

    public static class LeaderboardItem {
        public final int userId;
        public final String userName;
        public final int userReviewCount;
        public final int badgeLevel;

        public LeaderboardItem(int userId, String userName, int userReviewCount, int badgeLevel) {
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
        ArrayList<LeaderboardActivity.LeaderboardItem> leaderboardItems = new ArrayList<>(Arrays.asList(
                new LeaderboardItem(1, "user_123", 10, 1),
                new LeaderboardItem(2, "user_456", 30, 3),
                new LeaderboardItem(3, "user_789", 20, 2))
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.leaderboardRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaderboardItems.sort((Comparator.comparing(LeaderboardItem::getUserReviewCount)).reversed());

        recyclerViewAdapter = new LeaderboardRecyclerViewAdapter(LeaderboardActivity.this, leaderboardItems);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
