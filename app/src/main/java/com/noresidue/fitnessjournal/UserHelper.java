package com.noresidue.fitnessjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

// Database helper class:
public class UserHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "UserDatabase";

    // Database columns:
    public static final String TABLE_NAME = "UserTable";
    public static final String TABLE_ID = "id";
    public static final String TABLE_EMAIL = "email";
    public static final String TABLE_PASSWORD = "password";
    public static final String TABLE_GOAL = "goal";
    public static final String TABLE_GAIN = "gain";

    // Constructor:
    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_ID + " INTEGER PRIMARY KEY, " + TABLE_EMAIL + " VARCHAR, " + TABLE_PASSWORD + " VARCHAR, " + TABLE_GOAL + " VARCHAR, " + TABLE_GAIN + " VARCHAR)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean update(int id, String user, int goal, int gain) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(TABLE_ID, id);
        value.put(TABLE_EMAIL, user);
        value.put(TABLE_GOAL, goal);
        value.put(TABLE_GAIN, gain);
        int ret = db.update(TABLE_NAME, value, "id = ?", new String[]{"" + id});
        db.close();
        if (ret > 0) {
            return true;
        }

        return false;
    }

    public int getID(String user) {
        int id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        // Add query to cursor:
        Cursor cursor = db.query(UserHelper.TABLE_NAME, null, " " + UserHelper.TABLE_EMAIL + "=?", new String[]{user}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                id = Integer.parseInt(cursor.getString(0));
                cursor.close();
                return id;
            }
        }

        return id; // error if reached
    }

    public int getGoal(String user) {
        int goal = 100;
        SQLiteDatabase db = this.getWritableDatabase();

        // Add query to cursor:
        Cursor cursor = db.query(UserHelper.TABLE_NAME, null, " " + UserHelper.TABLE_EMAIL + "=?", new String[]{user}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                if (!TextUtils.isEmpty(cursor.getString(3))) {
                    goal = Integer.parseInt(cursor.getString(3));
                }

                cursor.close();
                //return goal;
            }
        }

        return goal;
    }

    public int getGain(String user) {
        int gain = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        // Add query to cursor:
        Cursor cursor = db.query(UserHelper.TABLE_NAME, null, " " + UserHelper.TABLE_EMAIL + "=?", new String[]{user}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                if (!TextUtils.isEmpty(cursor.getString(4))) {
                    gain = Integer.parseInt(cursor.getString(4));
                }

                cursor.close();
            }
        }

        return gain;
    }
}