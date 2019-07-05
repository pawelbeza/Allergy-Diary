package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class ChartsActivity extends AppCompatActivity{
    private static final String TAG = "ChartsActivity";

    //TODO DatePicker
    //TODO add some indication that medicine was taken on given day

    private BarChart barChart;
    private Calendar cal;
    private DatabaseHelper db;

    private ArrayList<BarEntry> Values = new ArrayList<>();
    long referenceTimestamp = Long.MAX_VALUE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        db = new DatabaseHelper(this);

        barChart = findViewById(R.id.BarChart);

        buttonOnClickListener(R.id.week, 0, 0, -7);
        buttonOnClickListener(R.id.month, 0, -1, 0);
        buttonOnClickListener(R.id.months, 0, -3, 0);

        (findViewById(R.id.week)).performClick();
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
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        barChart.animateY(1500);

        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);


        ValueFormatter xAxisFormatter = new DateAxisValueFormatter(referenceTimestamp);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(10, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(10f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setGranularity(1f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(10f);

        BarDataSet barDataSet = new BarDataSet(Values, "Feeling");
        int startColor = ContextCompat.getColor(this, android.R.color.holo_green_light);
        int endColor = ContextCompat.getColor(this, android.R.color.holo_red_dark);
        barDataSet.setGradientColor(startColor, endColor);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);

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
            referenceTimestamp = Math.min(referenceTimestamp, date);
            Values.add(new BarEntry(date, feeling));
        }

        for (int i=0;i<Values.size();i++) {
            float tmp = Values.get(i).getX() - referenceTimestamp;
            Values.get(i).setX(tmp);
        }

    }
}
