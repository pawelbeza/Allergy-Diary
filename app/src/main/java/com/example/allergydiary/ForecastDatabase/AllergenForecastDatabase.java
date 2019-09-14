package com.example.allergydiary.ForecastDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = AllergenForecast.class, version = 2)
public abstract class AllergenForecastDatabase extends RoomDatabase {
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    private static final String DATABASE_NAME = "allergy_forecast.db";
    private static AllergenForecastDatabase instance;

    public static synchronized AllergenForecastDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AllergenForecastDatabase.class, DATABASE_NAME)
                    .addMigrations(AllergenForecastDatabase.MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract AllergenForecastDao allergenForecastDao();
}
