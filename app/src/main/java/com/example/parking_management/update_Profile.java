package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking_management.models.usersModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_Profile extends AppCompatActivity {

    TextInputEditText name, username, phone;

    ProgressBar progressBar;
    String userId;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference db;
    Button update;
    TextView dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.update_name);
        username = findViewById(R.id.update_username);
        phone = findViewById(R.id.update_phone);
        dummy = findViewById(R.id.dummy);

        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("users");
        update = findViewById(R.id.update_profile);

        if (mUser != null) {
            userId = mUser.getUid();
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }

        fetchData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdate();
            }
        });
    }

    private void fetchData() {
        DatabaseReference dbRef = db.child("Reg_Users").child(userId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    usersModel um = snapshot.getValue(usersModel.class);

                    if (um != null) {
                        name.setText(um.getName());
                        username.setText(um.getUsername());
                        phone.setText(um.getNumber());
                    } else {
                        Toast.makeText(update_Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(update_Profile.this, "Data Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(update_Profile.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performUpdate() {
        String Name = name.getText().toString().trim();
        String Username = username.getText().toString().trim();
        String Phone = phone.getText().toString().trim();

        if (Name.isEmpty()) {
            name.setError("Enter the Name");
            return;
        }
        if (Username.isEmpty()) {
            username.setError("Enter the Username");
            return;
        }
        if (Phone.isEmpty()) {
            phone.setError("Enter the Phone Number");
            return;
        }
        if (Phone.length() < 10 || Phone.length() > 13) {
            phone.setError("Enter Correct Phone Number");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        fetchAndSetEmail();
        UpdateDataToFirebase(Name, Username, Phone);

        progressBar.setVisibility(View.INVISIBLE);
        SendUserToNextActivity();
        Toast.makeText(update_Profile.this, "Update Complete", Toast.LENGTH_SHORT).show();
    }

    private void fetchAndSetEmail() {
        String email = mUser.getEmail();
        dummy.setText(email);
    }

    private void UpdateDataToFirebase(String NAME, String USERNAME, String PHONE) {
        DatabaseReference userRef1 = db.child("Reg_Users").child(userId);
        usersModel userModel = new usersModel(NAME, USERNAME, dummy.getText().toString(), PHONE, userId);
        userRef1.setValue(userModel);
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(update_Profile.this, User_Profile.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
