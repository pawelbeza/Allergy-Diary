package com.example.allergydiary.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptom;
import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptomViewModel;
import com.example.allergydiary.BuildConfig;
import com.example.allergydiary.DateAxisValueFormatter;
import com.example.allergydiary.R;
import com.example.allergydiary.StatisticsRecycleView.StatisticsAdapter;
import com.example.allergydiary.Widgets.InlineCalendarPickerWidget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.stone.vega.library.VegaLayoutManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChartsFragment extends Fragment implements OnSelectDateListener {
    private long referenceTimestamp = Long.MAX_VALUE;
    private BarChart barChart;
    private InlineCalendarPickerWidget calendarPicker;
    private ArrayList<BarEntry> Values = new ArrayList<>();
    private ArrayList<BarEntry> pdfValues = new ArrayList<>();
    private AllergicSymptomViewModel symptomViewModel;
    private RecyclerView recyclerView;
    private StatisticsAdapter statisticsAdapter;
    private List<AllergicSymptom> symptoms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        symptomViewModel = ViewModelProviders.of(getActivity()).get(AllergicSymptomViewModel.class);

        barChart = view.findViewById(R.id.BarChart);


        calendarPicker = view.findViewById(R.id.inlineCalendar);
        calendarPicker.setListener(() -> {
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
            makeAndDisplayGraph(Values, barChart, true, startDate, endDate);
            updateStatistics(symptoms);
        });

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        initializeGraph();

        Button button = getActivity().findViewById(R.id.generateReport);
        button.setOnClickListener(view1 -> openRangePicker());
    }

    private void openRangePicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -1);

        Calendar max = Calendar.getInstance();

        List<Calendar> selectedDays = new ArrayList<>();
        selectedDays.add(min);
        selectedDays.addAll(CalendarUtils.getDatesRange(min, max));
        selectedDays.add(max);

        DatePickerBuilder rangeBuilder = new DatePickerBuilder(getActivity(), this)
                .setPickerType(CalendarView.RANGE_PICKER)
                .setSelectedDays(selectedDays)
                .setMaximumDate(max)
                .setHeaderColor(R.color.dirty_green)
                .setSelectionColor(R.color.dirty_green)
                .setTodayLabelColor(R.color.colorAccent);

        DatePicker rangePicker = rangeBuilder.build();
        rangePicker.show();
    }

    private void saveGraphToPDF(long fromDate, long toDate){
        if (!isExternalStorageReadable()) return;

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2250, 1400, 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_layout, null);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);

        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        BarChart barChartToPDF = content.findViewById(R.id.barChartToPDF);

        makeAndDisplayGraph(pdfValues, barChartToPDF, false, fromDate, toDate);

        content.draw(page.getCanvas());

        document.finishPage(page);

        String targetPdf = getActivity().getExternalFilesDir(null).getPath() + File.separator + "allergy_report.pdf";
        try {
            File f = new File(targetPdf);
            document.writeTo(new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File fileWithinMyDir = new File(targetPdf);


        if(fileWithinMyDir.exists()) {
            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile
                    .putExtra(Intent.EXTRA_STREAM, uri)
                    .setType("application/pdf")
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    .putExtra(Intent.EXTRA_SUBJECT, "Allergy Symptoms report")
                    .putExtra(Intent.EXTRA_TEXT, "Allergy Symptoms report");

            startActivity(intentShareFile);
        }
    }


    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }


    private void initializeGraph() {
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

        makeAndDisplayGraph(Values, barChart, true, startDate, endDate);
        updateStatistics(symptoms);
    }

    private void makeAndDisplayGraph(ArrayList<BarEntry> Values, BarChart barChart, boolean animate, long fromDate, long toDate) {
        Values.clear();
        getDataInRange(Values, fromDate, toDate);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        if (animate) barChart.animateY(1500);

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

    private void getDataInRange(ArrayList<BarEntry> Values, long fromDate, long toDate) {
        symptoms = symptomViewModel.getDataBaseContents(fromDate, toDate);
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

    private void updateStatistics(List<AllergicSymptom> symptoms) {
        List<Integer> values = new ArrayList<>();
        for (AllergicSymptom symptom : symptoms) {
            values.add(symptom.getFeeling());
        }

        if (values.size() == 0)
            return;

        Collections.sort(values);

        double medianValue = medianValue(values);
        double averageValue = averageValue(values);
        int minValue = values.get(0);
        int maxValue = values.get(values.size() - 1);
        int numOfRecords = values.size();
        int numOfPillsTaken = 0;
        for (AllergicSymptom symptom : symptoms) numOfPillsTaken += (symptom.isMedicine()) ? 1 : 0;

        double[] statistics = {numOfRecords, averageValue, medianValue, minValue, maxValue, numOfPillsTaken};
        if (statisticsAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            statisticsAdapter = new StatisticsAdapter(getActivity(), statistics);
            recyclerView.setAdapter(statisticsAdapter);
        } else {
            statisticsAdapter.swapDataSet(statistics);
            statisticsAdapter.notifyDataSetChanged();
        }
    }

    private double averageValue(List<Integer> values) {
        int sum = 0;
        for (int x : values) sum += x;
        return (double) sum / values.size();
    }

    private double medianValue(List<Integer> values) { //assume that list is sorted
        int listSize = values.size();
        if (listSize % 2 == 1)
            return values.get(listSize / 2);
        else
            return (double) values.get(listSize / 2 - 1) + values.get(listSize / 2);
    }

    @Override
    public void onSelect(List<Calendar> calendar) {
        Calendar fromDate = calendar.get(0);
        Calendar toDate = calendar.get(calendar.size() - 1);
        saveGraphToPDF(fromDate.getTimeInMillis(), toDate.getTimeInMillis());
    }
}
