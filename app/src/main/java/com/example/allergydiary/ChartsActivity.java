package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.LineData;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartsActivity extends AppCompatActivity{
    private static final String TAG = "ChartsActivity";

    private ArrayList<Entry> Values = new ArrayList<>();
    long referenceTimestamp = Long.MAX_VALUE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        getDataInRange("2017-6-1", "2029-6-30");
        displayGraph(Values);
    }
    private void displayGraph(ArrayList<Entry> Values) {

        LineChart mChart = findViewById(R.id.LineChart);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        LineDataSet set = new LineDataSet(Values, "Feeling");

        set.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        ValueFormatter xAxisFormatter = new DateAxisValueFormatter(referenceTimestamp);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);

        mChart.setData(data);
        mChart.invalidate();
    }
    private void getDataInRange(String fromDate, String toDate) {
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getDataBaseContents(fromDate, toDate);
        cursor.getCount();

        long reference_timestamps = Long.MAX_VALUE;
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
            int feeling = cursor.getInt(cursor.getColumnIndexOrThrow("FEELING"));
            long timestamp_date = toTimestamp(date);
            referenceTimestamp = Math.min(reference_timestamps, timestamp_date);
            Values.add(new Entry(timestamp_date, feeling));
        }

        for (int i=0;i<Values.size();i++) {
            float tmp = Values.get(i).getX() - referenceTimestamp;
            Values.get(i).setX(tmp);
        }

    }
    private long toTimestamp(String str_date){
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(str_date);
            return date.getTime();
        }
        catch (Exception e) {
            return 0;
        }


    }
}
