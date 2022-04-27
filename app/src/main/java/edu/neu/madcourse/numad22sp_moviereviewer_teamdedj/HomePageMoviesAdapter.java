package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        public CardView movieCardContainer;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDescription = itemView.findViewById(R.id.movieDescription);
            movieCardContainer = itemView.findViewById(R.id.movie_card_container);
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

        holder.movieCardContainer.setOnClickListener(view -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            // need current user
            intent.putExtra("currentUser", movieList.get(position).currentUser);
            intent.putExtra("movie_id", movieList.get(position).movieId);
            intent.putExtra("movie_title", movieList.get(position).title);
            intent.putExtra("movie_poster", movieList.get(position).poster);
            intent.putExtra("movie_release_date", movieList.get(position).releaseDate);
            intent.putExtra("movie_description", movieList.get(position).description);
            context.startActivity(intent);
        });
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
