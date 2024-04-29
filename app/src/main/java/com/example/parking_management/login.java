package com.example.parking_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    Button new_user;

    Button Go;

    ImageView imageView;
    TextView title,subtext;

    TextInputLayout email1,pass1;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    ProgressBar progressBar;

    TextInputEditText email,pass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        imageView = findViewById(R.id.car_logo);
        title = findViewById(R.id.welcome);
        subtext = findViewById(R.id.welcome_st);
        email = findViewById(R.id.email);
        email1 = findViewById(R.id.email1);
        pass = findViewById(R.id.password);
        pass1 = findViewById(R.id.password1);
        Go = findViewById(R.id.login_btn);
        new_user = findViewById(R.id.new_user);
        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();

        // Set the focused stroke color programmatically
        email1.setBoxStrokeColor(Color.BLACK);
        email1.setHintTextColor(ColorStateList.valueOf(Color.BLACK));

        pass1.setBoxStrokeColor(Color.BLACK);
        pass1.setHintTextColor(ColorStateList.valueOf(Color.BLACK));


        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });



        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,registration.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(imageView,"Parking_logo");
                pairs[1] = new Pair<View,String>(title,"Parking_text");
                pairs[2] = new Pair<View,String>(subtext,"subtext");
                pairs[3] = new Pair<View,String>(email1,"field1");
                pairs[4] = new Pair<View,String>(pass1,"field2");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
    }
    private void performLogin() {
        String Email = email.getText().toString();
        String Pass = pass.getText().toString();

        if (!Email.matches(EmailPattern)){
            email.setError("Enter Correct Email Id");
        } else if(Pass.isEmpty()|| Pass.length() < 6){
            pass.setError("Minimum Password length is 6 characters.");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            checkEmailInDatabaseAndRetrieveUserId(Email, userId);
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        // Handle authentication failure
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            // Password is incorrect
                            pass1.setError("Incorrect password.");
                        } else {
                            // Other error occurred
                            Toast.makeText(login.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void checkEmailInDatabaseAndRetrieveUserId(String email, String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve user data
                        String retrievedUserId = userSnapshot.getKey(); // Get the user ID directly
                        if (retrievedUserId != null && retrievedUserId.equals(userId)) {
                            // Email matches and user ID retrieved
                            SendUserToNextActivity(retrievedUserId);
                            return;
                        }
                    }

                    // User ID not found for the matched email
                    Toast.makeText(login.this, "User ID not found", Toast.LENGTH_SHORT).show();
                } else {
                    // Email not found in the database
                    Toast.makeText(login.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void SendUserToNextActivity(String userid) {
        Intent intent = new Intent(login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId",userid);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            SendUserToNextActivity(mAuth.getCurrentUser().getUid());
        }
    }
}