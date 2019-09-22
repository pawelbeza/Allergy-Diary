package com.example.allergydiary.AboutRecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.R;

public class AboutAdapter extends RecyclerView.Adapter {
    private int[] ids;
    private String[] authors;
    private Context context;

    public AboutAdapter(Context context, int[] ids, String[] authors) {
        this.ids = ids;
        this.authors = authors;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribution_item, parent, false);
        return new AboutViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AboutViewHolder viewHolder = (AboutViewHolder) holder;
        viewHolder.bindData(ids[position], authors[position]);
    }

    @Override
    public int getItemCount() {
        return ids.length;
    }
}
