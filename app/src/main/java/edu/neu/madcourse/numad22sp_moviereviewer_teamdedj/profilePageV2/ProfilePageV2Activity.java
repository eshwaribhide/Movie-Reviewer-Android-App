package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

public class ProfilePageV2Activity extends AppCompatActivity {
    private String currentUser;
    private DatabaseReference mDatabase;
    private TextView fullNameValue;
    private TextView followersValue;
    private TextView followingValue;

    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
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

                // Get count of following
                int followingCount = 0;
                for (DataSnapshot dschild: task.getResult().child("following").getChildren()) {
                    followingCount++;
                }
                followingValue.setText(String.valueOf(followingCount));

                // Get count of reviews
                String reviewsTabTitle = String.valueOf(task.getResult().child("reviewCount").getValue());

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

                ProfileReviewsFragment reviewsFragment = new ProfileReviewsFragment();

                vpAdapter.addFragment(genresFragment, "GENRES");
                vpAdapter.addFragment(reviewsFragment, "REVIEWS (" + reviewsTabTitle + ")");
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
            }
        }
    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        initData(savedInstanceState);
    }
}