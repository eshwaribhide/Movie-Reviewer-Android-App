package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.MovieListRecyclerViewHolder> {

    private final ArrayList<MovieListActivity.MovieItem> movieItems;
    private ListItemClickListener listener;

    public MovieListRecyclerViewAdapter(ArrayList<MovieListActivity.MovieItem> movieItems) {
        this.movieItems = movieItems;
    }

    public class MovieListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView movieReleaseDate;

        public MovieListRecyclerViewHolder(View itemView, final ListItemClickListener listener) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieReleaseDate = itemView.findViewById(R.id.movie_release_date);
        }
    }

    @NonNull
    @Override
    public MovieListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieListRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MovieListRecyclerViewHolder holder, int position) {
        Picasso.get().load(movieItems.get(position).getMoviePoster()).into(holder.moviePoster);
        holder.movieTitle.setText(movieItems.get(position).getMovieTitle());
        holder.movieReleaseDate.setText(movieItems.get(position).getMovieReleaseDate());
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }
}