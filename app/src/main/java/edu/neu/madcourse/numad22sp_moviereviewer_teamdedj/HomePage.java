package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

public class HomePage extends AppCompatActivity {
    private String currentUser;

    // static data for testing
    private MovieCard movie1 = new MovieCard(1, 10, "12 Angry Men", "Description");
    private MovieCard movie2 = new MovieCard(2, 5, "21 Jump Street", "Description");;
    private MovieCard movie3 = new MovieCard(3, 23, "Bohemian Rhapsody", "Description");

    private ArrayList<MovieCard> movieList = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayout;
    private HomePageAdapter homePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initSavedInstanceState(savedInstanceState);
        createRecyclerView();
    }

    private void createRecyclerView() {
        linearLayout = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        homePageAdapter = new HomePageAdapter(movieList, this);
        recyclerView.setAdapter(homePageAdapter);
        recyclerView.setLayoutManager(linearLayout);
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
}