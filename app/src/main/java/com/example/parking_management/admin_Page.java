package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class admin_Page extends AppCompatActivity {

    String userId;
    Button qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        userId=getIntent().getStringExtra("userId");

        qr = findViewById(R.id.qr_generate);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(admin_Page.this, qr_code.class);
                startActivity(intent1);
            }
        });
    }
}