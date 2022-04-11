package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class HomePage extends AppCompatActivity {

    // static data for testing
    private final MovieCard movie1 = new MovieCard(1, 10, "12 Angry Men", "Description");
    private final MovieCard movie2 = new MovieCard(2, 5, "21 Jump Street", "Description");
    private final MovieCard movie3 = new MovieCard(3, 23, "Bohemian Rhapsody", "Description");

    private final ArrayList<MovieCard> movieList = new ArrayList<>(Arrays.asList(movie1, movie2, movie3));
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayout;
    private HomePageAdapter homePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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
}