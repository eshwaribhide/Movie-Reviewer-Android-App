package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private static final String TAG = "LeaderboardActivity";
    private RecyclerView recyclerView;
    private LeaderboardRecyclerViewAdapter recyclerViewAdapter;

    // private ArrayList<LeaderboardActivity.LeaderboardItem> userItems = new ArrayList<>();

    public static class LeaderboardItem {
        public final String userName;
        public final int userReviewCount;
        public final String badgeLevel;

        public LeaderboardItem(String userName, int userReviewCount, String badgeLevel) {
            this.userName = userName;
            this.userReviewCount = userReviewCount;
            this.badgeLevel = badgeLevel;
        }

        public String getBadgeLevel() {
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
        ArrayList<LeaderboardActivity.LeaderboardItem> leaderboardItems = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.leaderboardRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase.child("users").get().addOnCompleteListener(t -> {
            if (!t.isSuccessful()) {
                Log.e("firebase", "Error getting data", t.getException());
            } else {

                for (DataSnapshot dschild : t.getResult().getChildren()) {
                    String username = String.valueOf(dschild.getKey());

                    int reviewCount = Integer.parseInt(String.valueOf(dschild.child("reviewCount").getValue()));
                    String badgeStatus = String.valueOf(dschild.child("badgeStatus").getValue());
                    leaderboardItems.add(new LeaderboardItem(username, reviewCount, badgeStatus));
                    Log.e("LEADERBOARDITEMS", String.valueOf(leaderboardItems));
                }
                Log.e("LEADERBOARDITEMSDONE", String.valueOf(leaderboardItems));


                leaderboardItems.sort((Comparator.comparing(LeaderboardItem::getUserReviewCount)).reversed());

                recyclerViewAdapter = new LeaderboardRecyclerViewAdapter(LeaderboardActivity.this, leaderboardItems);
                recyclerView.setAdapter(recyclerViewAdapter);

            }});



    }
}
