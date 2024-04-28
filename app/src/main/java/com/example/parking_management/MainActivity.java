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

import com.example.parking_management.adapters.dashboard_adapter;
import com.example.parking_management.models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    dashboard_adapter adapter;
    DatabaseReference usersRef;
    CardView scan_qr;
    CardView find;
    String results;
    String userId;
    ImageView profile;


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
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan A QR");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            String contents = intentResult.getContents();
            if (contents != null){
                results = intentResult.getContents();
                Intent intent = new Intent(MainActivity.this,Confirm_slot.class);
                intent.putExtra("userId",userId);
                intent.putExtra("spotId",results);
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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