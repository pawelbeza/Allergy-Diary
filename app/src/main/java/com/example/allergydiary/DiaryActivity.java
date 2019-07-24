package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DiaryActivity extends Fragment {
    private static final String TAG = "DiaryActivity";
    private long date;
    private DatabaseHelper db;
    private SeekBar seekBar;
    private Switch simpleSwitch;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());

        calendarView = view.findViewById(R.id.calendarView);

        calendarView();

        simpleSwitch = view.findViewById(R.id.Switch);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addData();
            }
        });

        seekBar = view.findViewById(R.id.seekBar);
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

        Button btnToDataBase = view.findViewById(R.id.btnToDataBase);
        btnToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DataListActivity()).commit();
            }
        });

        getCurrDate();
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