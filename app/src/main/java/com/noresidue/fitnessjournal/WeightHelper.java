package com.noresidue.fitnessjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Database helper class:
public class WeightHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "WeightDatabase";

    // Database columns:
    public static final String TABLE_NAME = "WeightTable";
    public static final String TABLE_ID = "id";
    public static final String TABLE_USER = "user";
    public static final String TABLE_WEIGHT = "weight";
    public static final String TABLE_DATE = "date";

    // Constructor:
    public WeightHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_ID + " INTEGER PRIMARY KEY, " + TABLE_USER + " VARCHAR, " + TABLE_WEIGHT + " INTEGER, " + TABLE_DATE + " VARCHAR)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean create(String user, int weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(TABLE_USER, user);
        value.put(TABLE_WEIGHT, weight);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        value.put(TABLE_DATE, dtf.format(now));
        long result = db.insert(TABLE_NAME, null, value);
        db.close();
        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor read(int _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where id = " + _id, null);
        return cursor;
    }

    public boolean update(int id, String user, int weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(TABLE_ID, id);
        value.put(TABLE_USER, user);
        value.put(TABLE_WEIGHT, weight);
        value.put(TABLE_DATE, date);
        int ret = db.update(TABLE_NAME, value, "id = ?", new String[]{"" + id});
        db.close();
        if (ret > 0) {
            return true;
        }

        return false;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int ret = db.delete(TABLE_NAME, "id = ?", new String[]{"" + id});
        db.close();
        if (ret > 0) {
            return true;
        }

        return false;
    }

    public int getNewestID() {
        String id = "1";
        Cursor cursor = getAllData();
        if (cursor.moveToLast()) {
            id = cursor.getString(0); // get the id;
        }

        return Integer.parseInt(id);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }
}