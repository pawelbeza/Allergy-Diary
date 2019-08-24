package com.example.allergydiary.AllergyDiaryDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AllergicSymptomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(AllergicSymptom symptom);

    @Query("SELECT * FROM allergic_symptoms")
    List<AllergicSymptom> getDataBaseContents();

    @Query("SELECT * FROM allergic_symptoms WHERE DATE = :Date")
    AllergicSymptom getDataBaseContents(long Date);

    @Query("SELECT * FROM allergic_symptoms WHERE DATE BETWEEN :fromDate AND :toDate")
    List<AllergicSymptom> getDataBaseContents(long fromDate, long toDate);
}
