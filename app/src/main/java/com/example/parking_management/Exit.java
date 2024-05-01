package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

public class Exit extends AppCompatActivity {

    private String spotId, loc,userId;
    private DatabaseReference counterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotId = getIntent().getStringExtra("spotId");
        userId = getIntent().getStringExtra("userId");

        if (spotId != null && spotId.contains("_")) {
            int index = spotId.indexOf('_');
            loc = spotId.substring(0, index);
        } else {
            loc = spotId;  // Handle appropriately if no underscore is present
        }

        counterRef = FirebaseDatabase.getInstance().getReference().child("counter").child("Temp_Counter").child(loc);
        updateQR();
        finish();

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
                    mutableData.setValue(currentValue + 1);
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
        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(Loc).child(spotId);
        DatabaseReference sourceRef = FirebaseDatabase.getInstance().getReference().child("qr").child("parked").child(Loc).child(spotId);

        sourceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if data exists and retrieve the user ID
                if (dataSnapshot.exists()) {
                    String parkedUserId = dataSnapshot.child("userId").getValue(String.class);
                    if (parkedUserId != null && parkedUserId.equals(userId)) {
                        // Create a new data object from the snapshot, removing or nullifying the user ID
                        Map<String, Object> dataMap = (Map<String, Object>) dataSnapshot.getValue();
                        dataMap.put("userId", ""); // Set userId to an empty string or you could also use null

                        // Delete the data from the source location
                        sourceRef.removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    // Set the modified data to the new location
                                    destinationRef.setValue(dataMap)
                                            .addOnSuccessListener(aVoid1 -> {
                                                Log.d("FirebaseData", "Data moved and user ID cleared successfully");
                                                updateCounter();
                                            })
                                            .addOnFailureListener(e -> Log.e("FirebaseData", "Failed to move data: " + e.getMessage()));
                                })
                                .addOnFailureListener(e -> Log.e("FirebaseData", "Failed to delete data: " + e.getMessage()));
                    } else {
                        Toast.makeText(Exit.this, "Log In with the same Id to scan exit code.", Toast.LENGTH_SHORT).show();
                        Log.d("FirebaseData", "User ID does not match. No data moved.");
                    }
                } else {
                    Log.d("FirebaseData", "No data found at source reference.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });
    }

}