package com.noresidue.fitnessjournal;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    // View variables:
    Button submit;
    EditText email;
    EditText password;
    TextView gotoRegister;

    // Database variables:
    SQLiteDatabase db;
    UserHelper helper;
    Cursor cursor;
    String emailString, passwordString;
    String temp = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Grab our view items:
        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        gotoRegister = (TextView) findViewById(R.id.gotoRegister);
        submit = (Button) findViewById(R.id.submit_button);

        // Create the user database helper:
        helper = new UserHelper(this);

        // Set register on click listener:
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity();
            }
        });

        // Set login button on click listener:
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fields empty?
                if (IsEmpty()) {
                    Login();
                } else {
                    Toast.makeText(LogInActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("Range")
    public void Login() {
        db = helper.getWritableDatabase(); // open the database with write permission

        // Add query to cursor:
        cursor = db.query(UserHelper.TABLE_NAME, null, " " + UserHelper.TABLE_EMAIL + "=?", new String[]{emailString}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                temp = cursor.getString(cursor.getColumnIndex(UserHelper.TABLE_PASSWORD));
                cursor.close();
            }
        }

        Validate();
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

    // Verify if password correct:
    public void Validate() {
        if (temp.equalsIgnoreCase(passwordString)) {
            Toast.makeText(LogInActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
            // Save user info:
            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("email", emailString);
            Ed.putString("password", passwordString);
            Ed.commit();

            mainActivity(); // goto the main activity
        } else {
            Toast.makeText(LogInActivity.this, "Email or password is wrong, please try again.", Toast.LENGTH_SHORT).show();
        }
        temp = "";
    }

    // Goto the main activity:
    public void mainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", emailString); // pass email to main
        startActivity(intent);
    }

    // Goto the register activity:
    public void registerActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        emailString = email.getText().toString();
        if (!TextUtils.isEmpty(emailString)) {
            intent.putExtra("EMAIL", emailString);
        }
        startActivity(intent);
    }

    // Goto the profile activity:
    public void profileActivity() {
        Intent intent = new Intent(LogInActivity.this, ProfileActivity.class);
        intent.putExtra("USER", emailString); // pass email to profile
        startActivity(intent);
    }
}