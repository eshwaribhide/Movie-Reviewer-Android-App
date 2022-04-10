package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieListActivity extends AppCompatActivity {
    private static final String TAG = "MovieListActivity";
    private RecyclerView recyclerView;
    private MovieListRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<MovieListActivity.MovieItem> MovieItems = new ArrayList<>();
    private Handler textHandler = new Handler();
    private ProgressBar spinner;
    private EditText searchInputBox;
    private boolean moviesSearched = false;


    public static class MovieItem {
        private final String movieID;
        private final String moviePoster;
        private final String movieTitle;
        private final String movieReleaseDate;
        private final String movieDescription;


        public MovieItem(String movieID, String moviePoster, String movieTitle, String movieReleaseDate, String movieDescription) {
            this.movieID = movieID;
            this.moviePoster = moviePoster;
            this.movieTitle = movieTitle;
            this.movieReleaseDate = movieReleaseDate;
            this.movieDescription = movieDescription;
        }
        public String getMovieID() {
            return movieID;
        }

        public String getMoviePoster() {
            return moviePoster;
        }

        public String getMovieTitle() {
            return movieTitle;
        }

        public String getMovieReleaseDate() {
            return movieReleaseDate;
        }

        public String getMovieDescription() { return movieDescription; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        initSavedInstanceState(savedInstanceState);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        searchInputBox = findViewById(R.id.searchInputBox);
        searchInputBox.setHint("Search By Movie Name");
    }

    private void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("LengthMovieItems")) {
            moviesSearched = savedInstanceState.getBoolean("MoviesSearched");


            if (MovieItems == null || MovieItems.size() == 0) {
                int size = savedInstanceState.getInt("LengthMovieItems");


                for (int i = 0; i < size; i++) {
                    int keyInt = i + 1;
                    String key = Integer.toString(keyInt);
                    String movieID = savedInstanceState.getString("MovieIDKey"+ key);
                    String moviePoster = savedInstanceState.getString("MoviePosterKey"+ key);
                    String movieTitle = savedInstanceState.getString("MovieTitleKey" + key);
                    String movieReleaseDate = savedInstanceState.getString("MovieReleaseDateKey" + key);
                    String movieDescription = savedInstanceState.getString("MovieDescription" + key);

                    MovieListActivity.MovieItem MovieItem = new MovieListActivity.MovieItem(movieID, moviePoster, movieTitle, movieReleaseDate, movieDescription);

                    MovieItems.add(MovieItem);
                }
            }


        }

    }

    private void addMovieToRecyclerView(String movieID, String imageURL, String movieTitle, String movieReleaseDate,
                                        String movieDescription) {
        // For search box clear need to refresh RecyclerView
        if (MovieItems.size() == 0) {
            generateRecyclerView();
        }
        recyclerViewLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
        MovieItems.add(0, new MovieListActivity.MovieItem(movieID, imageURL, movieTitle, movieReleaseDate, movieDescription));
        recyclerViewAdapter.notifyItemInserted(0);
    }

    private void generateRecyclerView() {
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new MovieListRecyclerViewAdapter(this, MovieItems);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
        generateRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = 0;

        if (MovieItems != null) {
            size = MovieItems.size();
        }
        outState.putInt("LengthMovieItems", size);
        outState.putBoolean("MoviesSearched", moviesSearched);

        for (int i = 0; i < size; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putString("MovieIDKey" + key, MovieItems.get(i).getMovieID());
            outState.putString("MoviePosterKey" + key, MovieItems.get(i).getMoviePoster());
            outState.putString("MovieTitleKey" + key, MovieItems.get(i).getMovieTitle());
            outState.putString("MovieReleaseDateKey" + key, MovieItems.get(i).getMovieReleaseDate());
            outState.putString("MovieDescription" + key, MovieItems.get(i).getMovieDescription());
        }
        super.onSaveInstanceState(outState);

    }

    public void searchButtonOnClick(View view) {
        if (searchInputBox.getText().toString().matches("")) {
            Snackbar.make(view, "Please Enter Movie Title", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            // For search box clear need to refresh RecyclerView
            if (MovieItems.size() > 0) {
                MovieItems = new ArrayList<>();
            }
            moviesSearched = true;
            spinner.setVisibility(View.VISIBLE);
            String movieName = searchInputBox.getText().toString();
            String urlStr = "https://api.themoviedb.org/3/search/movie?api_key=eea1a7fc0d5c72b36736e248dc5e2693&language=en-US&query=" + movieName + "&include_adult=false";
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
                    for (int i = 0; i < Math.min(5, jArray.length()); i++) {
                        JSONObject result = jArray.getJSONObject(i);
                        String movieID = result.getString("id");
                        String posterPath = result.getString("poster_path").contains("/") ? "https://image.tmdb.org/t/p/original" + result.getString("poster_path") : "https://i.imgur.com/HGjprLt.jpeg";
                        String movieTitle = !result.getString("original_title").equals("") ? result.getString("original_title") : "No Title";
                        String releaseDate = !result.getString("release_date").equals("") ? "Released: " + result.getString("release_date") : "No Release Date";
                        String description = !result.getString("overview").equals("") ? result.getString("overview") : "No description";
                        textHandler.post(() -> addMovieToRecyclerView(movieID, posterPath, movieTitle, releaseDate, description));
                    }


                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException");
                    e.printStackTrace();
                    Snackbar.make(view, "Failed with MalformedURLException", BaseTransientBottomBar.LENGTH_LONG).show();
                } catch (ProtocolException e) {
                    Log.e(TAG, "ProtocolException");
                    e.printStackTrace();
                    Snackbar.make(view, "Failed with ProtocolException", BaseTransientBottomBar.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                    Snackbar.make(view, "Failed with IOException", BaseTransientBottomBar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException");
                    e.printStackTrace();
                    Snackbar.make(view, "Failed with JSONException", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                textHandler.post(() -> spinner.setVisibility(View.GONE));
            });
            thread.start();
        }
    }
}