package com.example.allergydiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    ArrayList<SeekBar> seekBars = new ArrayList<>();
    Switch simpleSwitch;
    private TextView date;
    private DatabaseHelper db;
    private int myear;
    private int mmonth;
    private int mdayOfMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        seekBars.add((SeekBar) findViewById(R.id.seekBar1));
        seekBars.add((SeekBar) findViewById(R.id.seekBar2));
        seekBars.add((SeekBar) findViewById(R.id.seekBar3));
        seekBars.add((SeekBar) findViewById(R.id.seekBar4));
        seekBars.add((SeekBar) findViewById(R.id.seekBar5));

        simpleSwitch = findViewById(R.id.Switch);
        db = new DatabaseHelper(this);
        date = findViewById(R.id.textView1);

        getCurrentDate();
        datePicker();
        Button btnToDataBase = findViewById(R.id.btnToDataBase);
        btnToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, DataListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        int[] seekBarValues = new int[6];
        for (int i = 0; i < seekBars.size(); i++)
            seekBarValues[i] = seekBars.get(i).getProgress();
        seekBarValues[5] = simpleSwitch.isChecked() ? 1 : 0;
        addData(seekBarValues);
        super.onPause();
    }

    private void getCurrentDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        myear = cal.get(java.util.Calendar.YEAR);
        mmonth = cal.get(java.util.Calendar.MONTH) + 1;
        mdayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private void datePicker() {
        Button btn1 = findViewById(R.id.btnToCalendar);
        date = findViewById(R.id.textView1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CalendarActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myear = year;
                                mmonth = month + 1;
                                mdayOfMonth = dayOfMonth;
                                Log.d(TAG, "CalendarActivity: " + mdayOfMonth + "/" + mmonth + "/" + myear);

                                date.setText(getString(R.string.date, dayOfMonth, month, year));
                            }
                        }, myear, mmonth, mdayOfMonth);

                datePickerDialog.show();
            }
        });
        date.setText(getString(R.string.date, mdayOfMonth, mmonth, myear));
    }

    private void addData(int[] seekBarValues) {
        boolean insertData = db.addData(date.getText().toString(), seekBarValues);
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