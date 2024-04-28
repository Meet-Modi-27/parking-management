package com.example.parking_management.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class nfDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Parking.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String COLUMN_ID = "SID";
    private static final String COLUMN_USER_ID = "USER_ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_NUMBER_PLATE = "VEHICLE_NUMBER";
    private static final String COLUMN_SLOT_ID = "SLOT_ID";
    private static final String COLUMN_LOCATION_ID = "LOCATION_ID";
    private static final String COLUMN_LOCATION = "LOCATION";
    private static final String COLUMN_TIMESTAMP = "TIMESTAMP";




    public nfDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String q1 = "CREATE TABLE NF_USERS (" +
                COLUMN_USER_ID +" TEXT PRIMARY KEY, " +
                COLUMN_NAME+ " TEXT);";
        db.execSQL(q1);

        String q2 = "CREATE TABLE NF_LOCATIONS (" +
                COLUMN_LOCATION+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOCATION_ID+" TEXT);";
        db.execSQL(q2);

        String q3 = "CREATE TABLE NF_SLOTS (" +
                COLUMN_SLOT_ID+" TEXT, " +
                COLUMN_LOCATION+" INTEGER, " +
                "FOREIGN KEY ("+COLUMN_LOCATION+") REFERENCES NF_LOCATIONS("+COLUMN_LOCATION+"));";
        db.execSQL(q3);

        String q4 = "CREATE TABLE NF_SLOTBOOKING (" +
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID +" TEXT, " +
                COLUMN_NUMBER_PLATE+" TEXT, " +
                COLUMN_SLOT_ID+" TEXT, " +
                COLUMN_TIMESTAMP+" TEXT, " +
                "FOREIGN KEY ("+COLUMN_USER_ID+") REFERENCES NF_USERS("+COLUMN_USER_ID+"), "+
                "FOREIGN KEY ("+COLUMN_SLOT_ID+") REFERENCES NF_SLOTS("+COLUMN_SLOT_ID+"));";
        db.execSQL(q4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS NF_USERS");
        db.execSQL("DROP TABLE IF EXISTS NF_LOCATIONS");
        db.execSQL("DROP TABLE IF EXISTS NF_SLOTS");
        db.execSQL("DROP TABLE IF EXISTS NF_SLOTBOOKING");
        onCreate(db);
    }

    public void DeleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS NF_USERS");
        db.execSQL("DROP TABLE IF EXISTS NF_LOCATIONS");
        db.execSQL("DROP TABLE IF EXISTS NF_SLOTS");
        db.execSQL("DROP TABLE IF EXISTS NF_SLOTBOOKING");
        onCreate(db);
    }

    public void insertData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Log.d("Checking ","Running");
            String insertUsersQuery = "INSERT INTO NF_USERS ("+COLUMN_USER_ID+"," + COLUMN_NAME+") SELECT USER_ID, NAME FROM USERS";
            db.execSQL(insertUsersQuery);

            String insertLocationsQuery = "INSERT INTO NF_LOCATIONS ("+COLUMN_LOCATION_ID+") SELECT LOCATION_ID FROM slotBooking";
            db.execSQL(insertLocationsQuery);

            String insertSlotsQuery = "INSERT INTO NF_SLOTS ("+COLUMN_LOCATION+","+COLUMN_SLOT_ID+") SELECT LOCATION_ID,SLOT_ID FROM slotBooking";
            db.execSQL(insertSlotsQuery);

            String insertSlotBookingQuery = "INSERT INTO NF_SLOTBOOKING ("+COLUMN_USER_ID+","+ COLUMN_NUMBER_PLATE+","+ COLUMN_SLOT_ID+","+ COLUMN_TIMESTAMP+") " +
                    "SELECT S.USER_ID, S.VEHICLE_NUMBER, NF.SLOT_ID, S.TIMESTAMP FROM slotBooking S JOIN NF_SLOTS NF WHERE S.SLOT_ID = NF.SLOT_ID";
            db.execSQL(insertSlotBookingQuery);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("InsertData", "Error inserting data", e);
        } finally {
            db.endTransaction();
        }
    }
}