package com.example.allergydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButtonOnClickListener(R.id.goToDiary, CalendarActivity.class);
        setButtonOnClickListener(R.id.goToGraphs, ChartsActivity.class);
        setButtonOnClickListener(R.id.goToForecast, ForecastActivity.class);
        setButtonOnClickListener(R.id.goToSettings, SettingsActivity.class);
    }

    private void setButtonOnClickListener(int id, final Class<?> cls) {
        Button goToActivity = findViewById(id);
        goToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cls);
                startActivity(intent);
            }
        });
    }
}
