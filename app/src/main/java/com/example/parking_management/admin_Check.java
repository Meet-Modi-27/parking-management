package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class admin_Check extends AppCompatActivity {

    String userId;
    TextInputEditText password;
    Button go;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check);

        userId=getIntent().getStringExtra("userId");
        password = findViewById(R.id.admin_password);
        go = findViewById(R.id.admin_btn);
        
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("Admin@123")) {
                    Intent intent = new Intent(admin_Check.this, admin_Page.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else{
                    Toast.makeText(admin_Check.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(admin_Check.this,User_Profile.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}