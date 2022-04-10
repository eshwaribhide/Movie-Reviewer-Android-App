package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// need username

public class MovieDetailsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("movie_id") && getIntent().hasExtra("movie_title") && getIntent().hasExtra("movie_poster")
                && getIntent().hasExtra("movie_release_date") && getIntent().hasExtra("movie_description")) {
            // add when a review is made
            String movieID = getIntent().getStringExtra("movie_id");
            String movieTitle = getIntent().getStringExtra("movie_title");
            String moviePoster = getIntent().getStringExtra("movie_poster");
            String movieReleaseDate = getIntent().getStringExtra("movie_release_date");
            String movieDescription = getIntent().getStringExtra("movie_description");

            this.movieID = movieID;
            setMovieDetails(movieTitle, moviePoster, movieReleaseDate, movieDescription);

        }
    }

    private void setMovieDetails(String movieTitle, String moviePoster, String movieReleaseDate, String movieDescription) {
        TextView movieTitleView = findViewById(R.id.movie_details_title);
        movieTitleView.setText(movieTitle);

        TextView releaseDateView = findViewById(R.id.movie_details_release_date);
        releaseDateView.setText(movieReleaseDate);

        ImageView moviePosterView = findViewById(R.id.movie_details_image);
        Picasso.get().load(moviePoster).into(moviePosterView);

        // set description
        TextView movieDescriptionView = findViewById(R.id.movie_details_description);
        movieDescriptionView.setText(movieDescription);
    }

    public void reviewMovieBtnOnClick(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Write A Review");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editReview = new EditText(this);
        editReview.setHint("Write Review");
        layout.addView(editReview);

        editReview.setTextColor(Color.parseColor("#9C27B0"));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
            mDatabase.child("movies").child(movieID).get().addOnCompleteListener(t1 -> {
                // The date is the review ID
                String date = new Date().toString();
                if (t1.getResult().getValue() == null) {
                    mDatabase.child("movies").child(movieID).setValue(movieID);
                }
                mDatabase.child("movies").child(movieID).child("reviews").child(date).setValue(date);
                mDatabase.child("reviews").child(date).setValue(editReview.getText().toString());

            });
        });


        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
}
}