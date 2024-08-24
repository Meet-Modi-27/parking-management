package com.example.parking_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class qrcode extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Parking.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String LOCATION = "LOCATION";
    private static final String SPOT_ID = "SPOT_ID";
    private static final String SPOT_TYPE = "SPOT_TYPE";

    private static final String TABLE_NAME = "QR_CODE";

    public qrcode(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q1 = "CREATE TABLE "+TABLE_NAME+"("+LOCATION+" TEXT, "+SPOT_ID+" TEXT, "+SPOT_TYPE+" TEXT);";
        db.execSQL(q1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS QR_CODE");
        onCreate(db);
    }

    public void DeleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS QR_CODE");
        onCreate(db);
    }

    public void insertQrData(String location, String spotId, String spotType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATION, location);
        values.put(SPOT_ID, spotId);
        values.put(SPOT_TYPE, spotType);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            // Insertion failed
            Log.e("SlotDatabase", "Failed to insert data into database");
        } else {
            // Data inserted successfully
            Log.d("SlotDatabase", "Data inserted successfully into database");
        }

        db.close();

    }
}
