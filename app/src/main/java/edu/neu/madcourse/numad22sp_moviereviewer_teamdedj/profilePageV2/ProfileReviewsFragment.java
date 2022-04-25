package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.HomePageReviewsAdapter;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.Review;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.ReviewCard;

public class ProfileReviewsFragment extends Fragment {

    private ArrayList<ReviewCard> userReviews = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomePageReviewsAdapter adapter;
    private DatabaseReference mDatabase;
    private String currentUser;
    private String searchedUser;

    public ProfileReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_reviews, container, false);
        recyclerView = view.findViewById(R.id.profile_recycler_view);

        if (getArguments() != null) {
            userReviews = getArguments().getParcelableArrayList("userReviews");
            currentUser = getArguments().getString("currentUser");
            searchedUser = getArguments().getString("searchedUser");
        }

        adapter = new HomePageReviewsAdapter(userReviews, getContext());
        // Enable swipe capability if the user is on their own profile
        if (currentUser.equals(searchedUser)) {
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Delete the review locally
            ReviewCard currentReview = userReviews.get(viewHolder.getAdapterPosition());
            userReviews.remove(currentReview);
            // Delete the review from the database
            mDatabase.child("reviews").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting reviews data in reviews fragment", task.getException());
                } else {
                    mDatabase.child("reviews").child(currentReview.reviewId).removeValue();
                }
            });
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Removed " + currentReview.reviewId, Toast.LENGTH_SHORT).show();

            // decrement review count
            mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    int reviewCount = Integer.parseInt(String.valueOf(task.getResult().child("reviewCount").getValue()));
                    int newReviewCount = reviewCount - 1;
                    // Change user's status
                    if (newReviewCount <20) {
                        if (newReviewCount >=10) {
                            mDatabase.child("users").child(currentUser).child("badgeStatus").setValue("Silver");
                        }
                        else {
                            mDatabase.child("users").child(currentUser).child("badgeStatus").setValue("Bronze");
                        }
                    }
                    mDatabase.child("users").child(currentUser).child("reviewCount").setValue(newReviewCount);
                }
            });
        }
    };
}