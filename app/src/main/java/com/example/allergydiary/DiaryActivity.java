package com.example.allergydiary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DiaryActivity extends AppCompatActivity {
    private static final String TAG = "DiaryActivity";

    private long date;
    private DatabaseHelper db;
    private SeekBar seekBar;
    private Switch simpleSwitch;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
        date = cal.getTimeInMillis();
        setSavedValues();
    }

    private void setSavedValues() {
        Cursor cursor = db.getDataBaseContents(date);
        if (cursor == null || cursor.getCount() == 0) {//then there is no record with current date
            seekBar.setProgress(0);
            simpleSwitch.setChecked(false);
            return;
        }
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
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
                date = cal.getTimeInMillis();
                setSavedValues();
            }
        });
    }

    private void addData() {
        int[] seekBarValues = new int[2];
        seekBarValues[0] = seekBar.getProgress();
        seekBarValues[1] = simpleSwitch.isChecked() ? 1 : 0;

        boolean insertData = db.addData(date, seekBarValues);
        if (insertData) {
            Log.d(TAG, "addData: " + "Insertion successful");
        } else {
            Log.d(TAG, "addData: " + "Insertion failure");
        }
    }
}