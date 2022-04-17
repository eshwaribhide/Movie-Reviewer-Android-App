package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.HomePageReviewsAdapter;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.ReviewCard;

// TODO: save data on screen orientation change
public class ProfileReviewsFragment extends Fragment {

    private ArrayList<ReviewCard> userReviews = new ArrayList<>();

    private RecyclerView recyclerView;

    public ProfileReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_reviews, container, false);
        recyclerView = view.findViewById(R.id.profile_recycler_view);

        if (getArguments() != null) {
            userReviews = getArguments().getParcelableArrayList("userReviews");
        }

        HomePageReviewsAdapter adapter = new HomePageReviewsAdapter(userReviews, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

}