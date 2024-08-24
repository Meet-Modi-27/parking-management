package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking_management.adapters.booking_Adapter1;
import com.example.parking_management.adapters.bookings_adapters;
import com.example.parking_management.database.SlotDatabase;
import com.example.parking_management.database.nfDatabase;
import com.example.parking_management.database.qrcode;
import com.example.parking_management.database.userDatabase;
import com.example.parking_management.models.bookingsModel;
import com.example.parking_management.models.qrModel;
import com.example.parking_management.models.vehicleModel;
import com.example.parking_management.models.confirmModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Booking_History extends AppCompatActivity {

    String userId;
    SlotDatabase slotDatabase;
    userDatabase userDatabase;
    qrcode qrDatabase;
    nfDatabase nf;
    TextView currentText;
    RecyclerView rv_current, rv_previous;
    bookings_adapters adapter;
    booking_Adapter1 adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        userId = getIntent().getStringExtra("userId");
        slotDatabase = new SlotDatabase(this);
        userDatabase = new userDatabase(this);
        qrDatabase = new qrcode(this);
        nf = new nfDatabase(this);
        currentText = findViewById(R.id.currentBooking_Text);
        rv_current = findViewById(R.id.currentBooking);
        rv_previous = findViewById(R.id.previousBookings);
        rv_previous.setLayoutManager(new LinearLayoutManager(this));
        rv_current.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query = db.child("users").child("bookings").child("Previous").child(userId);

        FirebaseRecyclerOptions<bookingsModel> options =
                new FirebaseRecyclerOptions.Builder<bookingsModel>()
                        .setQuery(query, bookingsModel.class)
                        .build();

        adapter = new bookings_adapters(options);
        rv_previous.setAdapter(adapter);

        createDatabase(this);
    }

    private void createDatabase(Context context) {
        slotDatabase.DeleteData();
        userDatabase.DeleteData();
        nf.DeleteData();
        qrDatabase.DeleteData();

        FirebaseDatabase.getInstance().getReference().child("users").child("bookings").child("Previous")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot confirmSnapshot : userSnapshot.getChildren()) {
                                confirmModel confirm = confirmSnapshot.getValue(confirmModel.class);
                                if (confirm != null) {
                                    String userName = confirm.getUserName();
                                    String slotId = confirm.getSpotId();
                                    String locId = confirm.getLocationId();
                                    String timestamp = confirm.getTimestamp();
                                    String number = confirm.getNumber();
                                    String UserId = confirm.getUserId();

                                    slotDatabase.insertData(UserId, userName, slotId, locId, timestamp, number);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseData", "Failed to read vehicle data.", databaseError.toException());
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userName = snapshot.child("username").getValue(String.class);
                            String name = snapshot.child("name").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String number = snapshot.child("number").getValue(String.class);
                            String UserId = snapshot.child("userId").getValue(String.class);
                            userDatabase.insertUserData(UserId, name, userName, email, number);
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
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot vehicleSnapshot : userSnapshot.getChildren()) {
                                vehicleModel vehicle = vehicleSnapshot.getValue(vehicleModel.class);
                                if (vehicle != null) {
                                    String make = vehicle.getMake();
                                    String model = vehicle.getModel();
                                    String number = vehicle.getNumber();
                                    String type = vehicle.getType();
                                    String UserId = vehicle.getUserId();

                                    userDatabase.insertVehicleData(UserId, make, model, type, number);
                                }
                            }
                        }
                        nf.insertData();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseData", "Failed to read vehicle data.", databaseError.toException());
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("qr").child("empty")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot qrSnapshot : userSnapshot.getChildren()) {
                                qrModel qr = qrSnapshot.getValue(qrModel.class);
                                if (qr != null) {
                                    String location = qr.getLocation();
                                    String spotId = qr.getSpotId();
                                    String spotType = qr.getSpotType();

                                    qrDatabase.insertQrData(location, spotId, spotType);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
        if (adapter1 != null) {
            adapter1.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
        if (adapter1 != null) {
            adapter1.stopListening();
        }
    }
}