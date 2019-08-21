package com.example.allergydiary;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AllergenForecastDao {
    @Query("SELECT * FROM allergy_forecast")
    List<AllergenForecast> getDataBaseContents();

    @Query("SELECT * FROM allergy_forecast WHERE REGION = :region AND MONTH = :month AND DECADE = :decade")
    List<AllergenForecast> getDataBaseContents(int region, int month, int decade);
}
