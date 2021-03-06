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

public class HomePageReviewsAdapter extends RecyclerView.Adapter<HomePageReviewsAdapter.ReviewsViewHolder> {

    private final ArrayList<ReviewCard> reviewList;
    private final Context context;

    public HomePageReviewsAdapter(ArrayList<ReviewCard> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_card_homepage, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        ReviewCard currentReview = reviewList.get(position);
        holder.reviewTitle.setText(currentReview.username + " reviewed " + currentReview.movieTitle);
        holder.reviewDate.setText("Reviewed on " + currentReview.reviewDate);
        holder.reviewPreview.setText(currentReview.reviewTitle);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewTitle;
        public TextView reviewDate;
        public TextView reviewPreview;
        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewTitle = itemView.findViewById(R.id.usernameFeedText);
            reviewDate = itemView.findViewById(R.id.reviewDateFeedText);
            reviewPreview = itemView.findViewById(R.id.reviewPreviewText);
        }
    }
}
