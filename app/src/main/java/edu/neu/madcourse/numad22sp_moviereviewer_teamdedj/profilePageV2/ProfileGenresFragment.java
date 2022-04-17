package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.profilePageV2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import edu.neu.madcourse.numad22sp_moviereviewer_teamdedj.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileGenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileGenresFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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


    public ProfileGenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileGenresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileGenresFragment newInstance(String param1, String param2) {
        ProfileGenresFragment fragment = new ProfileGenresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_genres, container, false);
        if (getArguments() != null) {
            setCheckboxes(view);
        }

        return view;
    }

    private void setCheckboxes(View view) {
        CheckBox comedyCheckbox = (CheckBox) view.findViewById(R.id.profile_comedy_checkbox);
        CheckBox actionCheckbox = (CheckBox) view.findViewById(R.id.profile_action_checkbox);
        CheckBox dramaCheckbox = (CheckBox) view.findViewById(R.id.profile_drama_checkbox);
        CheckBox animationCheckbox = (CheckBox) view.findViewById(R.id.profile_animation_checkbox);
        CheckBox crimeCheckbox = (CheckBox) view.findViewById(R.id.profile_crime_checkbox);
        CheckBox horrorCheckbox = (CheckBox) view.findViewById(R.id.profile_horror_checkbox);
        CheckBox romanceCheckbox = (CheckBox) view.findViewById(R.id.profile_romance_checkbox);
        CheckBox sciFiCheckbox = (CheckBox) view.findViewById(R.id.profile_scifi_checkbox);
        CheckBox documentaryCheckbox = (CheckBox) view.findViewById(R.id.profile_documentary_checkbox);
        CheckBox historyCheckbox = (CheckBox) view.findViewById(R.id.profile_history_checkbox);


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