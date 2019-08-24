package com.example.allergydiary;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    ImageView[] imageViews = new ImageView[3];
    TextView tv;
    Context context;

    public ForecastViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        this.tv = itemView.findViewById(R.id.allergen_name);
        int[] ids = {R.id.flower1, R.id.flower2, R.id.flower3};
        for (int i = 0; i < imageViews.length; i++)
            imageViews[i] = itemView.findViewById(ids[i]);
    }

    public void bindData(AllergenForecast allergenForecast) {
        String name = allergenForecast.getName();
        int numToColor = allergenForecast.getIntensity();
        int color;
        switch (numToColor) {
            case 1:
                color = ContextCompat.getColor(context, R.color.green);
                break;
            case 2:
                color = ContextCompat.getColor(context, R.color.orange);
                break;
            case 3:
                color = ContextCompat.getColor(context, R.color.red);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.smoky_white);
                break;
        }
        
        for (int i = 0; i < numToColor; i++) {
            ImageView view = imageViews[i];
            view.setColorFilter(color);
        }

        color = ContextCompat.getColor(context, R.color.smoky_white);
        for (int i = numToColor; i < imageViews.length; i++) {
            ImageView view = imageViews[i];
            view.setColorFilter(color);
        }
    }
}
