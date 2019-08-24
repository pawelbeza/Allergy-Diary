package com.example.allergydiary.ForecastRecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.ForecastDatabase.AllergenForecast;
import com.example.allergydiary.ForecastRecycleView.ForecastViewHolder;
import com.example.allergydiary.R;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter {
    private List<AllergenForecast> dataSet;
    private Context context;

    public ForecastAdapter(Context context, List<AllergenForecast> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allergen_forecast_item, parent, false);
        return new ForecastViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ForecastViewHolder viewHolder = (ForecastViewHolder) holder;
        viewHolder.bindData(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void swapDataSet(List<AllergenForecast> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }
}
