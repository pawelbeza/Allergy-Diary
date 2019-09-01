package com.example.allergydiary.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allergydiary.R;
import com.jaredrummler.android.widget.AnimatedSvgView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "Splash";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AnimatedSvgView svgView = findViewById(R.id.animated_svg_view);
        svgView.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 1200);
    }
}
