package com.example.allergydiary.StatisticsRecycleView;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.R;

import java.text.DecimalFormat;

class StatisticsViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView result;

    StatisticsViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.name = itemView.findViewById(R.id.name);
        this.result = itemView.findViewById(R.id.result);
    }

    void bindData(String name, double result) {
        this.name.setText(name);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        String strResult = df.format(result);
        this.result.setText(strResult);
    }
}
