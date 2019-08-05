package com.example.allergydiary;

public class Notifications {
    private static String[] not1 = {"Daily questionnaire", "Time for filling daily questionnaire."};
    private static String[] not2 = {"Take your medicine", "Time for taking your medicine."};
    private static String[][] notificationContents = {not1, not2, not2};

    public static String[] getNotificationContents(int index) {
        return notificationContents[index];
    }
}
