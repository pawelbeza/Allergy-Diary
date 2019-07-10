package com.example.allergydiary;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_settings);

        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignClickHandler(R.id.everyDay, R.id.switch1);
        assignClickHandler(R.id.morning, R.id.switch2);
        assignClickHandler(R.id.evening, R.id.switch3);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void assignClickHandler(int tvID, int swID) {
        final TextView textView = findViewById(tvID);
        final Switch sw = findViewById(swID);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView.setText(hourOfDay + ":" + minute);
                        sw.setChecked(true);
                    }
                }, 0, 0, DateFormat.is24HourFormat(SettingsActivity.this));
                timePickerDialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < switchIDs.length; i++) {
            Switch sw = findViewById(switchIDs[i]);
            editor.putBoolean("PopUpScheduleChecked" + i, sw.isChecked());

            TextView tv = findViewById(tvIDs[i]);
            editor.putString("PopUpSchedule" + i, tv.getText().toString());
        }

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < 3; i++) {
            Boolean isChecked = sharedPref.getBoolean("PopUpScheduleChecked" + i, false);
            Switch sw = findViewById(switchIDs[i]);
            sw.setChecked(isChecked);

            String hour = sharedPref.getString("PopUpSchedule" + i, "12:00");
            TextView tv = findViewById(tvIDs[i]);
            tv.setText(hour);
        }
    }
}
