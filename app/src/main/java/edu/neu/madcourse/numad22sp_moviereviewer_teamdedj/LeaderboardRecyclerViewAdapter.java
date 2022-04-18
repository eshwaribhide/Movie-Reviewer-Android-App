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

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder>{
    private final ArrayList<LeaderboardActivity.LeaderboardItem> leaderboardItems;
    private final Context context;

    public LeaderboardRecyclerViewAdapter(Context context, ArrayList<LeaderboardActivity.LeaderboardItem> leaderboardItems) {
        this.leaderboardItems = leaderboardItems;
        this.context = context;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        public ImageView badgeImage;
        public TextView userName;
        public TextView reviewCount;
        public TextView positionNumber;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.badge_image);
            userName = itemView.findViewById(R.id.user_name);
            reviewCount = itemView.findViewById(R.id.review_count);
            positionNumber = itemView.findViewById(R.id.position_number);
        }
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_card, parent, false);
        return new LeaderboardRecyclerViewAdapter.LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        holder.userName.setText(leaderboardItems.get(position).getUserName());
        int reviewCount = leaderboardItems.get(position).getUserReviewCount();
        holder.reviewCount.setText("Reviews: " + reviewCount);
        int currentPosition = position + 1;
        holder.positionNumber.setText(currentPosition + ".");

        switch (leaderboardItems.get(position).getBadgeLevel()) {
            case "Bronze":
                holder.badgeImage.setImageResource(R.drawable.bronze_medal);
                break;
            case "Silver":
                holder.badgeImage.setImageResource(R.drawable.silver_medal);
                break;
            case "Gold":
                holder.badgeImage.setImageResource(R.drawable.gold_medal);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }
}
