package com.example.allergydiary.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptom;
import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptomViewModel;
import com.example.allergydiary.DateAxisValueFormatter;
import com.example.allergydiary.R;
import com.example.allergydiary.Widgets.InlineCalendarPickerWidget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartsFragment extends Fragment {
    //TODO add support for landscape view
    //TODO Add back button
    private long referenceTimestamp = Long.MAX_VALUE;
    private BarChart barChart;
    private InlineCalendarPickerWidget calendarPicker;
    private ArrayList<BarEntry> Values = new ArrayList<>();
    private AllergicSymptomViewModel symptomViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        symptomViewModel = ViewModelProviders.of(getActivity()).get(AllergicSymptomViewModel.class);

        calendarPicker = view.findViewById(R.id.inlineCalendar);
        calendarPicker.setListener(new InlineCalendarPickerWidget.MyOnClickListener() {
            @Override
            public void onClickListener() {
                Calendar cal = calendarPicker.getCalendar();

                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);

                cal.set(Calendar.DAY_OF_MONTH, 1);
                long startDate = cal.getTimeInMillis();

                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.MILLISECOND, -1);
                long endDate = cal.getTimeInMillis();
                makeAndDisplayGraph(startDate, endDate);
            }
        });

        barChart = view.findViewById(R.id.BarChart);

        getCurrMonth();
    }

    private void getCurrMonth() {
        Calendar cal = calendarPicker.getCalendar();

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        long startDate = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        long endDate = cal.getTimeInMillis();
        makeAndDisplayGraph(startDate, endDate);
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

        BarDataSet barDataSet = new BarDataSet(Values, "AllergicSymptom");
        int startColor = ContextCompat.getColor(getActivity(), R.color.bright_green);
        int endColor = ContextCompat.getColor(getActivity(), R.color.bright_blue);
        barDataSet.setGradientColor(startColor, endColor);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);

        barChart.setData(barData);
        barChart.invalidate();
    }

    private void getDataInRange(long fromDate, long toDate) {
        List<AllergicSymptom> symptoms = symptomViewModel.getDataBaseContents(fromDate, toDate);
        for (AllergicSymptom symptom : symptoms) {
            long date = symptom.getDate();
            date = TimeUnit.MILLISECONDS.toDays(date) + 1; // +1 because TimeUnit rounds down
            int feeling = symptom.getFeeling();
            boolean medicine = symptom.isMedicine();
            referenceTimestamp = Math.min(referenceTimestamp, date);
            if (medicine) {
                Drawable icon;
                if (android.os.Build.VERSION.SDK_INT >= 21)
                    icon = getResources().getDrawable(R.drawable.ic_pill, null);
                else
                    icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_pill);
                Values.add(new BarEntry(date, feeling, icon));
            }
            else
                Values.add(new BarEntry(date, feeling));
        }

        for (int i = 0; i < Values.size(); i++) {
            float tmp = Values.get(i).getX() - referenceTimestamp;
            Values.get(i).setX(tmp);
        }
    }
}
