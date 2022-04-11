package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity {
    private String currentUser;
    private DatabaseReference mDatabase;
    private EditText searchInputBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        initSavedInstanceState(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchInputBox = findViewById(R.id.user_SearchInputBox);
        searchInputBox.setHint("Search By Username");
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("currentUser")) {
            currentUser = savedInstanceState.getString("currentUser");
        } else {
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

    public void userSearchButtonOnClick(View view) {
        String inputText = searchInputBox.getText().toString();
        if (inputText.matches("")) {
            Snackbar.make(view, "Please Enter Username", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            mDatabase.child("users").child(inputText).get().addOnCompleteListener(t1 -> {
                if (t1.getResult().getValue() == null) {
                    Snackbar.make(view, "User does not exist, please try again", BaseTransientBottomBar.LENGTH_LONG).show();

                } else {
                    if (inputText.equals(currentUser)) {
                        Bundle b = new Bundle();
                        b.putString("currentUser", currentUser);
                        Intent intent = new Intent(this, ProfilePageActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        Bundle b = new Bundle();
                        b.putString("currentUser", currentUser);
                        b.putString("searchedUser", inputText);
                        Intent intent = new Intent(this, StaticProfilePageActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }

            });
        }
    }
}