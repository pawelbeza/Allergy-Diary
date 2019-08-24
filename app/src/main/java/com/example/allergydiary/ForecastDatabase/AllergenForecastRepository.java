package com.example.allergydiary.ForecastDatabase;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AllergenForecastRepository {
    private AllergenForecastDao forecastDao;

    public AllergenForecastRepository(Application application) {
        AllergenForecastDatabase database = AllergenForecastDatabase.getInstance(application);
        forecastDao = database.allergenForecastDao();
    }

    public List<AllergenForecast> getDataBaseContents() {
        try {
            return new AllergenForecastRepository.getAllDataBaseContents(forecastDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public List<AllergenForecast> getDataBaseContents(int region, int month, int decade) {
        try {
            return new AllergenForecastRepository.getDataBaseContentsInRange(forecastDao, region, month, decade).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private class getAllDataBaseContents extends AsyncTask<Void, Void, List<AllergenForecast>> {
        private AllergenForecastDao forecastDao;

        private getAllDataBaseContents(AllergenForecastDao forecastDao) {
            this.forecastDao = forecastDao;
        }

        @Override
        protected List<AllergenForecast> doInBackground(Void... voids) {
            return forecastDao.getDataBaseContents();
        }
    }

    private class getDataBaseContentsInRange extends AsyncTask<Void, Void, List<AllergenForecast>> {
        private AllergenForecastDao forecastDao;
        private int region;
        private int month;
        private int decade;

        public getDataBaseContentsInRange(AllergenForecastDao forecastDao, int region, int month, int decade) {
            this.forecastDao = forecastDao;
            this.region = region;
            this.month = month;
            this.decade = decade;
        }

        @Override
        protected List<AllergenForecast> doInBackground(Void... voids) {
            return forecastDao.getDataBaseContents(region, month, decade);
        }
    }
}
