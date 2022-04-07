package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import java.util.Scanner;

public class MovieListActivity extends AppCompatActivity {
    private static final String TAG = "MovieListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
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
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject result = jArray.getJSONObject(i);
                            String title = result.getString("original_title");
                            Log.e("TITLE", title);
                }

//                    if (jokeNumberInt == 1) {
//                        String jokeSetup = jObject.getString("setup");
//                        String jokeDelivery = jObject.getString("delivery");
//                        String jokeCategory = jObject.getString("category");
//                        if (jokeCategory.equals("Christmas")) {
//                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.presents, jokeSetup, jokeDelivery));
//                        } else if (jokeCategory.equals("Pun")) {
//                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.facepalm, jokeSetup, jokeDelivery));
//                        } else {
//                            textHandler.post(() -> addJokeToRecyclerView(R.drawable.programmer, jokeSetup, jokeDelivery));
//                        }
//
//                    } else {
//                        Log.e(TAG, "Getting Jokes");
//                        JSONArray jArray = jObject.getJSONArray("jokes");
//                        for (int i = 0; i < jArray.length(); i++) {
//                            JSONObject joke = jArray.getJSONObject(i);
//                            String jokeSetup = joke.getString("setup");
//                            String jokeDelivery = joke.getString("delivery");
//                            String jokeCategory = joke.getString("category");
//                            if (jokeCategory.equals("Christmas")) {
//                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.presents, jokeSetup, jokeDelivery));
//                            } else if (jokeCategory.equals("Pun")) {
//                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.facepalm, jokeSetup, jokeDelivery));
//                            } else {
//                                textHandler.post(() -> addJokeToRecyclerView(R.drawable.programmer, jokeSetup, jokeDelivery));
//                            }
//                        }
//                    }

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