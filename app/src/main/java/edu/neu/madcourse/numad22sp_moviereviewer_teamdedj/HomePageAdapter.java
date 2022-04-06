package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MoviesViewHolder> {

    private final ArrayList<MovieCard> movieList;
    private Context context;

    public HomePageAdapter(ArrayList<MovieCard> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView reviewCount;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            reviewCount = itemView.findViewById(R.id.reviewCountText);
        }
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_card, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        MovieCard currentMovie = movieList.get(position);
        // this is a dummy image; will change this
        holder.moviePoster.setImageResource(R.drawable.movie);
        holder.movieTitle.setText(currentMovie.title);
        holder.reviewCount.setText(currentMovie.reviewCount + "reviews");
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
