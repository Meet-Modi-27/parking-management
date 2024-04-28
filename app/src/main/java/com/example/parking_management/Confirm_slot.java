package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.parking_management.models.confirmModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Confirm_slot extends AppCompatActivity {

    private String userId, spotId, loc;
    private DatabaseReference userRef, dbRef,vehicleRef;
    private boolean onDataChangeCalled = false; // Flag to track if onDataChange was called
    AutoCompleteTextView vehicleAutoCompleteTextView;
    List<String> vehicleNumbers;
    Button confirm;
    String dropDownItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_slot);

        userId = getIntent().getStringExtra("userId");
        spotId = getIntent().getStringExtra("spotId");
        confirm = findViewById(R.id.confirm_btn);
        vehicleAutoCompleteTextView = findViewById(R.id.vehicle_number);

        if (spotId != null && spotId.contains("_")) {
            int index = spotId.indexOf('_');
            loc = spotId.substring(0, index);
        } else {
            loc = spotId;  // Handle appropriately if no underscore is present
        }

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users").child(userId);
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child("bookings").child(userId);
        vehicleRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_vehicles").child(userId);
        List<String> vehicleNumbers = new ArrayList<>(); // Initialize list to store vehicle numbers

        vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Ensure vehicleNumbers list is not null
                if (vehicleNumbers != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String vehicleNumber = snapshot.child("number").getValue(String.class);
                        Log.d("FirebaseData", "Number: " + vehicleNumber);
                        // Add vehicle number to the list
                        vehicleNumbers.add(vehicleNumber);
                    }
                    // Populate AutoCompleteTextView with vehicle numbers
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Confirm_slot.this, android.R.layout.simple_dropdown_item_1line, vehicleNumbers);
                    vehicleAutoCompleteTextView.setAdapter(adapter);
                } else {
                    Log.e("FirebaseData", "vehicleNumbers list is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleNumber = vehicleAutoCompleteTextView.getText().toString(); // Get selected vehicle number

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!onDataChangeCalled) { // Check if onDataChange was already called
                            onDataChangeCalled = true; // Set flag to true to prevent multiple executions
                            String userName = dataSnapshot.child("name").getValue(String.class);
                            long timestamp = System.currentTimeMillis();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String timeStamp = sdf.format(new Date(timestamp));
                            // Create confirmModel object with vehicle number
                            confirmModel confirm = new confirmModel(userId, userName, spotId, timeStamp, loc, vehicleNumber);
                            dbRef.push().setValue(confirm);
                            Toast.makeText(Confirm_slot.this, "Booking Confirmed!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("FirebaseData", "loadPost:onCancelled", databaseError.toException());
                        Toast.makeText(Confirm_slot.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
