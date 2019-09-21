package com.example.allergydiary.Widgets;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.StayConsumer;
import com.billy.android.swipe.listener.SimpleSwipeListener;
import com.example.allergydiary.R;
import com.example.allergydiary.TimeHelper;

import java.util.Calendar;

public class InlineCalendarPickerWidget extends PickerWidget {
    private TextSwitcher textSwitcher;
    private Calendar calendar = Calendar.getInstance();
    private Context context;

    public InlineCalendarPickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initInterface();
        initControl(context);
        SmartSwipe.wrap(layout)
                .addConsumer(new StayConsumer())
                .enableAllDirections()
                .addListener(new SimpleSwipeListener() {

                    @Override
                    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        if (direction == 1) {
                            updatePicker(-1);
                        } else if (direction == 2 && !isCurrentDate()) {
                            updatePicker(1);
                        }
                    }
                });
    }

    public Calendar getCalendar() {
        return calendar;
    }

    protected void assignUiElements(Context context) {
        layout = findViewById(R.id.layout);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        textSwitcher = findViewById(R.id.display_date);
        textSwitcher.setFactory(() -> {
            TextView tv = new TextView(context);
            if (Build.VERSION.SDK_INT >= 23)
                tv.setTextAppearance(R.style.InlineCalendarTheme);
            else
                tv.setTextAppearance(context, R.style.InlineCalendarTheme);
            return tv;
        });
    }

    @Override
    protected void assignClickHandlers() {
        super.assignClickHandlers();
        btnNext.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void arrowVisibility() {
        boolean condition = isCurrentDate();
        btnNext.setVisibility(condition ? View.INVISIBLE : View.VISIBLE);
    }

    private boolean isCurrentDate() {
        return (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
    }

    public void updatePicker(int addToPicker) {
        if(addToPicker == 1) {
            textSwitcher.setInAnimation(inLeft);
            textSwitcher.setOutAnimation(outRight);
        }
        else {
            textSwitcher.setInAnimation(inRight);
            textSwitcher.setOutAnimation(outLeft);
        }
        calendar.add(Calendar.MONTH, addToPicker);
        textSwitcher.setText(TimeHelper.calendarToString(context, calendar));
        super.updatePicker(addToPicker);
    }

    protected void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.inline_calendar, this);

        assignUiElements(context);
        assignClickHandlers();

        updatePicker(0);
    }

}