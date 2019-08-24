package com.example.allergydiary.AllergyDiaryDatabase;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "allergic_symptoms",
        indices = {
                @Index(value = "date", unique = true)
        })
public class AllergicSymptom {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long date;

    private int feeling;

    private boolean medicine;

    public AllergicSymptom(long date, int feeling, boolean medicine) {
        this.date = date;
        this.feeling = feeling;
        this.medicine = medicine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public int getFeeling() {
        return feeling;
    }

    public boolean isMedicine() {
        return medicine;
    }
}
