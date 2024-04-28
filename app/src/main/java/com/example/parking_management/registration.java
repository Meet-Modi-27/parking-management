package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parking_management.models.usersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {

    Animation bottomAnim;

    Button back,register;

    TextInputEditText name,username,email,phone,pass;

    String EmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    ProgressBar progressBar;
    String userId;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.reg_bottom);

        register = findViewById(R.id.reg_next);
        back = findViewById(R.id.new_user);

        name= findViewById(R.id.reg_name);
        username= findViewById(R.id.reg_username);
        email= findViewById(R.id.reg_email);
        phone= findViewById(R.id.reg_phone);
        pass= findViewById(R.id.reg_password);

        progressBar = findViewById(R.id.progress_bar);
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("users");

        register.setAnimation(bottomAnim);
        back.setAnimation(bottomAnim);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        String Name = name.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();
        String Pass = pass.getText().toString();

        if (Name.isEmpty()){
            name.setError("Enter the Name.");
        } else if(Username.isEmpty()){
            username.setError("Enter the username.");
        } else if (!Email.matches(EmailPattern)){
            email.setError("Enter Correct Email Id");
        } else if (Phone.isEmpty()){
            phone.setError("Enter the Phone Number");
        } else if (Phone.length() < 10 || Phone.length() > 12){
            phone.setError("Enter Correct Phone Number");
        } else if(Pass.isEmpty()|| Pass.length() < 6){
            pass.setError("Minimum Password length is 6 characters.");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UpdateDataToFirebase(Name,Username,Email,Phone);
                                progressBar.setVisibility(View.INVISIBLE);
                                SendUserToNextActivity();
                                Toast.makeText(registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(registration.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void UpdateDataToFirebase(String NAME, String USERNAME, String EMAIL, String PHONE) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            DatabaseReference userRef = db.child("Reg_Users").child(userId);
            usersModel userModel = new usersModel(NAME, USERNAME, EMAIL, PHONE, userId);
            userRef.setValue(userModel);
        }
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(registration.this, registration_2.class);
        intent.putExtra("userId",userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}