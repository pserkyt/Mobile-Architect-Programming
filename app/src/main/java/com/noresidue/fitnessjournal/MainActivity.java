package com.noresidue.fitnessjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    String userEmail;
    String userPassword;
    int userID = -1;
    int weightID = -1;
    int goal = 100;
    WeightHelper helper;
    UserHelper userHelper;
    private FloatingActionButton addButton;
    private ImageButton editButton;
    private ImageButton listButton;
    private TextView currentWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab locally saved login info:
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        userEmail = sp1.getString("email", null);
        userPassword = sp1.getString("password", null);

        currentWeight = (TextView) findViewById(R.id.currentWeight);

        // Create the weight database helper:
        helper = new WeightHelper(this);

        // Get the goal weight:
        userHelper = new UserHelper(this);
        userID = userHelper.getID(userEmail);
        goal = userHelper.getGoal(userEmail);

        // Get the newest weight ID:
        weightID = helper.getNewestID();

        Cursor cursor = helper.read(weightID);
        //weightID = cursor.getString(0)+"\n"
        boolean hasWeight = false;
        if (cursor.moveToNext()) {
            currentWeight.setText("" + cursor.getString(2));
            hasWeight = true;
        } else {
            currentWeight.setText("None");
        }

        // If we have a current weight and have reached our goal weight send a SMS:
        if(hasWeight) {
            boolean reached = false;
            if(userHelper.getGain(userEmail) == 1 && Integer.parseInt(currentWeight.getText().toString()) >= goal) {
                reached = true;
            } else if (userHelper.getGain(userEmail) == 0 && Integer.parseInt(currentWeight.getText().toString()) <= goal) {
                reached = true;
            }
            if(reached) {
                Toast.makeText(this, "Goal reached! Check your text messages if you enabled SMS messaging in settings :)", Toast.LENGTH_LONG).show();
                sendSMSGoalReached();
            }
        }

        // Nav bar:
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Weight");

        // Add button:
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClicked();
            }
        });

        // Edit button:
        editButton = (ImageButton) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClicked();
            }
        });

        // List button:
        listButton = (ImageButton) findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClicked();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("ID", "" + userID); // pass ID
                intent.putExtra("USER", userEmail); // pass email
                intent.putExtra("GOAL", "" + goal); // pass goal
                startActivity(intent);
                break;

            case R.id.logout:
                // Delete local info:
                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("email", null);
                Ed.putString("password", null);
                Ed.commit();

                // Go back to the login screen:
                Toast.makeText(MainActivity.this, "Log out successfull!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addClicked() {
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("USER", userEmail); // pass email to add
        startActivity(intent);
    }

    public void editClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("USER", userEmail); // pass email to edit
        intent.putExtra("WEIGHT_ID", weightID); // pass weight id to edit
        startActivity(intent);
    }

    public void listClicked() {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("USER", userEmail); // pass email to list
        startActivity(intent);
    }

    public void sendSMSGoalReached() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("5555555555", null,"Congrats, you have reached your goal weight of " + goal + "!!! Set your new weight goal in your profile and keep up the good work!", null, null);
    }
}