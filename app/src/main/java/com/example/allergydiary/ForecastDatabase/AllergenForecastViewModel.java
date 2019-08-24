package com.example.allergydiary.ForecastDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.allergydiary.ForecastDatabase.AllergenForecast;
import com.example.allergydiary.ForecastDatabase.AllergenForecastRepository;

import java.util.List;

public class AllergenForecastViewModel extends AndroidViewModel {
    private AllergenForecastRepository repository;

    public AllergenForecastViewModel(@NonNull Application application) {
        super(application);
        repository = new AllergenForecastRepository(application);
    }

    public List<AllergenForecast> getDataBaseContents() {
        return repository.getDataBaseContents();
    }

    public List<AllergenForecast> getDataBaseContents(int region, int month, int decade) {
        return repository.getDataBaseContents(region, month, decade);
    }
}
