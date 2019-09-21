package com.example.allergydiary.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allergydiary.R;
import com.jaredrummler.android.widget.AnimatedSvgView;

public class SplashActivity extends AppCompatActivity {
    private String prefName = "onboarding";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AnimatedSvgView svgView = findViewById(R.id.animated_svg_view);
        svgView.start();
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            boolean showedOnboarding = sharedPreferences.getBoolean(prefName, false);
            if (showedOnboarding) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(prefName, true);
                editor.apply();
                startActivity(new Intent(getApplicationContext(), OnboardingActivity.class));
            }
            finish();
        }, 1200);
    }
}
