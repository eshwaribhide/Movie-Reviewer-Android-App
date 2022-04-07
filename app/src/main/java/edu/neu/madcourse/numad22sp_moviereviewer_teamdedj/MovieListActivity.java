package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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


    public static class MovieItem {
        private final String moviePoster;
        private final String movieTitle;
        private final String movieReleaseDate;


        public MovieItem(String moviePoster, String movieTitle, String movieReleaseDate) {
            this.moviePoster = moviePoster;
            this.movieTitle = movieTitle;
            this.movieReleaseDate = movieReleaseDate;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        initSavedInstanceState(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("LengthMovieItems")) {

            if (MovieItems == null || MovieItems.size() == 0) {
                int size = savedInstanceState.getInt("LengthMovieItems");


                for (int i = 0; i < size; i++) {
                    int keyInt = i + 1;
                    String key = Integer.toString(keyInt);
                    String moviePoster = savedInstanceState.getString("MoviePosterKey"+ key);
                    String movieTitle = savedInstanceState.getString("MovieTitleKey" + key);
                    String movieReleaseDate = savedInstanceState.getString("MovieReleaseDateKey" + key);

                    MovieListActivity.MovieItem MovieItem = new MovieListActivity.MovieItem(moviePoster, movieTitle, movieReleaseDate);

                    MovieItems.add(MovieItem);
                }
            }


        }

    }

    private void addMovieToRecyclerView(String imageURL, String movieTitle, String movieReleaseDate) {
        recyclerViewLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
        MovieItems.add(0, new MovieListActivity.MovieItem(imageURL, movieTitle, movieReleaseDate));
        recyclerViewAdapter.notifyItemInserted(0);
    }

    private void generateRecyclerView() {
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new MovieListRecyclerViewAdapter(MovieItems);

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

        for (int i = 0; i < size; i++) {
            int keyInt = i + 1;
            String key = Integer.toString(keyInt);
            outState.putString("MoviePosterKey" + key, MovieItems.get(i).getMoviePoster());
            outState.putString("MovieTitleKey" + key, MovieItems.get(i).getMovieTitle());
            outState.putString("MovieReleaseDateKey" + key, MovieItems.get(i).getMovieReleaseDate());
        }
        super.onSaveInstanceState(outState);

    }

    public void searchButtonOnClick(View view) {
            String urlStr = "https://api.themoviedb.org/3/search/movie?api_key=eea1a7fc0d5c72b36736e248dc5e2693&language=en-US&query=batman&include_adult=false";
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
                    // min of array length and 5
                        for (int i = 0; i < Math.min(5, jArray.length()); i++) {
                            JSONObject result = jArray.getJSONObject(i);
                            String posterPath = result.getString("poster_path");
                            String movieTitle = result.getString("original_title");
                            String releaseDate = "Released: " + result.getString("release_date");
                            Log.e("POSTER PATH", posterPath);
                            Log.e("MOVIE TITLE", movieTitle);
                            textHandler.post(() -> addMovieToRecyclerView("https://image.tmdb.org/t/p/original" + posterPath, movieTitle, releaseDate));
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
                //textHandler.post(() -> spinner.setVisibility(View.GONE));
            });
            thread.start();
        }
}