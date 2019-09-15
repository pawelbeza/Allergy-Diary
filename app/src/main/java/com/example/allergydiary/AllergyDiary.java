package com.example.allergydiary;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class AllergyDiary extends Application {
    public static final String HIGH_CHANNEL_ID = "HighImpChannel";
    public static final String DEFAULT_CHANNEL_ID = "DefaultImpChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel highChannel = new NotificationChannel(
                    HIGH_CHANNEL_ID,
                    "High Importance Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            highChannel.setDescription("High Importance Daily Notification");

            NotificationChannel defaultChannel = new NotificationChannel(
                    DEFAULT_CHANNEL_ID,
                    "Default Importance Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            defaultChannel.setDescription("Default Importance Daily Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(highChannel);
                manager.createNotificationChannel(defaultChannel);
            }
        }
    }
}
