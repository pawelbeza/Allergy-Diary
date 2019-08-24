package com.example.allergydiary;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateAxisValueFormatter extends ValueFormatter {
    private long referenceTimestamp; // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    public DateAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("MM:dd", Locale.ENGLISH);
        this.mDate = new Date();
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;

        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        // Convert timestamp to month-day
        return getDate(TimeUnit.DAYS.toMillis(originalTimestamp));
    }

    private String getDate(long timestamp) {
        try {
            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

}
