package com.noresidue.fitnessjournal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    String userEmail = "";
    WeightHelper helper;
    Button addButton;
    EditText weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Get passed through user information:
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER");

        // Create the weight database helper:
        helper = new WeightHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Weight");
        actionBar.setDisplayHomeAsUpEnabled(true);

        addButton = (Button) findViewById(R.id.addButton);
        weight = (EditText) findViewById(R.id.addWeight);

        // Set login on click listener:
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeight();
            }
        });
    }

    public void addWeight() {
        boolean result = helper.create(userEmail, Integer.parseInt(weight.getText().toString()));
        if(result) {
            Toast.makeText(AddActivity.this, "Weight added!", Toast.LENGTH_SHORT).show();
            mainActivity();
        } else {
            Toast.makeText(AddActivity.this, "Failed to add weight!", Toast.LENGTH_SHORT).show();
        }
    }

    // Goto the main activity:
    public void mainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", userEmail); // pass email to main
        startActivity(intent);
    }
}