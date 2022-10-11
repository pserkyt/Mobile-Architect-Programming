package com.noresidue.fitnessjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class ProfileActivity extends AppCompatActivity {
    String IdHolder;
    String EmailHolder;
    String GoalHolder;
    TextView email;
    TextView goal;
    Button logOut;
    Button update;
    UserHelper helper;
    CheckBox gain;
    int gainState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the user database helper:
        helper = new UserHelper(this);

        email = (TextView) findViewById(R.id.emailTextView);
        goal = (TextView) findViewById(R.id.goalTextView);
        logOut = (Button) findViewById(R.id.buttonLogOut);
        update = (Button) findViewById(R.id.buttonUpdate);
        gain = (CheckBox) findViewById(R.id.gain);

        // Get passed through user information:
        Intent intent = getIntent();
        IdHolder = intent.getStringExtra("ID");
        EmailHolder = intent.getStringExtra("USER");
        GoalHolder = intent.getStringExtra("GOAL");
        if (!TextUtils.isEmpty(GoalHolder)) {
            goal.setText("" + GoalHolder);
        } else {
            goal.setText("100");
        }
        if (!TextUtils.isEmpty(EmailHolder)) {
            email.setText("" + EmailHolder);
        }

        // Get gain value from user database:
        if(helper.getGain(EmailHolder) == 1) {
            gain.setChecked(true);
        } else {
            gain.setChecked(false);
        }

        // Adding click listener to update button.
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if gain is checked:
                if(gain.isChecked()) {
                    gainState = 1;
                } else {
                    gainState = 0;
                }

                // Update user database info:
                if (helper.update(Integer.parseInt(IdHolder), email.getText().toString(), Integer.parseInt(goal.getText().toString()), gainState)) {
                    // Save user info:
                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor Ed = sp.edit();
                    Ed.putString("email", email.getText().toString());
                    Ed.commit();

                    Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    mainActivity();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding click listener to Log Out button.
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete local info:
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("email", null);
                Ed.putString("password", null);
                Ed.commit();

                // Go back to the login screen:
                finish();
                Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Log out successfull!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ProfileFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_screen, rootKey);
        }
    }

    // Goto the main activity:
    public void mainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", EmailHolder); // pass email to main
        startActivity(intent);
    }
}
