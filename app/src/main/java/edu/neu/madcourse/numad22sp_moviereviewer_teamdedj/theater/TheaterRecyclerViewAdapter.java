package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.theater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

/**
 * RecyclerView adapter for the theater list.
 */
public class TheaterRecyclerViewAdapter extends RecyclerView.Adapter<TheaterRecyclerViewAdapter.TheaterViewHolder> {

    private final ArrayList<TheaterNearMeActivity.TheaterItem> theaterItems;
    private final Context context;

    public TheaterRecyclerViewAdapter(Context context, ArrayList<TheaterNearMeActivity.TheaterItem> theaterItems) {
        this.theaterItems = theaterItems;
        this.context = context;
    }

    /**
     * Creates a new view holder.
     */
    public class TheaterViewHolder extends RecyclerView.ViewHolder {
        public TextView theaterName;
        public TextView theaterRatingValue;
        public RatingBar theaterRatingBar;
        public TextView theaterUserReviewCount;
        public TextView theaterDistance;
        public TextView theaterAddress;

        public TheaterViewHolder(View view) {
            super(view);
            theaterName = view.findViewById(R.id.theater_name);
            theaterRatingValue = view.findViewById(R.id.rating_value);
            theaterRatingBar = view.findViewById(R.id.theater_rating_bar);
            theaterUserReviewCount = view.findViewById(R.id.review_count);
            theaterDistance = view.findViewById(R.id.theater_distance);
            theaterAddress = view.findViewById(R.id.theater_address);
        }
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.theater_card, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {

        holder.theaterName.setText(theaterItems.get(position).getTheaterName());

        holder.theaterRatingValue.setText(String.valueOf(theaterItems.get(position).getTheaterRating()));
        holder.theaterRatingBar.setRating(theaterItems.get(position).getTheaterRating());

        String reviewCount = String.format(Locale.ENGLISH, "(%d)", theaterItems.get(position).getTheaterUserReviewCount());
        holder.theaterUserReviewCount.setText(reviewCount);

        System.out.println("theaterItems.get(position).getTheaterAddress() = " + theaterItems.get(position).getTheaterAddress());
        holder.theaterDistance.setText(theaterItems.get(position).getDistance());
        holder.theaterAddress.setText(theaterItems.get(position).getTheaterAddress());


    }

    @Override
    public int getItemCount() {
        return theaterItems.size();
    }
}
