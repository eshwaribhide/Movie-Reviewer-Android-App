package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("movie_title") && getIntent().hasExtra("movie_poster")
                && getIntent().hasExtra("movie_release_date") && getIntent().hasExtra("movie_description")) {
            String movieTitle = getIntent().getStringExtra("movie_title");
            String moviePoster = getIntent().getStringExtra("movie_poster");
            String movieReleaseDate = getIntent().getStringExtra("movie_release_date");
            String movieDescription = getIntent().getStringExtra("movie_description");

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
}