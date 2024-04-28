package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parking_management.adapters.dashboard_adapter;
import com.example.parking_management.adapters.userProfile_adapter;
import com.example.parking_management.database.nfDatabase;
import com.example.parking_management.database.userDatabase;
import com.example.parking_management.models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class User_Profile extends AppCompatActivity {

    RecyclerView rv;
    userProfile_adapter adapter;
    DatabaseReference usersRef;
    String userId;
    Button update;
    Button vehicle;
    Button history;
    TextView admin;

    nfDatabase nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userId=getIntent().getStringExtra("userId");
        rv = findViewById(R.id.userProfile_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        nf = new nfDatabase(this);

        nf.DeleteData();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(userId);
        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        adapter = new userProfile_adapter(options);
        rv.setAdapter(adapter);

        update = findViewById(R.id.details);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Profile.this,update_Profile.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        vehicle = findViewById(R.id.vehicle_details);
        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Profile.this,vehicle_details.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Profile.this,Booking_History.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
        admin = findViewById(R.id.admin_access);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Profile.this, admin_Check.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        nf.insertData();
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