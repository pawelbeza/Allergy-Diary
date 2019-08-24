package com.example.allergydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;


import static android.view.View.getDefaultSize;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ForecastFragment extends Fragment {
    private RegionPicker regionPicker;
    private AllergenForecastViewModel forecastViewModel;
    private RecyclerView recyclerView;
    private List<AllergenForecast> database = new ArrayList<>();

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

        List<AllergenForecast> list = forecastViewModel.getDataBaseContents(1, 1, 1);
        for (int i = 0; i < list.size(); i++) {
            AllergenForecast allergenForecast = list.get(i);
            Log.d(TAG, "onViewCreated: " + allergenForecast.getRegion() + " " + allergenForecast.getRegion() + " " +
                    allergenForecast.getName() + " " + allergenForecast.getMonth() + " " + allergenForecast.getDecade() + " " +
                    allergenForecast.getIntensity());

        }

        regionPicker = getActivity().findViewById(R.id.region);

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 2));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 0));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 3));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 1));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 2));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 3));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 2));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 2));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 0));
        database.add(new AllergenForecast(1, "Leszczyna", 1, 1, 3));

        VegaLayoutManager layoutManager = new VegaLayoutManager();;
        recyclerView.setLayoutManager(new VegaLayoutManager());
        ForecastAdapter forecastAdapter = new ForecastAdapter(getActivity(), database);
        recyclerView.setAdapter(forecastAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        forecastAdapter.notifyDataSetChanged();


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
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int region = sharedPref.getInt("RegionIndex", 0);
        regionPicker.setIndex(region);
    }
}
