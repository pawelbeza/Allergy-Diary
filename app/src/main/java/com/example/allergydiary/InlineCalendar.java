package com.example.allergydiary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InlineCalendar extends LinearLayout {
    private Button btnPrev;
    private Button btnNext;
    private TextView tvDate;
    private Calendar calendar = Calendar.getInstance();
    private MyOnClickListener myOnClickListener;
    private static final String TAG = "InlineCalendar";

    public InlineCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
        initInterface();
    }

    public void setListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        tvDate = findViewById(R.id.display_date);
    }

    private void initInterface() {
        myOnClickListener = new MyOnClickListener() {
            @Override
            public void onClickListener() {
            }
        };
    }

    private void assignClickHandlers() {
        assignOnClickListener(btnPrev, -1);
        assignOnClickListener(btnNext, 1);
        btnNext.setVisibility(View.INVISIBLE);
    }

    private void assignOnClickListener(final Button btn, final int addMonth) {
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCalendar(addMonth);
                myOnClickListener.onClickListener();
                boolean condition = (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                        Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR));
                btnNext.setVisibility(condition ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.inline_calendar, this);

        assignUiElements();
        assignClickHandlers();

        updateCalendar(0);
    }

    public void updateCalendar(int addMonth) {
        calendar.add(Calendar.MONTH, addMonth);
        tvDate.setText(calendarToString());
    }

    private String calendarToString() {
        SimpleDateFormat date_format = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        return date_format.format(calendar.getTime());
    }

    public interface MyOnClickListener {
        void onClickListener();
    }
}