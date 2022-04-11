package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

// can expand genres later, need to add more checkboxes

// Things that need to be done
// 1. Expand genres
// 2. Add followers so this can be in feed, which requires being able to see other users' profiles
// 3. Leaderboard, remove profile picture and replace with badge, add review count and perhaps position
// 4. Display reviews design poster gets cut off
// 5. Theaters near me
// 6. layout for profile page, add username change, and static profile page
public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void loginButtonOnClick(View view) {
//        /////////////////This dialog is for login/////////////////
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Login");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editUsername = new EditText(this);
        editUsername.setHint("Enter Username");
        layout.addView(editUsername);

        editUsername.setTextColor(Color.parseColor("#9C27B0"));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("OK", (dialog, whichButton) -> {

            String username = editUsername.getText().toString();
            mDatabase.child("users").child(username).get().addOnCompleteListener(t1 -> {
                        if (t1.getResult().getValue() == null) {
                            Toast toast = Toast.makeText(MainActivity.this, "User does not exist, please sign up", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else {
                            Bundle b = new Bundle();
                            b.putString("currentUser", username);
                            Intent intent = new Intent(this, NavigationActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                });



        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();



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
            String username = editUsername.getText().toString();
            String fullName = editFullName.getText().toString();

                    mDatabase.child("users").child(username).get().addOnCompleteListener(t1 -> {
                        if (t1.getResult().getValue() == null) {
                            mDatabase.child("users").child(username).setValue(new User(fullName, new Genre(comedyCheckBox.isChecked(),actionCheckBox.isChecked(),dramaCheckBox.isChecked())));

                            Bundle b = new Bundle();
                            b.putString("currentUser", username);
                            Intent intent = new Intent(this, NavigationActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);

                        }
                        else {
                            Snackbar.make(view, "User already exists", BaseTransientBottomBar.LENGTH_LONG).show();

                        }
                    });
                });


        alertDialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }
}