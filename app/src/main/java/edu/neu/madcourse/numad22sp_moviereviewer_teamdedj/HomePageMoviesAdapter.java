package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePageMoviesAdapter extends RecyclerView.Adapter<HomePageMoviesAdapter.MoviesViewHolder> {

    private final ArrayList<MovieCard> movieList;
    private final Context context;

    public HomePageMoviesAdapter(ArrayList<MovieCard> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView movieDescription;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDescription = itemView.findViewById(R.id.movieDescription);
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
        Picasso.get().load(currentMovie.poster).into(holder.moviePoster);
        holder.movieTitle.setText(currentMovie.title);

        // Set a word limit of 20 for description
        String originalDesc = currentMovie.description;
        String formattedDesc = getFormattedDescription(originalDesc);
        holder.movieDescription.setText(formattedDesc);
    }

    private String getFormattedDescription(String description) {
        String[] words = description.split("\\s+", 21);
        int wordCount = words.length;

        if (wordCount <= 20) {
            return description;
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                result.append(words[i]).append(" ");
            }
            result.append("...");
            return result.toString();
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
