package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.parking_management.models.confirmModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Confirm_slot extends AppCompatActivity {

    private String userId, spotId, loc;
    private DatabaseReference userRef, dbRef,vehicleRef,spotRef,counterRef;
    private boolean onDataChangeCalled = false;
    AutoCompleteTextView vehicleAutoCompleteTextView;
    Button confirm;


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
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child("bookings").child("Current").child(userId);
        vehicleRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_vehicles").child(userId);
        spotRef = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(loc).child(spotId);
        counterRef = FirebaseDatabase.getInstance().getReference().child("counter").child("Temp_Counter").child(loc);

        spotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String spotType = dataSnapshot.child("spotType").getValue(String.class);
                    addVehicleData(spotType);
                } else {
                    // Handle the case if the spotId does not exist
                    Toast.makeText(Confirm_slot.this, "Spot is not Empty", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            private void addVehicleData(String spotType) {
                List<String> vehicleNumbers = new ArrayList<>();

                vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String vehicleNumber = snapshot.child("number").getValue(String.class);
                            String vehicleType = snapshot.child("type").getValue(String.class);

                            // Check if the vehicle type matches the spot type
                            if (vehicleType != null && vehicleType.equals(spotType)) {
                                Log.d("FirebaseData", "Number: " + vehicleNumber);
                                // Add vehicle number to the list
                                vehicleNumbers.add(vehicleNumber);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Confirm_slot.this, android.R.layout.simple_dropdown_item_1line, vehicleNumbers);
                        vehicleAutoCompleteTextView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("FirebaseData", "Failed to read value.", error.toException());
                    }
                });
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
                String vehicleNumber = vehicleAutoCompleteTextView.getText().toString();

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
                            updateQR();
                            updateCounter();
                            finish();
                        }
                    }

                    private void updateCounter() {
                        counterRef.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Long currentValue = mutableData.getValue(Long.class);
                                if (currentValue == null) {
                                    mutableData.setValue(1);
                                } else {
                                    mutableData.setValue(currentValue - 1);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                            }
                        });
                    }

                    private void updateQR() {
                        String[] parts = spotId.split("_");
                        String Loc = parts[0];

                        DatabaseReference sourceRef = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(Loc).child(spotId);
                        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference().child("qr").child("parked").child(Loc).child(spotId);

                        // Read data from the source location
                        sourceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Save the data locally
                                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();

                                if (data != null) {
                                    // Update userId in the data map
                                    data.put("userId", userId);

                                    // Delete the data from the source location
                                    sourceRef.removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write the updated data to the new location
                                                    destinationRef.setValue(data)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Data moved successfully with userId updated
                                                                    Log.d("FirebaseData", "Data moved and userId updated successfully");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Failed to move data to the new location
                                                                    Log.e("FirebaseData", "Failed to move data: " + e.getMessage());
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to delete data from the source location
                                                    Log.e("FirebaseData", "Failed to delete data: " + e.getMessage());
                                                }
                                            });
                                } else {
                                    Log.e("FirebaseData", "Data snapshot is null or not a map");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("FirebaseData", "Failed to read value.", error.toException());
                            }
                        });
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
