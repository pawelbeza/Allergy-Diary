package com.example.allergydiary.AllergyDiaryDatabase;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AllergicSymptomRepository {
    private AllergicSymptomDao symptomDao;

    public AllergicSymptomRepository(Application application) {
        AllergicSymptomDatabase database = AllergicSymptomDatabase.getInstance(application);
        symptomDao = database.allergicSymptomDao();
    }

    public void upsert(AllergicSymptom symptom) {
        new UpsertSymptomAsyncTask(symptomDao).execute(symptom);
    }

    public List<AllergicSymptom> getDataBaseContents() {
        try {
            return new getAllDataBaseContents(symptomDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public AllergicSymptom getDataBaseContents(long Date) {
        try {
            return new getDataBaseContents(symptomDao, Date).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public List<AllergicSymptom> getDataBaseContents(long fromDate, long toDate) {
        try {
            return new getDataBaseContentsInRange(symptomDao, fromDate, toDate).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private static class UpsertSymptomAsyncTask extends AsyncTask<AllergicSymptom, Void, Void> {
        private AllergicSymptomDao symptomDao;

        private UpsertSymptomAsyncTask(AllergicSymptomDao symptomDao) {
            this.symptomDao = symptomDao;
        }

        @Override
        protected Void doInBackground(AllergicSymptom... allergicSymptoms) {
            symptomDao.upsert(allergicSymptoms[0]);
            return null;
        }
    }

    private class getDataBaseContents extends AsyncTask<Void, Void, AllergicSymptom> {
        private final long Date;
        private AllergicSymptomDao symptomDao;

        private getDataBaseContents(AllergicSymptomDao symptomDao, long Date) {
            this.symptomDao = symptomDao;
            this.Date = Date;
        }

        @Override
        protected AllergicSymptom doInBackground(Void... voids) {
            return symptomDao.getDataBaseContents(Date);
        }
    }

    private class getDataBaseContentsInRange extends AsyncTask<Void, Void, List<AllergicSymptom>> {
        private final long fromDate;
        private final long toDate;

        private AllergicSymptomDao symptomDao;

        private getDataBaseContentsInRange(AllergicSymptomDao symptomDao, long fromDate, long toDate) {
            this.symptomDao = symptomDao;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        @Override
        protected List<AllergicSymptom> doInBackground(Void... voids) {
            return symptomDao.getDataBaseContents(fromDate, toDate);
        }
    }

    private class getAllDataBaseContents extends AsyncTask<Void, Void, List<AllergicSymptom>> {
        private AllergicSymptomDao symptomDao;

        private getAllDataBaseContents(AllergicSymptomDao symptomDao) {
            this.symptomDao = symptomDao;
        }

        @Override
        protected List<AllergicSymptom> doInBackground(Void... voids) {
            return symptomDao.getDataBaseContents();
        }
    }
}