package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
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
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HomePage extends AppCompatActivity {
    private String currentUser;
    private final String TAG = "HomePage debug";
    private final Handler textHandler = new Handler();

    private final ArrayList<MovieCard> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView reviewsRecyclerView;
    private RecyclerView.LayoutManager linearLayout;
    private HomePageMoviesAdapter homePageAdapter;
    private HomePageReviewsAdapter homePageReviewsAdapter;

    private DatabaseReference mDatabase;
    private boolean hasSelectedComedy = false;
    private boolean hasSelectedAction = false;
    private boolean hasSelectedDrama = false;
    private boolean hasSelectedCrime = false;
    private boolean hasSelectedAnimation = false;
    private boolean hasSelectedDocumentary = false;
    private boolean hasSelectedHistory = false;
    private boolean hasSelectedHorror = false;
    private boolean hasSelectedRomance = false;
    private boolean hasSelectedSciFi = false;

    private ArrayList<String> usersFollowing = new ArrayList<>();
    private ArrayList<ReviewCard> relevantReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initSavedInstanceState(savedInstanceState);

        if (savedInstanceState == null) {
            // Get 5 movies of user's specified genres
            mDatabase = FirebaseDatabase.getInstance().getReference();
            // Find genres
            mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
                if(!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    try {
                        hasSelectedComedy = (Boolean) task.getResult().child("genres").child("comedyGenreSelected").getValue();
                        hasSelectedAction = (Boolean) task.getResult().child("genres").child("actionGenreSelected").getValue();
                        hasSelectedDrama = (Boolean) task.getResult().child("genres").child("dramaGenreSelected").getValue();
                        hasSelectedAnimation = (Boolean) task.getResult().child("genres").child("animationGenreSelected").getValue();
                        hasSelectedCrime = (Boolean) task.getResult().child("genres").child("crimeGenreSelected").getValue();
                        hasSelectedDocumentary = (Boolean) task.getResult().child("genres").child("documentaryGenreSelected").getValue();
                        hasSelectedHistory= (Boolean) task.getResult().child("genres").child("historyGenreSelected").getValue();
                        hasSelectedHorror = (Boolean) task.getResult().child("genres").child("horrorGenreSelected").getValue();
                        hasSelectedRomance = (Boolean) task.getResult().child("genres").child("romanceGenreSelected").getValue();
                        hasSelectedSciFi = (Boolean) task.getResult().child("genres").child("sciFiGenreSelected").getValue();

                        getDisplayedMovies();
                    } catch (NullPointerException e) {
                        Log.e("firebase", "Genre error", task.getException());
                    }
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
                }
            });
        } else {
            createRecyclerView();
        }
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
            Log.e("CURRENT_USER", currentUser);
            if (savedInstanceState.containsKey("moviesCount")) {
                int size = savedInstanceState.getInt("moviesCount");

                if (movieList == null || movieList.size() == 0) {
                    for (int i = 0; i < size; i++) {
                        int keyInt = i+1;
                        String key = Integer.toString(keyInt);
                        String movieId = savedInstanceState.getString("MovieId"+key);
                        String poster = savedInstanceState.getString("MoviePoster"+key);
                        String title = savedInstanceState.getString("MovieTitle"+key);
                        String releaseDate = savedInstanceState.getString("ReleaseDate"+key);
                        String description = savedInstanceState.getString("Description"+key);

                        MovieCard movie = new MovieCard(movieId, title, description, releaseDate, poster);
                        assert movieList != null;
                        movieList.add(movie);
                    }
                }
            }
            if (savedInstanceState.containsKey("reviewsCount")) {
                int size = savedInstanceState.getInt("reviewsCount");

                if (relevantReviews == null || relevantReviews.size() == 0) {
                    for (int i = 0; i < size; i++) {
                        int keyInt = i+1;
                        String key = Integer.toString(keyInt);
                        String reviewId = savedInstanceState.getString("ReviewId"+key);
                        String movieTitle = savedInstanceState.getString("MovieTitle"+key);
                        String reviewTitle = savedInstanceState.getString("ReviewTitle"+key);
                        String reviewDate = savedInstanceState.getString("ReviewDate"+key);
                        String author = savedInstanceState.getString("Author"+key);

                        ReviewCard review = new ReviewCard(reviewId, author, movieTitle, reviewTitle, reviewDate);
                        relevantReviews.add(review);
                    }
                }
            }
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

    private void getDisplayedMovies() {
        // Get 3 random genres that are liked by user
        ArrayList<Integer> likedGenres = new ArrayList<>();
        if (hasSelectedComedy) {
            likedGenres.add(35);
        } if (hasSelectedAction) {
            likedGenres.add(28);
        } if (hasSelectedDrama) {
            likedGenres.add(18);
        }
        if (hasSelectedCrime) {
            likedGenres.add(80);
        }
        if (hasSelectedAnimation) {
            likedGenres.add(16);
        }
        if (hasSelectedDocumentary) {
            likedGenres.add(99);
        }
        if (hasSelectedHistory) {
            likedGenres.add(36);
        }
        if (hasSelectedHorror) {
            likedGenres.add(27);
        }
        if (hasSelectedRomance) {
            likedGenres.add(10749);
        }
        if (hasSelectedSciFi) {
            likedGenres.add(878);
        }

        Random rand = new Random();
        int randomGenre =  likedGenres.get(rand.nextInt(likedGenres.size()));

        // Make API call
        String urlStr = "https://api.themoviedb.org/3/discover/movie?api_key=eea1a7fc0d5c72b36736e248dc5e2693&language=en-US&with_genres=" + randomGenre;
        Log.e("URL", urlStr);
        Thread thread = new Thread(() -> {
            JSONObject jObject = new JSONObject();
            try {
                URL url = new URL(urlStr);
                Log.e(TAG, urlStr);
                HttpURLConnection req = (HttpURLConnection) url.openConnection();
                req.setRequestMethod("GET");
                req.setDoInput(true);
                req.connect();

                Scanner s = new Scanner(req.getInputStream()).useDelimiter("\\A");
                String resp = s.hasNext() ? s.next() : "";
                jObject = new JSONObject(resp);
                Log.e("RESPONSE", String.valueOf(jObject));
                JSONArray jArray = jObject.getJSONArray("results");
                for (int i = 0; i < Math.min(3, jArray.length()); i++) {
                    JSONObject result = jArray.getJSONObject(i);
                    String movieID = result.getString("id");
                    String posterPath = result.getString("poster_path").contains("/") ? "https://image.tmdb.org/t/p/original" + result.getString("poster_path") : "https://i.imgur.com/HGjprLt.jpeg";
                    String movieTitle = !result.getString("title").equals("") ? result.getString("title") : "No Title";
                    String releaseDate = !result.getString("release_date").equals("") ? "Released: " + result.getString("release_date") : "No Release Date";
                    String description = !result.getString("overview").equals("") ? result.getString("overview") : "No description";
                    textHandler.post(() -> addMovieToRecyclerView(movieID, movieTitle, description, releaseDate, posterPath));
                }


            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "JSONException");
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void addMovieToRecyclerView(String movieId, String movieTitle, String description, String releaseDate, String posterPath) {
        MovieCard newMovie = new MovieCard(movieId, movieTitle, description, releaseDate, posterPath);
        Log.e("MOVIE_CARD", movieTitle);
        movieList.add(newMovie);
        createRecyclerView();
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
                    String reviewId = String.valueOf(dschild.getKey());
                    if (usersFollowing.contains(author)) {
                        String movieId = String.valueOf(dschild.child("movieID").getValue());
                        try {
                            String movieTitle = String.valueOf(dschild.child("movieTitle").getValue());
                            String reviewTitle = String.valueOf(dschild.child("reviewTitle").getValue());
                            String reviewDate = dschild.getKey();
                            ReviewCard review = new ReviewCard(reviewId, author, movieTitle, reviewTitle, reviewDate);
                            System.out.println("MOVIE TITLE LENGTH: " + movieTitle.length());
                            // This is for old reviews that don't include the movie title
                            if (movieTitle.equals("null")) {
                                review = new ReviewCard(reviewId, author, movieId, reviewTitle, reviewDate);
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
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentUser", currentUser);

        int moviesCount = 0;
        if (movieList != null) {
            moviesCount = movieList.size();
        }

        outState.putInt("moviesCount", moviesCount);

        for (int i = 0; i < moviesCount; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putString("MovieId" + key, movieList.get(i).movieId);
            outState.putString("MoviePoster" + key, movieList.get(i).poster);
            outState.putString("MovieTitle" + key, movieList.get(i).title);
            outState.putString("ReleaseDate" + key, movieList.get(i).releaseDate);
            outState.putString("Description" + key, movieList.get(i).description);
        }

        int reviewsCount = 0;
        if (relevantReviews != null) {
            reviewsCount = relevantReviews.size();
        }

        outState.putInt("reviewsCount", reviewsCount);

        for (int i = 0; i < reviewsCount; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            ReviewCard review = relevantReviews.get(i);
            outState.putString("MovieTitle" + key, review.movieTitle);
            outState.putString("ReviewTitle" + key, review.reviewTitle);
            outState.putString("ReviewDate" + key, review.reviewDate);
            outState.putString("ReviewId" + key, review.reviewId);
            outState.putString("Author" + key, review.username);
        }
    }
}