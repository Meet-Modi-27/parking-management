package com.example.parking_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {

    Animation topAnim,bottomAnim;

    ImageView imageView;
    TextView title,slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView = findViewById(R.id.Logo);
        title = findViewById(R.id.title);
        slogan = findViewById(R.id.Slogan);

        imageView.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splash_screen.this,login.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View,String>(imageView,"Parking_logo");
            pairs[1] = new Pair<View,String>(title,"Parking_text");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(intent,options.toBundle());

        },2000);
    }
}