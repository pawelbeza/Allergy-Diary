package com.example.allergydiary.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.allergydiary.Activities.MainActivity;
import com.example.allergydiary.TimeHelper;

import java.util.Calendar;
import java.util.Objects;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean b;
        String booting = "android.intent.action.BOOT_COMPLETED";
        if (Build.VERSION.SDK_INT >= 19) {
            b = Objects.equals(intent.getAction(), booting);
        } else {
            String action = intent.getAction();
            b = (action != null && action.equals(booting));
        }
        if (b) {

            // on device boot complete, reset the alarm
            for (int i = 0; i < 3; i++) {
                final SharedPreferences sharedPref = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);

                Boolean isChecked = sharedPref.getBoolean("PopUpScheduleChecked" + i, false);
                if (!isChecked) {
                    continue;
                }

                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                String[] notificationContent = Notification.getNotificationContents(i);
                alarmIntent.putExtra("notificationContent", notificationContent);
                alarmIntent.putExtra("id", i);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                String date = sharedPref.getString("PopUpSchedule" + i, "20:00");
                Calendar tmpCal = TimeHelper.stringToCalendar(date);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, tmpCal.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, tmpCal.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, 0);

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
            }
        }
    }
}
