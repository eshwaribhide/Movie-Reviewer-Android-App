package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ArrayList<MovieCard> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayout;
    private HomePageAdapter homePageAdapter;

    private MovieCard movie1 = new MovieCard(1, 10, "12 Angry Men", "Description");
    private MovieCard movie2 = new MovieCard(2, 5, "21 Jump Street", "Description");;
    private MovieCard movie3 = new MovieCard(3, 23, "Bohemian Rhapsody", "Description");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
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