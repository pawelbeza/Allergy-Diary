package com.example.allergydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    ArrayList<SeekBar> seekBars = new ArrayList<>();
    Switch simpleSwitch;
    private String date;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        seekBars.add((SeekBar) findViewById(R.id.seekBar1));

        simpleSwitch = findViewById(R.id.Switch);
        db = new DatabaseHelper(this);
        getCurrDate();

        calendarView();
        Button btnToDataBase = findViewById(R.id.btnToDataBase);
        btnToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, DataListActivity.class);
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
    }

    @Override
    protected void onPause() {
        int[] seekBarValues = new int[2];
        SeekBar seekBar = findViewById(R.id.seekBar1);
        seekBarValues[0] = seekBar.getProgress();
        seekBarValues[1] = simpleSwitch.isChecked() ? 1 : 0;
        addData(seekBarValues);
        super.onPause();
    }

    private void calendarView() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = getString(R.string.date, dayOfMonth, month, year);
            }
        });
    }

    private void addData(int[] seekBarValues) {
        boolean insertData = db.addData(date, seekBarValues);
        if (insertData) {
            toastMessage("Insertion successful");
        } else {
            toastMessage("Insertion failure");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(CalendarActivity.this, message, Toast.LENGTH_LONG).show();
    }
}