package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HomePage extends AppCompatActivity {
    private String currentUser;

    // static data for testing
    private final MovieCard movie1 = new MovieCard(1, 10, "12 Angry Men", "Description");
    private final MovieCard movie2 = new MovieCard(2, 5, "21 Jump Street", "Description");
    private final MovieCard movie3 = new MovieCard(3, 23, "Bohemian Rhapsody", "Description");

    private final ArrayList<MovieCard> movieList = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));
    private RecyclerView recyclerView;
    private RecyclerView reviewsRecyclerView;
    private RecyclerView.LayoutManager linearLayout;
    private HomePageMoviesAdapter homePageAdapter;
    private HomePageReviewsAdapter homePageReviewsAdapter;

    private DatabaseReference mDatabase;
    private boolean hasSelectedComedy = false;
    private boolean hasSelectedAction = false;
    private boolean hasSelectedDrama = false;
    private ArrayList<String> usersFollowing = new ArrayList<>();
    private ArrayList<ReviewCard> relevantReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initSavedInstanceState(savedInstanceState);
        createRecyclerView();

        // Get 5 movies of user's specified genres
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Find genres
        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
           if(!task.isSuccessful()) {
               Log.e("firebase", "Error getting data", task.getException());
           } else {
               // need to do a try/catch here to avoid null pointer exception
               hasSelectedComedy = (Boolean) task.getResult().child("genres").child("comedyGenreSelected").getValue();
               hasSelectedAction = (Boolean) task.getResult().child("genres").child("actionGenreSelected").getValue();
               hasSelectedDrama = (Boolean) task.getResult().child("genres").child("dramaGenreSelected").getValue();
           }
        });

        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                try {
                    // Not sure if this is the best way to access all users following but it works
                    Map<String, String> followingObject = (Map<String, String>) task.getResult().child("following").getValue();
                    followingObject.forEach((key, value) -> {
                        usersFollowing.add(key);
                    });
                    if (usersFollowing.size() > 0) {
                        getArrayOfReviews();
                    }
                } catch (NullPointerException e) {
                    Log.e("firebase", "No following child found", task.getException());
                }
                System.out.println(usersFollowing);
            }
        });
        createReviewsRecyclerView();
    }

    private void createRecyclerView() {
        linearLayout = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setHasFixedSize(true);
        homePageAdapter = new HomePageMoviesAdapter(movieList, this);
        recyclerView.setAdapter(homePageAdapter);
        recyclerView.setLayoutManager(linearLayout);
    }

    private void createReviewsRecyclerView() {
        linearLayout = new LinearLayoutManager(this);
        reviewsRecyclerView = findViewById(R.id.recycler_view_reviews);
        reviewsRecyclerView.setHasFixedSize(true);
        // show only 5 most recent reviews
        homePageReviewsAdapter = new HomePageReviewsAdapter(new ArrayList<>(relevantReviews.subList(0, Math.min(5, relevantReviews.size()))), this);
        reviewsRecyclerView.setAdapter(homePageReviewsAdapter);
        reviewsRecyclerView.setLayoutManager(linearLayout);
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

    private void getArrayOfReviews() {
        mDatabase.child("reviews").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.i("firebase", "Error getting data", task.getException());
            } else {
                for (DataSnapshot dschild : task.getResult().getChildren()) {
                    String author = String.valueOf(dschild.child("username").getValue());
                    if (usersFollowing.contains(author)) {
                        String movieId = String.valueOf(dschild.child("movieID").getValue());
                        try {
                            String movieTitle = String.valueOf(dschild.child("movieTitle").getValue());
                            String reviewTitle = String.valueOf(dschild.child("reviewTitle").getValue());
                            ReviewCard review = new ReviewCard(author, movieTitle, reviewTitle);
                            System.out.println("MOVIE TITLE LENGTH: " + movieTitle.length());
                            // This is for old reviews that don't include the movie title
                            if (movieTitle.equals("null")) {
                                review = new ReviewCard(author, movieId, reviewTitle);
                            }
                            relevantReviews.add(0, review);
                        } catch (NullPointerException e) {
                            Log.e("Firebase", e.getLocalizedMessage());
                        }
                    }
                }
                System.out.println(relevantReviews);
            }
            if (relevantReviews.size() > 0) {
                createReviewsRecyclerView();
            }
            System.out.println("Total reviews " + relevantReviews.size());
        });
    }
}