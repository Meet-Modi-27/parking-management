package com.example.parking_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

public class SlotDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Parking.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String TABLE_NAME = "slotBooking";
    private static final String COLUMN_ID = "SID";
    private static final String COLUMN_USER_ID = "USER_ID";
    private static final String COLUMN_NAME = "USER_NAME";
    private static final String COLUMN_NUMBER_PLATE = "VEHICLE_NUMBER";
    private static final String COLUMN_SLOT_ID = "SLOT_ID";
    private static final String COLUMN_LOCATION_ID = "LOCATION_ID";
    private static final String COLUMN_TIMESTAMP = "TIMESTAMP";




    public SlotDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_NUMBER_PLATE + " TEXT, "
                + COLUMN_SLOT_ID + " TEXT, "
                + COLUMN_LOCATION_ID + " TEXT, "
                + COLUMN_TIMESTAMP + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String userId, String name, String slotId, String locId, String timestamp,String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert new data
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SLOT_ID, slotId);
        values.put(COLUMN_NUMBER_PLATE, number);
        values.put(COLUMN_LOCATION_ID, locId);
        values.put(COLUMN_TIMESTAMP, timestamp);

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


    public void DeleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
