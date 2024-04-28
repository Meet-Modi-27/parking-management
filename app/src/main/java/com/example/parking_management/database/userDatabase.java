package com.example.parking_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class userDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Parking.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String TABLE_NAME = "USERS";
    private static final String TABLE1_NAME = "VEHICLES";

    private static final String COLUMN_ID = "SID";
    private static final String COLUMN_USER_ID = "USER_ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_NUMBER = "NUMBER";
    private static final String COLUMN_USER_NAME = "USER_NAME";
    private static final String COLUMN_EMAIL_ID = "EMAIL_ID";

    private static final String COLUMN_MAKE = "MAKE";
    private static final String COLUMN_MODEL = "MODEL";
    private static final String COLUMN_NUMBER_PLATE = "NUMBER_PLATE";
    private static final String COLUMN_TYPE = "TYPE";

    public userDatabase (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_NUMBER + " TEXT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_EMAIL_ID + " TEXT);";
        db.execSQL(query);

        String q2 = "CREATE TABLE " + TABLE1_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_MAKE + " TEXT, "
                + COLUMN_MODEL + " TEXT, "
                + COLUMN_NUMBER_PLATE + " TEXT, "
                + COLUMN_TYPE + " TEXT);";
        db.execSQL(q2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE1_NAME);
        onCreate(db);
    }

    public void insertUserData(String userId, String name, String userName, String email,String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert new data
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_EMAIL_ID, email);

        // Inserting row
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            // Insertion failed
            Log.e("SlotDatabase", "Failed to insert data into database");
        } else {
            // Data inserted successfully
            Log.d("SlotDatabase", "Data inserted successfully into database");
        }

        db.close(); // Closing database connection
    }
    public void insertVehicleData(String userId, String make, String model, String type,String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert new data
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MAKE, make);
        values.put(COLUMN_MODEL, model);
        values.put(COLUMN_NUMBER_PLATE, number);
        values.put(COLUMN_TYPE, type);

        // Inserting row
        long result = db.insert(TABLE1_NAME, null, values);
        if (result == -1) {
            // Insertion failed
            Log.e("SlotDatabase", "Failed to insert data into database");
        } else {
            // Data inserted successfully
            Log.d("SlotDatabase", "Data inserted successfully into database");
        }

        db.close();
    }

    public void DeleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        onCreate(db);
    }
}
