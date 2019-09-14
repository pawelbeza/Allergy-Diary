package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.StayConsumer;
import com.billy.android.swipe.listener.SimpleSwipeListener;
import com.example.allergydiary.R;

public class RegionPickerWidget extends PickerWidget {
    final int[] imageResources = {R.drawable.ic_contour1, R.drawable.ic_contour2,
            R.drawable.ic_contour3, R.drawable.ic_contour4};
    private ImageSwitcher imageSwitcher;
    private int index = 0;

    public RegionPickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInterface();
        initControl(context);
        SmartSwipe.wrap(layout)
                .addConsumer(new StayConsumer())
                .enableAllDirections()
                .addListener(new SimpleSwipeListener() {
                    @Override
                    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        if (direction == 1)
                            updatePicker(-1);
                        else if (direction == 2)
                            updatePicker(1);
                    }
                });
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        updatePicker(0);
    }

    protected void assignUiElements(Context context) {
        // layout is inflated, assign local variables to components
        layout = findViewById(R.id.layout);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        imageSwitcher = findViewById(R.id.display_region);
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        });
    }

    public void updatePicker(int addToPicker) {
        super.updatePicker(addToPicker);
        index = (index + addToPicker + imageResources.length) % imageResources.length;
        imageSwitcher.setImageResource(imageResources[index]);
    }

    protected void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.region_picker, this);

        assignUiElements(context);
        assignClickHandlers();

        updatePicker(index);
    }

}
