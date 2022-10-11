package com.noresidue.fitnessjournal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    String userEmail = "";
    int weightID = -1;
    WeightHelper helper;
    Button editButton;
    EditText weight;
    EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Get passed through user information:
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER");
        weightID = intent.getIntExtra("WEIGHT_ID", weightID); // am I doing this right?

        // Create the weight database helper:
        helper = new WeightHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Weight");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editButton = (Button) findViewById(R.id.editButton);
        weight = (EditText) findViewById(R.id.editWeight);
        date = (EditText) findViewById(R.id.date);

        Cursor cursor = helper.read(weightID);
        cursor.moveToNext();
        weight.setText("" + cursor.getString(2));
        date.setText("" + cursor.getString(3));

        // Set login on click listener:
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWeight();
            }
        });
    }

    public void editWeight() {
        boolean result = helper.update(weightID, userEmail, Integer.parseInt(weight.getText().toString()), date.getText().toString());
        if(result) {
            Toast.makeText(EditActivity.this, "Weight edited!", Toast.LENGTH_SHORT).show();
            mainActivity();
        } else {
            Toast.makeText(EditActivity.this, "Failed to edit weight!", Toast.LENGTH_SHORT).show();
        }
    }

    // Goto the main activity:
    public void mainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", userEmail); // pass email to main
        startActivity(intent);
    }

}