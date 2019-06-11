package com.example.allergydiary;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class Callendar extends AppCompatActivity {
    private static final String TAG = "Callendar";
    TextView date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callendar);

        Calendar();
    }
    private void Calendar(){
        Button btn1 = findViewById(R.id.btnToCalendar);
        date = findViewById(R.id.textView1);

        btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Callendar.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    date.setText(getString(R.string.date, dayOfMonth, month, year));
                                }
                            }, year, month, dayOfMonth);

                    datePickerDialog.show();
                }
            });
    }
}
