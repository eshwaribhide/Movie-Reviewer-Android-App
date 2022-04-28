package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// get from db and then update

public class MovieDetailsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String currentUser;
    private String movieID;
    private String movieTitle;

    private RecyclerView recyclerView;
    private MovieDetailsRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private final ArrayList<MovieDetailsActivity.ReviewItem> ReviewItems = new ArrayList<>();

    public static class ReviewItem {
        private final String reviewTitle;
        private final String reviewContent;


        public ReviewItem(String reviewTitle, String reviewContent) {
            this.reviewTitle = reviewTitle;
            this.reviewContent = reviewContent;
        }

        public String getreviewTitle() {
            return reviewTitle;
        }

        public String getreviewContent() {
            return reviewContent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getIncomingIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        generateRecyclerView();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("reviews").get().addOnCompleteListener(t -> {
            if (!t.isSuccessful()) {
                Log.i("firebase", "Error getting data", t.getException());
            } else {
                for (DataSnapshot dschild : t.getResult().getChildren()) {
                    String childMovieID = String.valueOf(dschild.child("movieID").getValue());
                    if (childMovieID.equals(movieID)) {
                        String reviewTitle = String.valueOf(dschild.child("reviewTitle").getValue());
                        String reviewContent = dschild.child("reviewContent").getValue() + "\n~ Reviewed By: " + dschild.child("username").getValue() + " on " + dschild.getKey();
                        addReviewItemToRecyclerView(reviewTitle, reviewContent);
                    }
                }
            }
        });
    }

    private void addReviewItemToRecyclerView(String reviewTitle, String reviewContent) {
        recyclerViewLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
        ReviewItems.add(0, new MovieDetailsActivity.ReviewItem(reviewTitle, reviewContent));
        //recyclerViewAdapter.notifyItemInserted(0);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void generateRecyclerView() {
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewMovieDetails);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new MovieDetailsRecyclerViewAdapter(this, ReviewItems);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

    }


    private void getIncomingIntent() {
        if(getIntent().hasExtra("currentUser") && getIntent().hasExtra("movie_id") && getIntent().hasExtra("movie_title") && getIntent().hasExtra("movie_poster")
                && getIntent().hasExtra("movie_release_date") && getIntent().hasExtra("movie_description")) {
            String currentUser = getIntent().getStringExtra("currentUser");
            String movieID = getIntent().getStringExtra("movie_id");
            String movieTitle = getIntent().getStringExtra("movie_title");
            String moviePoster = getIntent().getStringExtra("movie_poster");
            String movieReleaseDate = getIntent().getStringExtra("movie_release_date");
            String movieDescription = getIntent().getStringExtra("movie_description");

            this.currentUser = currentUser;
            this.movieID = movieID;
            this.movieTitle = movieTitle;
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

        final EditText editReviewTitle = new EditText(this);
        editReviewTitle.setHint("Enter Review Title");
        layout.addView(editReviewTitle);

        final EditText editReviewContent = new EditText(this);
        editReviewContent.setHint("Write Review");
        layout.addView(editReviewContent);

        editReviewTitle.setTextColor(Color.parseColor("#9C27B0"));
        editReviewContent.setTextColor(Color.parseColor("#9C27B0"));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
            mDatabase.child("movies").child(movieID).get().addOnCompleteListener(t1 -> {
                // The date is the review ID
                String date = new Date().toString();
                if (t1.getResult().getValue() == null) {
                    mDatabase.child("movies").child(movieID).setValue(movieID);
                }
                // storing review content in a child node with review ID of date
                mDatabase.child("reviews").child(date).setValue(new Review(movieID, currentUser, editReviewTitle.getText().toString(), editReviewContent.getText().toString(), movieTitle));
                // Refresh adapter to reflect new review
                addReviewItemToRecyclerView(editReviewTitle.getText().toString(), editReviewContent.getText().toString());

                // increment user review count and then update the badge status
                mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        int reviewCount = Integer.parseInt(String.valueOf(task.getResult().child("reviewCount").getValue()));
                        int newReviewCount = reviewCount + 1;
                        if (newReviewCount >=10) {
                            if (newReviewCount >=20) {
                                mDatabase.child("users").child(currentUser).child("badgeStatus").setValue("Gold");
                            }
                            else {
                                mDatabase.child("users").child(currentUser).child("badgeStatus").setValue("Silver");
                            }
                        }
                        mDatabase.child("users").child(currentUser).child("reviewCount").setValue(newReviewCount);
                    }
                });
            });
        });


        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
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

    public void floatingActionButtonOnClick(View view) {
        finish();
        startActivity(getIntent());
    }

}