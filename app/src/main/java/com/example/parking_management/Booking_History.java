package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking_management.adapters.bookings_adapters;
import com.example.parking_management.database.SlotDatabase;
import com.example.parking_management.database.nfDatabase;
import com.example.parking_management.database.userDatabase;
import com.example.parking_management.models.bookingsModel;
import com.example.parking_management.models.usersModel;
import com.example.parking_management.models.vehicleModel;
import com.example.parking_management.models.confirmModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Booking_History extends AppCompatActivity {

    String userId;
    SlotDatabase slotDatabase;
    userDatabase userDatabase;
    nfDatabase nf;
    TextView currentText;
    RecyclerView rv_current,rv_previous;
    bookings_adapters adapter,adapter1;



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
        currentText = findViewById(R.id.currentBooking_Text);
        rv_current = findViewById(R.id.currentBooking); // Add this line to initialize rv_current
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

        DatabaseReference counter;
        counter = FirebaseDatabase.getInstance().getReference().child("counter").child("booking counter").child(userId);

        counter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object data = snapshot.getValue();
                if (data.equals(0)){
                    currentText.setText("");
                    currentText.setTextSize(0);
                    currentText.setVisibility(View.INVISIBLE);
                    rv_current.setVisibility(View.INVISIBLE);
                } else {
                    currentText.setText("Current Booked Slot");
                    currentText.setTextSize(40);
                    currentText.setVisibility(View.VISIBLE);
                    rv_current.setVisibility(View.VISIBLE);

                    DatabaseReference db1 = FirebaseDatabase.getInstance().getReference();
                    Query query1 = db1.child("users").child("bookings").child("Current").child(userId);
                    FirebaseRecyclerOptions<bookingsModel> options1 =
                            new FirebaseRecyclerOptions.Builder<bookingsModel>()
                                    .setQuery(query1, bookingsModel.class)
                                    .build();
                    adapter1 = new bookings_adapters(options1);
                    rv_current.setAdapter(adapter1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createDatabase();

    }
    private void createDatabase() {
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
