package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.parking_management.database.SlotDatabase;
import com.example.parking_management.database.nfDatabase;
import com.example.parking_management.database.userDatabase;
import com.example.parking_management.models.vehicleModel;
import com.example.parking_management.models.confirmModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Booking_History extends AppCompatActivity {

    String userId;
    SlotDatabase slotDatabase;
    userDatabase userDatabase;
    nfDatabase nf;



    vehicleModel vehicleModel;
    confirmModel confirmModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        userId = getIntent().getStringExtra("userId");
        slotDatabase = new SlotDatabase(this);
        userDatabase = new userDatabase(this);
        nf = new nfDatabase(this);


        slotDatabase.DeleteData();
        userDatabase.DeleteData();
        nf.DeleteData();

        FirebaseDatabase.getInstance().getReference().child("users").child("bookings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Iterate through each user's vehicles
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Iterate through each vehicle entry
                            for (DataSnapshot confirmSnapshot : userSnapshot.getChildren()) {
                                // Convert snapshot to Vehicle object
                                confirmModel confirm = confirmSnapshot.getValue(confirmModel.class);
                                // Now you can use the vehicle object
                                if (confirm != null) {
                                    String userName = confirm.getUserName();
                                    String slotId = confirm.getSpotId();
                                    String locId = confirm.getLocationId();
                                    String timestamp = confirm.getTimestamp();
                                    String number = confirm.getNumber();
                                    String UserId = confirm.getUserId();

                                    slotDatabase.insertData(UserId, userName, slotId,locId, timestamp,number);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                        Log.e("FirebaseData", "Failed to read vehicle data.", databaseError.toException());
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Iterate through each booking history entry
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Extract relevant data and insert into SQLite database
                            String userName = snapshot.child("username").getValue(String.class);
                            String name = snapshot.child("name").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String number = snapshot.child("number").getValue(String.class);
                            String UserId = snapshot.child("userId").getValue(String.class);
                            // Insert data into SQLite database
                            Toast.makeText(Booking_History.this, "Creating", Toast.LENGTH_SHORT).show();
                            userDatabase.insertUserData(UserId, name, userName,email,number);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Booking_History.this, "Not Created", Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users").child("Reg_vehicles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Iterate through each user's vehicles
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Iterate through each vehicle entry
                            for (DataSnapshot vehicleSnapshot : userSnapshot.getChildren()) {
                                // Convert snapshot to Vehicle object
                                vehicleModel vehicle = vehicleSnapshot.getValue(vehicleModel.class);
                                // Now you can use the vehicle object
                                if (vehicle != null) {
                                    String make =  vehicle.getMake();
                                    String model =  vehicle.getModel();
                                    String number =  vehicle.getNumber();
                                    String type =  vehicle.getType();
                                    String UserId = vehicle.getUserId();

                                    userDatabase.insertVehicleData(UserId, make, model,type,number);
                                }
                            }
                        }
                        nf.insertData();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                        Log.e("FirebaseData", "Failed to read vehicle data.", databaseError.toException());
                    }
                });
    }
}
