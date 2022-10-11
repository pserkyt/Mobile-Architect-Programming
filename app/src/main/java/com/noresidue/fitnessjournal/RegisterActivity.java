package com.noresidue.fitnessjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Button submit;
    EditText email;
    EditText password;
    TextView gotoLogin;

    String emailString, passwordString;
    SQLiteDatabase db;
    String query;
    UserHelper helper;
    Cursor cursor;
    Boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Grab our view items:
        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        gotoLogin = (TextView) findViewById(R.id.gotoLogin);
        submit = (Button) findViewById(R.id.submit_button);

        Intent intent = getIntent();

        // Get entered email, if any:
        emailString = intent.getStringExtra("EMAIL");
        if (!TextUtils.isEmpty(emailString)) {
            email.setText(email.getText().toString() + emailString);
        }

        // Create the user database helper:
        helper = new UserHelper(this);

        // Set login on click listener:
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity();
            }
        });

        // Set register button on click listener:
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create database if it does not exist already:
                GetDatabase();
                BuildTable();

                // Fields empty?
                if (IsEmpty()) {
                    VerifyEmail();
                    ClearFields();
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // SQLite database build:
    public void GetDatabase() {
        db = openOrCreateDatabase(UserHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    // SQLite table build:
    public void BuildTable() {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserHelper.TABLE_NAME + "(" + UserHelper.TABLE_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + UserHelper.TABLE_EMAIL + " VARCHAR, " + UserHelper.TABLE_PASSWORD + " VARCHAR, " + UserHelper.TABLE_GOAL + " VARCHAR, " + UserHelper.TABLE_GAIN + " VARCHAR);");
    }

    // Insert registration data into the database:
    public void Register() {
        // Query:
        query = "INSERT INTO " + UserHelper.TABLE_NAME + " (email,password) VALUES('" + emailString + "', '" + passwordString + "');";

        db.execSQL(query); // execute the query
        db.close(); // done, close the database

        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
        mainActivity();
    }

    // Empty fields:
    public void ClearFields() {
        email.getText().clear();
        password.getText().clear();
    }

    // Check if the email or password fields are empty:
    public Boolean IsEmpty() {
        // Store the email and password values into strings:
        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        // Check if the email or password fields are empty:
        if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
            return false;
        }

        return true;
    }

    // Verify if the email already exists:
    public void VerifyEmail() {
        db = helper.getWritableDatabase(); // open the database with write permission

        // Add query to cursor:
        cursor = db.query(UserHelper.TABLE_NAME, null, " " + UserHelper.TABLE_EMAIL + "=?", new String[]{emailString}, null, null, null);

        // Check to see if the account already exists:
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                exists = true;
                cursor.close();
            }
        }

        Validate(); // verify data has been inserted into the database
    }

    // Verify if data has been inserted or exists:
    public void Validate() {
        // Does the user account already exist?
        if (exists) {
            Toast.makeText(RegisterActivity.this, "User account already exists! Try logging in instead.", Toast.LENGTH_SHORT).show();
        } else {
            // If it does not exist, register it:
            Register();
        }
        exists = false; // reset
    }

    // Goto the main activity:
    public void mainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", emailString); // pass email to main
        startActivity(intent);
    }

    // Goto the login activity:
    public void loginActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}