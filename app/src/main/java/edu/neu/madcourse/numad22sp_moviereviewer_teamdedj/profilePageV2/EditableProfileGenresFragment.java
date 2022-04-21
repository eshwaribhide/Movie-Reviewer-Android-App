package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

public class EditableProfileGenresFragment extends Fragment {

    private boolean comedyChecked = false;
    private boolean actionChecked = false;
    private boolean dramaChecked = false;
    private boolean animationChecked = false;
    private boolean crimeChecked = false;
    private boolean horrorChecked = false;
    private boolean romanceChecked = false;
    private boolean sciFiChecked  = false;
    private boolean documentaryChecked  = false;
    private boolean historyChecked  = false;
    private Button saveChangesButton;
    private String currentUser;
    private DatabaseReference mDatabase;

    private CheckBox comedyCheckbox;
    private CheckBox actionCheckbox;
    private CheckBox dramaCheckbox;
    private CheckBox animationCheckbox;
    private CheckBox crimeCheckbox;
    private CheckBox horrorCheckbox;
    private CheckBox romanceCheckbox;
    private CheckBox sciFiCheckbox;
    private CheckBox documentaryCheckbox;
    private CheckBox historyCheckbox;

    public EditableProfileGenresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = getArguments().getString("currentUser");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editable_profile_genres, container, false);

        saveChangesButton = view.findViewById(R.id.saveProfileChangesButton);
        comedyCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_comedy_checkbox);
        actionCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_action_checkbox);
        dramaCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_drama_checkbox);
        animationCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_animation_checkbox);
        crimeCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_crime_checkbox);
        horrorCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_horror_checkbox);
        romanceCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_romance_checkbox);
        sciFiCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_scifi_checkbox);
        documentaryCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_documentary_checkbox);
        historyCheckbox = (CheckBox) view.findViewById(R.id.editable_profile_history_checkbox);

        if (getArguments() != null) {
            setCheckboxes(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the current user's genre preferences in firebase
                mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting user from genres fragment", task.getException());
                    } else {
                        mDatabase.child("users").child(currentUser).child("genres").child("comedyGenreSelected").setValue(comedyCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("actionGenreSelected").setValue(actionCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("dramaGenreSelected").setValue(dramaCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("animationGenreSelected").setValue(animationCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("crimeGenreSelected").setValue(crimeCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("horrorGenreSelected").setValue(horrorCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("romanceGenreSelected").setValue(romanceCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("sciFiGenreSelected").setValue(sciFiCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("documentaryGenreSelected").setValue(documentaryCheckbox.isChecked());
                        mDatabase.child("users").child(currentUser).child("genres").child("historyGenreSelected").setValue(historyCheckbox.isChecked());
                    }
                });
                Toast.makeText(getActivity(), "Your changes have been saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCheckboxes(View view) {
        comedyChecked = getArguments().getBoolean("comedyChecked");
        actionChecked = getArguments().getBoolean("actionChecked");
        dramaChecked = getArguments().getBoolean("dramaChecked");
        animationChecked = getArguments().getBoolean("animationChecked");
        crimeChecked = getArguments().getBoolean("crimeChecked");
        horrorChecked = getArguments().getBoolean("horrorChecked");
        romanceChecked = getArguments().getBoolean("romanceChecked");
        sciFiChecked = getArguments().getBoolean("sciFiChecked");
        documentaryChecked = getArguments().getBoolean("documentaryChecked");
        historyChecked = getArguments().getBoolean("historyChecked");

        comedyCheckbox.setChecked(comedyChecked);
        actionCheckbox.setChecked(actionChecked);
        dramaCheckbox.setChecked(dramaChecked);
        animationCheckbox.setChecked(animationChecked);
        crimeCheckbox.setChecked(crimeChecked);
        horrorCheckbox.setChecked(horrorChecked);
        romanceCheckbox.setChecked(romanceChecked);
        sciFiCheckbox.setChecked(sciFiChecked);
        documentaryCheckbox.setChecked(documentaryChecked);
        historyCheckbox.setChecked(historyChecked);
    }
}