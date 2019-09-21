package com.example.allergydiary.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allergydiary.Fragments.SplashScreenFragment;
import com.example.allergydiary.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_onboarding);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                new SplashScreenFragment()).commit();
    }
}
