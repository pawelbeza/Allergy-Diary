package com.example.allergydiary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import java.util.Calendar;

public class DiaryActivity extends AppCompatActivity {
    private static final String TAG = "DiaryActivity";

    private String date;
    private DatabaseHelper db;
    private SeekBar seekBar;
    private Switch simpleSwitch;

    //TODO Fix showing starting value of seekBars
    //TODO Fix adding feeling to multiple days
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        db = new DatabaseHelper(this);
        seekBar = findViewById(R.id.seekBar);
        simpleSwitch = findViewById(R.id.Switch);

        getCurrDate();
        calendarView();

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addData();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                addData();
            }
        });

        Button btnToDataBase = findViewById(R.id.btnToDataBase);
        btnToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, DataListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        date = getString(R.string.date, dayOfMonth, month, year);
        setSavedValues();
    }

    private void setSavedValues() {
        Cursor cursor = db.getDataBaseContents(date);
        if(cursor == null || cursor.getCount() == 0) //then there is no record with current date
            return;
        cursor.moveToNext();
        int feeling = cursor.getInt(cursor.getColumnIndex("FEELING"));
        seekBar.setProgress(feeling);

        int medicine = cursor.getInt(cursor.getColumnIndex("MEDICINE"));
        simpleSwitch.setChecked(medicine == 1);
    }

    private void calendarView() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = getString(R.string.date, dayOfMonth, month, year);
            }
        });
    }

    private void addData() {
        int[] seekBarValues = new int[2];
        seekBarValues[0] = seekBar.getProgress();
        seekBarValues[1] = simpleSwitch.isChecked() ? 1 : 0;

        boolean insertData = db.addData(date, seekBarValues);
        if (insertData) {
            Log.d(TAG, "addData: "+ "Insertion successful");
        } else {
            Log.d(TAG, "addData: " + "Insertion failure");
        }
    }
}