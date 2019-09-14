package com.example.allergydiary.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.DatabaseCopier;
import com.example.allergydiary.ForecastDatabase.AllergenForecast;
import com.example.allergydiary.ForecastDatabase.AllergenForecastViewModel;
import com.example.allergydiary.ForecastRecycleView.ForecastAdapter;
import com.example.allergydiary.R;
import com.example.allergydiary.Widgets.RegionPickerWidget;
import com.stone.vega.library.VegaLayoutManager;

import java.util.Calendar;
import java.util.List;

public class ForecastFragment extends Fragment {
    private RegionPickerWidget regionPicker;
    private AllergenForecastViewModel forecastViewModel;
    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseCopier.getInstance(getActivity());
        forecastViewModel = ViewModelProviders.of(getActivity()).get(AllergenForecastViewModel.class);

        regionPicker = getActivity().findViewById(R.id.region);
        regionPicker.setListener(this::updateForecast);

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
    }

    private void updateForecast() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);

        int decade;
        if (dayOfWeek <= 10)
            decade = 1;
        else if (dayOfWeek <= 20)
            decade = 2;
        else //cannot simplify to /= 10 because 31 is also in 3rd decade
            decade = 3;

        int region = regionPicker.getIndex() + 1;

        List<AllergenForecast> database = forecastViewModel.getDataBaseContents(region, month, decade);

        if (forecastAdapter == null) {
            recyclerView.setLayoutManager(new VegaLayoutManager());
            forecastAdapter = new ForecastAdapter(getActivity(), database);
            recyclerView.setAdapter(forecastAdapter);
        } else {
            forecastAdapter.swapDataSet(database);
            forecastAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int region = regionPicker.getIndex();
        editor.putInt("RegionIndex", region);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        //nullifying to ensure that setting adapter is made when user clicks back button (without that
        //no adapter attached error occurs
        forecastAdapter = null;
        updateForecast();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int region = sharedPref.getInt("RegionIndex", 0);
        regionPicker.setIndex(region);
    }
}
