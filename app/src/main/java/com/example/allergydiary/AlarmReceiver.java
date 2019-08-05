package com.example.allergydiary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] notificationContent = intent.getStringArrayExtra("notificationContent");
        String title = notificationContent[0];
        String contentTitle = notificationContent[1];
        int id = intent.getIntExtra("id", 0);

        NotificationManager nm = context.getSystemService(NotificationManager.class);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = (id == 0) ? NotificationManager.IMPORTANCE_DEFAULT : NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("notification" + id,
//                    "Daily Notification",
//                    importance);
//            channel.setDescription("Daily Notification");
//            if (nm != null) {
//                Log.d(TAG, "onReceive: :///");
//                nm.createNotificationChannel(channel);
//            }
//        }

        String channelId = (id == 0) ? AllergyDiary.DEFAULT_CHANNEL_ID : AllergyDiary.HIGH_CHANNEL_ID;
        int priority = (id == 0) ? NotificationCompat.PRIORITY_DEFAULT : NotificationCompat.PRIORITY_HIGH;

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, channelId);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(contentTitle)
                .setContentTitle(title)
                .setContentText(contentTitle)
                .setPriority(priority)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentInfo("INFO");

        if (id == 0) {
            Intent notificationIntent = new Intent(context, MainActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingI = PendingIntent.getActivity(context, id,
                    notificationIntent, 0);

            b.setContentIntent(pendingI);
        }

        if (nm != null) {
            nm.notify(id, b.build());
        }
    }
}