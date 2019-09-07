package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.allergydiary.R;

public class RegionPickerWidget extends PickerWidget {
    final int[] imageResources = {R.drawable.ic_contour1, R.drawable.ic_contour2,
            R.drawable.ic_contour3, R.drawable.ic_contour4};
    private ImageView imageView;
    private int index = 0;

    public RegionPickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
        initInterface();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        updatePicker(0);
    }

    protected void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        imageView = findViewById(R.id.display_region);
    }

    public void updatePicker(int addToPicker) {
        index = (index + addToPicker + imageResources.length) % imageResources.length;
        imageView.setImageResource(imageResources[index]);
    }

    protected void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.region_picker, this);

        assignUiElements();
        assignClickHandlers();

        updatePicker(index);
    }

}