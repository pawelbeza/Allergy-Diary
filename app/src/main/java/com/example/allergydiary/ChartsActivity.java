package com.example.allergydiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.LineData;


import java.util.ArrayList;

public class ChartsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        ArrayList<Entry> Vals = new ArrayList<>();
        Vals.add(new Entry(0, 60f));
        Vals.add(new Entry(1, 30f));
        Vals.add(new Entry(2, 60f));
        Vals.add(new Entry(3, 50f));
        Vals.add(new Entry(4, 30f));
        Vals.add(new Entry(5, 20f));
        displayGraph(Vals);
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

        mChart.setData(data);
        mChart.invalidate();
    }
}
