package com.example.allergydiary.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import com.example.allergydiary.TimeHelper;

import java.util.Calendar;

public class Notifications {
    public static void setAlarm(Context context, Boolean dailyNotify, String hour, int notificationId, String[] notificationContent) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("notificationContent", notificationContent);
        alarmIntent.putExtra("id", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (dailyNotify) {
            //enable daily notifications
            Calendar calendar = TimeHelper.stringToCalendar(hour);
            // if notification time is before selected time, send notification the next day
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        } else { //disable Daily Notification
            if (PendingIntent.getBroadcast(context, notificationId, alarmIntent, 0) != null && manager != null) {
                manager.cancel(pendingIntent);
            }
        }
    }
}
