package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
// reviews child
// review -> ID is date, review content

// we go to a movie page, need to see all the reviews for it -> in a movie object, should have
// a reviews child node with the review IDs
// then when you want to load all the current reviews, you take all the ids, go to review, and
// get the content
// also have nodes with child nodes that are movie ids have a specific section for the ids, then
// per movie you have reviews
// can expand genres later, need to add more checkboxes
public class NavigationActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initSavedInstanceState(savedInstanceState);
        }
    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("currentUser")) {
            currentUser = savedInstanceState.getString("currentUser");
        }
        else {
            Log.e("INITDATA", "INITDATA");
            Bundle b = getIntent().getExtras();
            if (b != null) {
                currentUser = b.getString("currentUser");
            }
        }
    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }



    public void profilePageOnClick(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

    public void feedPageOnClick(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void reviewMoviesOnClick(View view) {
        Intent intent = new Intent(this, MovieListActivity.class);
        startActivity(intent);
    }

    public void leaderboardOnClick(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

}