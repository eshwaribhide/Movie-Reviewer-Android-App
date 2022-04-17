package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2.ProfilePageV2Activity;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater.TheaterNearMeActivity;


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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                currentUser = b.getString("currentUser");
                finish();
                startActivity(getIntent());
            }
        }
    }

    public void profilePageOnClick(View view) {
        Bundle b = new Bundle();
        b.putString("currentUser", currentUser);
        b.putString("searchedUser", currentUser);
        //Intent intent = new Intent(this, ProfilePageActivity.class);
        Intent intent = new Intent(this, ProfilePageV2Activity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void feedPageOnClick(View view) {
        Bundle b = new Bundle();
        b.putString("currentUser", currentUser);
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void reviewMoviesOnClick(View view) {
        Bundle b = new Bundle();
        b.putString("currentUser", currentUser);
        Intent intent = new Intent(this, MovieListActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void leaderboardOnClick(View view) {
        Bundle b = new Bundle();
        b.putString("currentUser", currentUser);
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtras(b);
        startActivity(intent);

    }

    public void searchUsersOnClick(View view) {
        Bundle b = new Bundle();
        b.putString("currentUser", currentUser);
        Intent intent = new Intent(this, SearchUsersActivity.class);
        intent.putExtras(b);
        startActivity(intent);

    }

    /**
     * An on-click function which opens the theaters list near the user
     * @param view parent view
     */
    public void theaterNearMeOnClick(View view) {
        Intent intent = new Intent(this, TheaterNearMeActivity.class);
        startActivity(intent);
    }
}