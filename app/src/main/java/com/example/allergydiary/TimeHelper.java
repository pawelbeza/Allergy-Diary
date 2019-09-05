package com.example.allergydiary;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class TimeHelper {
    public static Calendar stringToCalendar(String time) {
        String[] hourMin = time.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int minute = Integer.parseInt(hourMin[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(calendar.getTime());
        return formatted;
    }
}
