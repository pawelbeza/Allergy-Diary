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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Callendar extends AppCompatActivity {
    private static final String TAG = "Callendar";
    TextView date;
    DatabaseHelper db;
    Button btn1;
    int myear;
    int mmonth;
    int mdayOfMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callendar);

        db = new DatabaseHelper(this);
        date = findViewById(R.id.textView1);

        getCurrentDate();
        Calendar();
        seekBar();
        Button btnToDataBase = findViewById(R.id.btnToDataBase);
        btnToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Callendar.this, DataList.class);
                startActivity(intent);
            }
        });
    }
    private void getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        myear = cal.get(Calendar.YEAR);
        mmonth = cal.get(Calendar.MONTH);
        mdayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void Calendar() {
        btn1 = findViewById(R.id.btnToCalendar);
        date = findViewById(R.id.textView1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Callendar.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myear = year;
                                mmonth = month;
                                mdayOfMonth = dayOfMonth;
                                Log.d(TAG, "Calendar: " + mdayOfMonth + "/" + mmonth + "/" + myear);

                                date.setText(getString(R.string.date, dayOfMonth, month, year));
                            }
                        }, myear, mmonth, mdayOfMonth);

                datePickerDialog.show();
            }
        });
        date.setText(getString(R.string.date, mdayOfMonth, mmonth, myear));

    }
    private void seekBar() {
        SeekBar seekBar1 = findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                addData(progressVal);
            }
        });
    }
    private void addData(int feeling){
        boolean insertData = db.addData(date.getText().toString() ,feeling);
        if(insertData){
            toastMessage("Insertion successful");
        }
        else{
            Log.d(TAG, "Insertion failure");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(Callendar.this, message, Toast.LENGTH_LONG).show();
    }
}