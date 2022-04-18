package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;
import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.ReviewCard;

public class ProfilePageV2Activity extends AppCompatActivity {
    private String currentUser;
    private String searchedUser;
    private DatabaseReference mDatabase;
    private TextView fullNameValue;
    private TextView followersValue;
    private TextView followingValue;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button followButton;
    private Button unfollowButton;
    private boolean isUserFollowingProfile = false;

    private final ArrayList<String> bundleKeys = new ArrayList<>(
            Arrays.asList("comedyChecked", "actionChecked", "dramaChecked", "animationChecked",
                    "crimeChecked", "horrorChecked", "romanceChecked", "sciFiChecked",
                    "documentaryChecked", "historyChecked")
    );

    private final ArrayList<String> firebaseKeys = new ArrayList<>(
            Arrays.asList("comedyGenreSelected", "actionGenreSelected", "dramaGenreSelected",
                    "animationGenreSelected", "crimeGenreSelected", "horrorGenreSelected",
                    "romanceGenreSelected", "sciFiGenreSelected", "documentaryGenreSelected",
                    "historyGenreSelected")
    );

    private ArrayList<ReviewCard> userReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove the top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_profile_page_v2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initSavedInstanceState(savedInstanceState);

        fullNameValue = findViewById(R.id.user_full_name_text);
        followersValue = findViewById(R.id.followers_count);
        followingValue = findViewById(R.id.following_count);

        tabLayout = findViewById(R.id.profile_tab_layout);
        viewPager = findViewById(R.id.profile_view_pager);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mDatabase.child("users").child(searchedUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                // Get user full name
                fullNameValue.setText(String.valueOf(task.getResult().child("fullName").getValue()));

                // Get count of followers
                int followersCount = 0;
                for (DataSnapshot dschild : task.getResult().child("followers").getChildren()) {
                    followersCount++;
                }
                followersValue.setText(String.valueOf(followersCount));

                // Get count of following and determine if currentUser is following searchedUser
                int followingCount = 0;
                for (DataSnapshot dschild: task.getResult().child("following").getChildren()) {
                    followingCount++;
                }
                followingValue.setText(String.valueOf(followingCount));

                // Add the genre fragment with the user data
                Bundle genreBundle = new Bundle();
                for (int i = 0; i < bundleKeys.size(); i++) {
                    String bundleKey = bundleKeys.get(i);
                    String firebaseKey = firebaseKeys.get(i);
                    try {
                        genreBundle.putBoolean(bundleKey, (Boolean) task.getResult().child("genres").child(firebaseKey).getValue());
                    } catch (NullPointerException e) {
                        genreBundle.putBoolean(bundleKey, false);
                    }
                }

                ProfileGenresFragment genresFragment = new ProfileGenresFragment();
                genresFragment.setArguments(genreBundle);

                vpAdapter.addFragment(genresFragment, "GENRES");
                viewPager.setAdapter(vpAdapter);
            }
        });

        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            followButton = findViewById(R.id.followButton);
            unfollowButton = findViewById(R.id.unfollowButton);
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                for (DataSnapshot dschild: task.getResult().child("following").getChildren()) {
                    if (String.valueOf(dschild.getValue()).equals(searchedUser)) {
                        unfollowButton.setVisibility(View.VISIBLE);
                        isUserFollowingProfile = true;
                        break;
                    }
                }
                if (!isUserFollowingProfile && !currentUser.equals(searchedUser)) {
                    followButton.setVisibility(View.VISIBLE);
                }
            }
        });

        mDatabase.child("reviews").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting reviews data", task.getException());
            } else {
                // Get all reviews written by the current user and save them in an array of ReviewCard objects
                for (DataSnapshot dschild: task.getResult().getChildren()) {
                    String author = String.valueOf(dschild.child("username").getValue());
                    if (author.equals(searchedUser)) {
                        String movieId = String.valueOf(dschild.child("movieID").getValue());
                        try {
                            String movieTitle = String.valueOf(dschild.child("movieTitle").getValue());
                            String reviewTitle = String.valueOf(dschild.child("reviewTitle").getValue());
                            ReviewCard review = new ReviewCard(author, movieTitle, reviewTitle);
                            if (movieTitle.equals("null")) {
                                review = new ReviewCard(author, movieId, reviewTitle);
                            }
                            userReviews.add(0, review);
                        } catch (NullPointerException e) {
                            Log.e("Firebase", e.getLocalizedMessage());
                        }
                    }
                }
                // Pass the array to the fragment
                Bundle reviewsBundle = new Bundle();
                reviewsBundle.putParcelableArrayList("userReviews", userReviews);

                ProfileReviewsFragment reviewsFragment = new ProfileReviewsFragment();
                reviewsFragment.setArguments(reviewsBundle);

                vpAdapter.addFragment(reviewsFragment, "REVIEWS (" + userReviews.size() + ")");
                viewPager.setAdapter(vpAdapter);
            }
        });
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("currentUser")) {
            currentUser = savedInstanceState.getString("currentUser");
        }
        else {
            Log.e("INITDATA", "INITDATA");
            Bundle b = getIntent().getExtras();
            if (b != null) {
                currentUser = b.getString("currentUser");
                searchedUser = b.getString("searchedUser");
            }
        }
    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }

    public void unfollowUserOnClick(View view) {
        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting user data", task.getException());
            } else {
                mDatabase.child("users").child(currentUser).child("following").child(searchedUser).removeValue();
                mDatabase.child("users").child(searchedUser).child("followers").child(currentUser).removeValue();
                Snackbar.make(view, "You have unfollowed " + searchedUser, BaseTransientBottomBar.LENGTH_LONG).show();
                // Update the UI
                unfollowButton.setVisibility(View.GONE);
                followButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void followUserOnClick(View view) {
        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting user data", task.getException());
            } else {
                mDatabase.child("users").child(currentUser).child("following").child(searchedUser).setValue(searchedUser);
                mDatabase.child("users").child(searchedUser).child("followers").child(currentUser).setValue(currentUser);
                Snackbar.make(view, "You are now following " + searchedUser, BaseTransientBottomBar.LENGTH_LONG).show();
                // Update the UI
                followButton.setVisibility(View.GONE);
                unfollowButton.setVisibility(View.VISIBLE);
            }
        });
    }
}