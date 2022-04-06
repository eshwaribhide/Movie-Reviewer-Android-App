package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    public void profilePageOnClick(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
    }

}