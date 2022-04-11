package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StaticProfilePageActivity extends AppCompatActivity {
    private String currentUser;
    private String searchedUser;
    private DatabaseReference mDatabase;
    private TextView username;
    private TextView totalReviews;
    private CheckBox comedyCheckBox;
    private CheckBox actionCheckBox;
    private CheckBox dramaCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_profile_page);
        initSavedInstanceState(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        username = findViewById(R.id.staticUsernameValue);
        totalReviews = findViewById(R.id.totalReviewsStatic);
        comedyCheckBox = findViewById(R.id.comedyCheckBoxStatic);
        actionCheckBox = findViewById(R.id.actionCheckBoxStatic);
        dramaCheckBox = findViewById(R.id.dramaCheckBoxStatic);

        mDatabase.child("users").child(searchedUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                username.setText(searchedUser);
                totalReviews.setText("Total Reviews: " + task.getResult().child("reviewCount").getValue());
                comedyCheckBox.setChecked((Boolean) task.getResult().child("genres").child("comedyGenreSelected").getValue());
                actionCheckBox.setChecked((Boolean) task.getResult().child("genres").child("actionGenreSelected").getValue());
                dramaCheckBox.setChecked((Boolean) task.getResult().child("genres").child("dramaGenreSelected").getValue());
            }
        });
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("currentUser") &&
                savedInstanceState.containsKey("searchedUser")) {
            currentUser = savedInstanceState.getString("currentUser");
            searchedUser = savedInstanceState.getString("searchedUser");
        } else {
            Log.e("INITDATA", "INITDATA");
            Bundle b = getIntent().getExtras();
            if (b != null) {
                currentUser = b.getString("currentUser");
                searchedUser = b.getString("searchedUser");
            }
        }
    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Bundle b = new Bundle();
                b.putString("currentUser", currentUser);
                Log.e("HISTORYCURRENTUSER", currentUser);
                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void followUserButtonOnClick(View view) {
        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                // Current user will follow searchedUser, searchedUser will have user following
                // follow button should disappear if user is following, replace with unfollow
                mDatabase.child("users").child(currentUser).child("following").child(searchedUser).setValue(searchedUser);
                mDatabase.child("users").child(searchedUser).child("followers").child(currentUser).setValue(currentUser);


            }
        });}
}