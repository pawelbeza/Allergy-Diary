package com.example.allergydiary.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.allergydiary.AllergyDiary;
import com.example.allergydiary.Activities.MainActivity;
import com.example.allergydiary.R;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String[] notificationContent = intent.getStringArrayExtra("notificationContent");
        String title = "Reminder";
        String contentTitle = "Reminder";

        if (notificationContent != null) {
            title = notificationContent[0];
            contentTitle = notificationContent[1];
        }
        int id = intent.getIntExtra("id", 0);

        NotificationManager nm;
        if (android.os.Build.VERSION.SDK_INT >= 23)
            nm = context.getSystemService(NotificationManager.class);
        else
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


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