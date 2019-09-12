package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.allergydiary.R;
import com.example.allergydiary.TimeHelper;

import java.util.Calendar;

public class InlineCalendarPickerWidget extends PickerWidget {
    private TextView tvDate;
    private Calendar calendar = Calendar.getInstance();

    public InlineCalendarPickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
        initInterface();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    protected void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        tvDate = findViewById(R.id.display_date);
    }

    @Override
    protected void assignClickHandlers() {
        super.assignClickHandlers();
        btnNext.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void arrowVisibility() {
        boolean condition = (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
        btnNext.setVisibility(condition ? View.INVISIBLE : View.VISIBLE);
    }

    public void updatePicker(int addToPicker) {
        calendar.add(Calendar.MONTH, addToPicker);
        tvDate.setText(TimeHelper.calendarToString(calendar));
    }

    protected void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.inline_calendar, this);

        assignUiElements();
        assignClickHandlers();

        updatePicker(0);
    }

}