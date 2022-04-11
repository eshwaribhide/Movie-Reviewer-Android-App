package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// pull from database
// update button and then use setvalue to write
// need another profilepage activity when it's not the current user -> static activity page
// on that page will have a follow button
// user object will have following -> username, and followers -> username,
// maybe on profile page can show followers

// Add username to display
// Save state with rotating
public class ProfilePageActivity extends AppCompatActivity {
    private String currentUser;
    private DatabaseReference mDatabase;
    private EditText fullNameValue;
    private TextView totalReviews;
    private CheckBox comedyCheckBox;
    private CheckBox actionCheckBox;
    private CheckBox dramaCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initSavedInstanceState(savedInstanceState);

        fullNameValue = findViewById(R.id.fullNameValue);
        totalReviews = findViewById(R.id.totalReviews);
        comedyCheckBox = findViewById(R.id.comedyCheckBox);
        actionCheckBox = findViewById(R.id.actionCheckBox);
        dramaCheckBox = findViewById(R.id.dramaCheckBox);

        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                fullNameValue.setText(String.valueOf(task.getResult().child("fullName").getValue()));
                totalReviews.setText("Total Reviews: " + task.getResult().child("reviewCount").getValue());
                comedyCheckBox.setChecked((Boolean) task.getResult().child("genres").child("comedyGenreSelected").getValue());
                actionCheckBox.setChecked((Boolean) task.getResult().child("genres").child("actionGenreSelected").getValue());
                dramaCheckBox.setChecked((Boolean) task.getResult().child("genres").child("dramaGenreSelected").getValue());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Bundle b = new Bundle();
                b.putString("currentUser", currentUser);
                Log.e("HISTORYCURRENTUSER", currentUser);
                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void profileSaveButtonOnClick(View view) {
        mDatabase.child("users").child(currentUser).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                // Need to also have ability to update username
                mDatabase.child("users").child(currentUser).child("fullName").setValue(fullNameValue.getText().toString());
                mDatabase.child("users").child(currentUser).child("genres").child("comedyGenreSelected").setValue(comedyCheckBox.isChecked());
                mDatabase.child("users").child(currentUser).child("genres").child("actionGenreSelected").setValue(actionCheckBox.isChecked());
                mDatabase.child("users").child(currentUser).child("genres").child("dramaGenreSelected").setValue(dramaCheckBox.isChecked());


            }
    });}
}