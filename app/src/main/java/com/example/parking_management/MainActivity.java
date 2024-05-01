package com.example.parking_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    CardView scan_qr, exit, find, avail_slots;
    ImageView profile;
    String results, userId, currentPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentPrompt = savedInstanceState.getString("CurrentPrompt");
        }

        userId = getIntent().getStringExtra("userId");
        rv = findViewById(R.id.dashboard_name_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(userId);
        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        adapter = new dashboard_adapter(options);
        rv.setAdapter(adapter);

        initViews();
    }

    private void initViews() {
        scan_qr = findViewById(R.id.dashboard_scan_qr);
        scan_qr.setOnClickListener(v -> startQRScan("Scan for Entry"));

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(v -> startQRScan("Scan for Exit"));

        avail_slots = findViewById(R.id.dashboard_available_Slots);
        avail_slots.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Available_slots.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        profile = findViewById(R.id.user_logo);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, User_Profile.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        find = findViewById(R.id.dashboard_Find);
        find.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Find_Location.class)));
    }

    private void startQRScan(String prompt) {
        currentPrompt = prompt;
        Log.d("startScan Prompt", currentPrompt);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt(prompt);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            results = intentResult.getContents();
            String[] parts = results.split("_");
            if (parts.length > 0) {
                String loc = parts[0];
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("qr").child("empty").child(loc).child(results);
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
        }
    }

    private void handleEntryScanResult(String result) {
        Intent intent = new Intent(this, Confirm_slot.class);
        intent.putExtra("userId", userId);
        intent.putExtra("spotId", result);
        startActivity(intent);
    }

    private void handleExitScanResult(String result) {
        Intent intent = new Intent(this, Exit.class);
        intent.putExtra("userId", userId);
        intent.putExtra("spotId", result);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CurrentPrompt", currentPrompt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
