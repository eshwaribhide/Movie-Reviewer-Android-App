package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginButtonOnClick(View view) {
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);

//        /////////////////This dialog is for login/////////////////
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Login");
//
//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final EditText editUsername = new EditText(this);
//        editUsername.setHint("Enter Username");
//        layout.addView(editUsername);
//
//        editUsername.setTextColor(Color.parseColor("#9C27B0"));
//
//        alertDialogBuilder.setView(layout);
//        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
//            // Would go to Feed screen
//        });
//
//        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        alertDialog.show();

    }

    public void signUpButtonOnClick(View view) {

        /////////////////This dialog is for signup/////////////////

        // perhaps need to make a layout and inflate later
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Sign Up");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editFullName = new EditText(this);
        editFullName.setHint("Enter Full Name");
        layout.addView(editFullName);

        final EditText editUsername = new EditText(this);
        editUsername.setHint("Enter Username");
        layout.addView(editUsername);

        editFullName.setTextColor(Color.parseColor("#9C27B0"));
        editUsername.setTextColor(Color.parseColor("#9C27B0"));

        final TextView genres = new TextView(this);
        genres.setText("Choose Genres");
        genres.setTextSize(20);
        genres.setTypeface(null, Typeface.BOLD_ITALIC);
        layout.addView(genres);

        final CheckBox comedyCheckBox = new CheckBox(this);
        comedyCheckBox.setText("Comedy");
        layout.addView(comedyCheckBox);

        final CheckBox actionCheckBox = new CheckBox(this);
        actionCheckBox.setText("Action");
        layout.addView(actionCheckBox);

        final CheckBox dramaCheckBox = new CheckBox(this);
        dramaCheckBox.setText("Drama");
        layout.addView(dramaCheckBox);

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
            // Would go to Feed screen
            // If username is taken need to display an error
        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }
}