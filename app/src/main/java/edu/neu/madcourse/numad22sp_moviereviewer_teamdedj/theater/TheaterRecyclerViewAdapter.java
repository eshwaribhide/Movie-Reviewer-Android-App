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

public class TheaterRecyclerViewAdapter extends RecyclerView.Adapter<TheaterRecyclerViewAdapter.TheaterViewHolder> {

    private final ArrayList<LeaderboardActivity.LeaderboardItem> theaterItems;
    private Context context;

    public TheaterRecyclerViewAdapter(Context context, ArrayList<LeaderboardActivity.LeaderboardItem> theaterItems) {
        this.theaterItems = theaterItems;
        this.context = context;
    }

    public class TheaterViewHolder extends RecyclerView.ViewHolder {
        public ImageView badgeImage;
        public ImageView profilePic;
        public TextView userName;
        public TextView badgeLevelText;

        public TheaterViewHolder(View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badge_image);
            userName = itemView.findViewById(R.id.user_name);
            badgeLevelText = itemView.findViewById(R.id.badge_level_text);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_card, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        holder.userName.setText(theaterItems.get(position).getUserName());
        holder.profilePic.setImageResource(R.drawable.propic);

        switch (theaterItems.get(position).getBadgeLevel()) {
            case 1:
                holder.badgeImage.setImageResource(R.drawable.bronze_medal);
                holder.badgeLevelText.setText("Bronze level member");
                break;
            case 2:
                holder.badgeImage.setImageResource(R.drawable.silver_medal);
                holder.badgeLevelText.setText("Silver level member");
                break;
            case 3:
                holder.badgeImage.setImageResource(R.drawable.gold_medal);
                holder.badgeLevelText.setText("Gold level member");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return theaterItems.size();
    }
}