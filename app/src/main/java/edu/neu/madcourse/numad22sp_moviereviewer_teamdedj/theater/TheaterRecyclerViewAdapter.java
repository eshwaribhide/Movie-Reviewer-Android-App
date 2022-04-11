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
        public ImageView theaterImage;
        public TextView theaterName;
        public TextView theaterAddress;
        public TextView theaterDistance;
        public RatingBar theaterRating;

        public TheaterViewHolder(View view) {
            super(view);
            theaterImage = view.findViewById(R.id.theater_image);
            theaterName = view.findViewById(R.id.theater_name);
            theaterAddress = view.findViewById(R.id.theater_address);
            theaterDistance = view.findViewById(R.id.theater_distance);
            theaterRating = view.findViewById(R.id.theater_rating);
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

        holder.theaterImage.setImageResource(R.drawable.propic);

        holder.theaterName.setText(theaterItems.get(position).getTheaterName());
        holder.theaterAddress.setText(theaterItems.get(position).getTheaterAddress());
        holder.theaterDistance.setText(theaterItems.get(position).getTheaterDistance());;
        holder.theaterRating.setRating(theaterItems.get(position).getTheaterRating());
    }

    @Override
    public int getItemCount() {
        return theaterItems.size();
    }
}
