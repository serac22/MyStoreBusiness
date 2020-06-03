package com.example.mystorebusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logo;
    TextView tvAppName;
    Animation left_to_right,right_to_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo        = findViewById(R.id.logo);
        tvAppName   = findViewById(R.id.tvAppName);

        left_to_right  = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        right_to_left = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

        logo.setAnimation(right_to_left);
        tvAppName.setAnimation(left_to_right);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent a = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(a);
                finish();
            }
        },3000);
    }
}
