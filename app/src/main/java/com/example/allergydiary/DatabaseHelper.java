package com.example.allergydiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "allergic_symptoms.db";
    private static final String TABLE_NAME = "allergic_symptoms";
    static final String[] COLS = {"DATE", "FEELING", "MEDICINE"};
    private static final String TAG = "DatabaseHelper";


    //TODO Take care of closing database

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer createTable = new StringBuffer("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DATE TEXT UNIQUE");
        for (int i = 1; i < COLS.length; i++) {
            Log.d(TAG, "onCreate: " + COLS[i]);
            createTable.append(", " + COLS[i] + " INT");
        }
        createTable.append(")");
        db.execSQL(createTable.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    boolean addData(String date, int[] seekBarValues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLS[0], date);
        for (int i = 1; i < COLS.length; i++)
            contentValues.put(COLS[i], seekBarValues[i - 1]);

        long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    Cursor getDataBaseContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    Cursor getDataBaseContents(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE DATE = '" + Date + "'";
        return db.rawQuery(query, null);
    }

    Cursor getDataBaseContents(String fromDate, String toDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE DATE BETWEEN '" + fromDate + "' AND '" + toDate +"'";
        return db.rawQuery(query, null);
    }
}