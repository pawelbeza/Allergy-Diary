package com.example.allergydiary.AboutRecycleView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.R;

class AboutViewHolder extends RecyclerView.ViewHolder {
    private ImageView image;
    private TextView attribution;
    private Context context;

    AboutViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.image = itemView.findViewById(R.id.icon);
        this.attribution = itemView.findViewById(R.id.attribution);
        this.context = context;
    }

    void bindData(int id, String author) {
        this.image.setImageResource(id);
        String madeBy = context.getResources().getString(R.string.madeBy, author);
        attribution.setText(madeBy);
    }
}
