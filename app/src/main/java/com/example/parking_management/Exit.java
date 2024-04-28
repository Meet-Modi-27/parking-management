package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Exit extends AppCompatActivity {

    private String spotId, loc;
    private DatabaseReference counterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotId = getIntent().getStringExtra("spotId");

        if (spotId != null && spotId.contains("_")) {
            int index = spotId.indexOf('_');
            loc = spotId.substring(0, index);
        } else {
            loc = spotId;  // Handle appropriately if no underscore is present
        }

        counterRef = FirebaseDatabase.getInstance().getReference().child("counter").child("Temp_Counter").child(loc);

        updateCounter();
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
        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(spotId);
        DatabaseReference sourceRef = FirebaseDatabase.getInstance().getReference().child("qr").child("parked").child(spotId);

        sourceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Save the data locally
                Object data = dataSnapshot.getValue();

                // Delete the data from the source location
                sourceRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write the data to the new location
                                destinationRef.setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data moved successfully
                                                Log.d("FirebaseData", "Data moved successfully");
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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FirebaseData", "Failed to read value.", error.toException());
            }
        });
    }
}