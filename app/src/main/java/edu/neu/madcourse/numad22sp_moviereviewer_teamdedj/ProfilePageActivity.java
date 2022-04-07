package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProfilePageActivity extends AppCompatActivity {

    // in db storing username, full name, genres, total reviews, and badge status (start with bronze, just store the drawable name),
    // which can
    // be retrieved with the username key. so when signing up need to control duplicate users.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
    }
}