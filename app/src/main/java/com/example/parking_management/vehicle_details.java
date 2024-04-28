package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.parking_management.adapters.userProfile_adapter;
import com.example.parking_management.adapters.vehicle_adapter;
import com.example.parking_management.models.usersModel;
import com.example.parking_management.models.vehicleModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class vehicle_details extends AppCompatActivity {

    RecyclerView rv;
    vehicle_adapter adapter;
    DatabaseReference usersRef;
    String userId;
    Button new_vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        userId=getIntent().getStringExtra("userId");
        rv = findViewById(R.id.vehicle_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Log.d("userId",userId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.child("Reg_vehicles").child(userId);
        FirebaseRecyclerOptions<vehicleModel> options =
                new FirebaseRecyclerOptions.Builder<vehicleModel>()
                        .setQuery(query, vehicleModel.class)
                        .build();

        adapter = new vehicle_adapter(options);
        rv.setAdapter(adapter);

        new_vehicle = findViewById(R.id.add_vehicle);
        new_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vehicle_details.this,new_vehicle.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}