package com.example.allergydiary;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME = "allergy_forecast.db";

    private static Context appContext;

    private DatabaseCopier() {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(appContext, DATABASE_NAME);
    }

    public static DatabaseCopier getInstance(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }

    private void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {
            return;
        }

        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }

    private static class Holder {
        private static final DatabaseCopier INSTANCE = new DatabaseCopier();
    }

}
