package com.example.parking_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parking_management.adapters.dashboard_adapter;
import com.example.parking_management.models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    dashboard_adapter adapter;
    DatabaseReference usersRef;
    CardView scan_qr,exit;
    CardView find;
    String results;
    String userId;
    ImageView profile;
    String currentPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId=getIntent().getStringExtra("userId");
        rv = findViewById(R.id.dashboard_name_rv);
        rv.setLayoutManager(new LinearLayoutManager(this)); // Don't forget to set a LayoutManager


        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

         Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(userId);
        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        adapter = new dashboard_adapter(options);
        rv.setAdapter(adapter);


        scan_qr = findViewById(R.id.dashboard_scan_qr);
        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPrompt = "Scan for Entry";
                Log.d("Prompt",currentPrompt);
                startQRScan("Scan for Entry");
            }
        });

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPrompt = "Scan for Exit";
                startQRScan("Scan for Exit");
            }
        });

        profile = findViewById(R.id.user_logo);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,User_Profile.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        find = findViewById(R.id.dashboard_Find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Find_Location.class);
                startActivity(intent);
            }
        });
    }

    private void startQRScan(String prompt) {
        currentPrompt = prompt;
        Log.d("startScan Prompt",currentPrompt);
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt(prompt);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                results = intentResult.getContents();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(results);
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(MainActivity.this, "Entry Result", Toast.LENGTH_SHORT).show();
                            handleEntryScanResult(results);
                        } else {
                            handleExitScanResult(results);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleEntryScanResult(String result) {
        Intent intent = new Intent(MainActivity.this, Confirm_slot.class);
        intent.putExtra("userId", userId);
        intent.putExtra("spotId", result);
        startActivity(intent);
    }

    private void handleExitScanResult(String result) {
        Intent intent = new Intent(MainActivity.this, Exit.class);
        intent.putExtra("userId", userId);
        intent.putExtra("spotId", result);
        startActivity(intent);
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