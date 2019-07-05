package com.example.allergydiary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class ChartsActivity extends AppCompatActivity{
    private static final String TAG = "ChartsActivity";
    private Calendar cal;
    private DatabaseHelper db;

    private ArrayList<BarEntry> Values = new ArrayList<>();
    long referenceTimestamp = Long.MAX_VALUE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        db = new DatabaseHelper(this);

        buttonOnClickListener(R.id.week, 0, 0, -7);
        buttonOnClickListener(R.id.month, 0, -1, 0);
        buttonOnClickListener(R.id.months, 0, -3, 0);
    }

    private void buttonOnClickListener(int ID, final int addToYear, final int addToMonth, final int addToDay) {
        cal = Calendar.getInstance();
        GregorianCalendar gCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        final long toDate = gCal.getTimeInMillis();

        findViewById(ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = getTime(addToYear, addToMonth, addToDay);
                GregorianCalendar gCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                long fromDate = gCal.getTimeInMillis();
                makeAndDisplayGraph(fromDate, toDate);
            }
        });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private Calendar getTime(int addToYear, int addToMonth, int addToDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, addToYear);
        cal.add(Calendar.MONTH, addToMonth);
        cal.add(Calendar.DAY_OF_MONTH, addToDay);
        return cal;
    }

    private void makeAndDisplayGraph(long fromDate, long toDate) {
        Values.clear();
        getDataInRange(fromDate, toDate);
        displayGraph();
    }

    private void displayGraph() {

        BarChart barChart = findViewById(R.id.BarChart);

        barChart.getDescription().setEnabled(false);
//        mChart.setDragEnabled(true);
//        mChart.setScaleEnabled(true);

        BarDataSet barDataSet = new BarDataSet(Values, "Feeling");
        barDataSet.setColor(Color.RED);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        ValueFormatter xAxisFormatter = new DateAxisValueFormatter(referenceTimestamp);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setData(barData);
        barChart.invalidate();
    }
    private void getDataInRange(long fromDate, long toDate) {
        Cursor cursor = db.getDataBaseContents(fromDate, toDate);

        Log.d(TAG, "getDataInRange: " + cursor.getCount());

        while (cursor.moveToNext()) {
            long date = cursor.getLong(cursor.getColumnIndexOrThrow("DATE"));
            date = TimeUnit.MILLISECONDS.toDays(date);
            int feeling = cursor.getInt(cursor.getColumnIndexOrThrow("FEELING"));
            Log.d(TAG, "getDataInRange: " + date + " " + feeling);
            referenceTimestamp = Math.min(referenceTimestamp, date);
            Values.add(new BarEntry(date, feeling));
        }

        for (int i=0;i<Values.size();i++) {
            float tmp = Values.get(i).getX() - referenceTimestamp;
            Values.get(i).setX(tmp);
        }

    }
}
