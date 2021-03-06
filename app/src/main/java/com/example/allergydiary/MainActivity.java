package com.example.allergydiary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allergydiary.Fragments.MainFragment;
import com.example.allergydiary.Fragments.SplashScreenFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_onboarding);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                new SplashScreenFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (!MainFragment.onBackPressed() ) {
            super.onBackPressed();
        }
    }
}
