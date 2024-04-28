package com.example.parking_management;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdate();
            }
        });
    }

    private void performUpdate() {
        String Name = name.getText().toString();
        String Username = username.getText().toString();
        String Phone = phone.getText().toString();

        if (Name.isEmpty()) {
            name.setError("Enter the Name.");
        } else if (Username.isEmpty()) {
            username.setError("Enter the username.");
        } else if (Phone.isEmpty()) {
            phone.setError("Enter the Phone Number");
        } else if (Phone.length() < 10 || Phone.length() > 13) {
            phone.setError("Enter Correct Phone Number");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            userId = mAuth.getCurrentUser().getUid();
            fetchAndSetEmail();
            UpdateDataToFirebase(Name, Username, Phone);
            progressBar.setVisibility(View.INVISIBLE);
            SendUserToNextActivity();
            Toast.makeText(update_Profile.this, "Update Complete", Toast.LENGTH_SHORT).show();
        }
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}