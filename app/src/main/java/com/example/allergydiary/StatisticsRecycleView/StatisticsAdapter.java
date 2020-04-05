package com.example.allergydiary.StatisticsRecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.R;

public class StatisticsAdapter extends RecyclerView.Adapter {
    private String[] names;
    private double[] results;

    public StatisticsAdapter(Context context, double[] results) {
        this.results = results;
        this.names = context.getResources().getStringArray(R.array.statistic_names);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item, parent, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StatisticsViewHolder viewHolder = (StatisticsViewHolder) holder;
        viewHolder.bindData(names[position], results[position]);
    }

    @Override
    public int getItemCount() {
        return results.length;
    }

    public void swapDataSet(double[] results) {
        this.results = results.clone();
        notifyDataSetChanged();
    }
}