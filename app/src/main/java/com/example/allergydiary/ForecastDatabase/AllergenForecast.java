package com.example.allergydiary.ForecastDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "allergy_forecast")
public class AllergenForecast {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    private int region;

    @NotNull
    private String name;

    private int month;

    private int decade;

    private int intensity;

    public AllergenForecast(int region, @NotNull String name, int month, int decade, int intensity) {
        this.region = region;
        this.name = name;
        this.month = month;
        this.decade = decade;
        this.intensity = intensity;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getRegion() {
        return region;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getMonth() {
        return month;
    }

    public int getDecade() {
        return decade;
    }

    public int getIntensity() {
        return intensity;
    }
}
