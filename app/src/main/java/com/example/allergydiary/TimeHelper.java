package com.example.allergydiary;

import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
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

    public static String calendarToString(Context context, Calendar calendar) {
        Locale locale = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                context.getResources().getConfiguration().getLocales().get(0) :
                context.getResources().getConfiguration().locale;
        SimpleDateFormat format1 = new SimpleDateFormat("MMM yyyy", locale);
        return format1.format(calendar.getTime());
    }

    public static String timeStampToString(Context context, long calendar) {
        Locale locale = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                context.getResources().getConfiguration().getLocales().get(0) :
                context.getResources().getConfiguration().locale;
        SimpleDateFormat format1 = new SimpleDateFormat("MMM yyyy", locale);
        return format1.format(calendar);
    }

    public static Calendar getFirstDayOfMonth(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal;
    }

    public static Calendar getLastDayOfMonth(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal;
    }
}
