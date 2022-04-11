package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.MovieListRecyclerViewHolder> {

    private final ArrayList<MovieListActivity.MovieItem> movieItems;
    private ListItemClickListener listener;
    private final Context context;

    public MovieListRecyclerViewAdapter(Context context, ArrayList<MovieListActivity.MovieItem> movieItems) {
        this.movieItems = movieItems;
        this.context = context;
    }

    public class MovieListRecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView movieReleaseDate;
        public RelativeLayout movieItemLayout;

        public MovieListRecyclerViewHolder(View itemView, final ListItemClickListener listener) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_details_title);
            movieReleaseDate = itemView.findViewById(R.id.movie_details_release_date);
            movieItemLayout = itemView.findViewById(R.id.movie_item_layout);
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

        // Send to movie details page when clicked
        holder.movieItemLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("currentUser", movieItems.get(position).getCurrentUser());
            intent.putExtra("movie_id", movieItems.get(position).getMovieID());
            intent.putExtra("movie_title", movieItems.get(position).getMovieTitle());
            intent.putExtra("movie_poster", movieItems.get(position).getMoviePoster());
            intent.putExtra("movie_release_date", movieItems.get(position).getMovieReleaseDate());
            intent.putExtra("movie_description", movieItems.get(position).getMovieDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }
}
