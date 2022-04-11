package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MovieDetailsRecyclerViewAdapter extends RecyclerView.Adapter<MovieDetailsRecyclerViewAdapter.MovieDetailsRecyclerViewHolder> {

    private final ArrayList<MovieDetailsActivity.ReviewItem> ReviewItems;
    private ListItemClickListener listener;
    private final Context context;

    public MovieDetailsRecyclerViewAdapter(Context context, ArrayList<MovieDetailsActivity.ReviewItem> ReviewItems) {
        this.ReviewItems = ReviewItems;
        this.context = context;
    }

    public class MovieDetailsRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewTitle;
        public TextView reviewContent;

        public MovieDetailsRecyclerViewHolder(View itemView, final ListItemClickListener listener) {
            super(itemView);
            reviewTitle = itemView.findViewById(R.id.review_title);
            reviewContent = itemView.findViewById(R.id.review_content);
        }
    }

    @NonNull
    @Override
    public MovieDetailsRecyclerViewAdapter.MovieDetailsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new MovieDetailsRecyclerViewAdapter.MovieDetailsRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MovieDetailsRecyclerViewAdapter.MovieDetailsRecyclerViewHolder holder, int position) {
        holder.reviewTitle.setText(ReviewItems.get(position).getreviewTitle());
        holder.reviewContent.setText(ReviewItems.get(position).getreviewContent());

    }

    @Override
    public int getItemCount() {
        return ReviewItems.size();
    }

    public void setOnItemClickListener(ListItemClickListener listener) {
        this.listener = listener;
    }
}

