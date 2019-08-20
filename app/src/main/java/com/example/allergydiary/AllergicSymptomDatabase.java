package com.example.allergydiary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = AllergicSymptom.class, version = 1)
public abstract class AllergicSymptomDatabase extends RoomDatabase {

    private static AllergicSymptomDatabase instance;

    public static synchronized AllergicSymptomDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AllergicSymptomDatabase.class, "allergic_symptom_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract AllergicSymptomDao allergicSymptomDao();
}
