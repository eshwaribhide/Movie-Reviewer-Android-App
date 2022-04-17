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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileReviewsFragment extends Fragment {

    // static data for testing
    ReviewCard review1 = new ReviewCard("Test123", "Some movie", "It was good");
    ReviewCard review2 = new ReviewCard("Test123", "Some other movie", "It was fine");
    ArrayList<ReviewCard> testReviews = new ArrayList<>(Arrays.asList(review1, review2));

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileReviewsFragment newInstance(String param1, String param2) {
        ProfileReviewsFragment fragment = new ProfileReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_reviews, container, false);
        recyclerView = view.findViewById(R.id.profile_recycler_view);
        HomePageReviewsAdapter adapter = new HomePageReviewsAdapter(testReviews, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

}