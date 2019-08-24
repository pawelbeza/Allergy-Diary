package com.example.allergydiary.AllergyDiaryDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class AllergicSymptomViewModel extends AndroidViewModel {
    private AllergicSymptomRepository repository;

    public AllergicSymptomViewModel(@NonNull Application application) {
        super(application);
        repository = new AllergicSymptomRepository(application);
    }

    public void upsert(AllergicSymptom symptom) {
        repository.upsert(symptom);
    }

    public List<AllergicSymptom> getDataBaseContents() {
        return repository.getDataBaseContents();
    }

    public AllergicSymptom getDataBaseContents(long Date) {
        return repository.getDataBaseContents(Date);
    }

    public List<AllergicSymptom> getDataBaseContents(long fromDate, long toDate) {
        return repository.getDataBaseContents(fromDate, toDate);
    }
}
