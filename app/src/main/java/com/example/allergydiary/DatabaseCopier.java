package com.example.allergydiary;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME_ENG = "allergy_forecast_eng.db";
    private static final String DATABASE_NAME_PL = "allergy_forecast_pl.db";
    private static final String POLISH_CODE = "pl_PL";
    public static final String DATABASE_NAME = Resources.getSystem().getConfiguration().locale.toString()
            .equals(POLISH_CODE) ? DATABASE_NAME_PL : DATABASE_NAME_ENG;

    private static Context appContext;

    private DatabaseCopier() {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(appContext);
    }

    public static DatabaseCopier getInstance(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }

    private void copyAttachedDatabase(Context context) {
        final File dbPath = context.getDatabasePath(DatabaseCopier.DATABASE_NAME);

        // If the database already exists and we have a path to the file, return
        if (dbPath.exists() && !Objects.requireNonNull(dbPath.getParentFile()).mkdirs())
            return;

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + DatabaseCopier.DATABASE_NAME);
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
