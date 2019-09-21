package com.example.allergydiary.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptom;
import com.example.allergydiary.AllergyDiaryDatabase.AllergicSymptomViewModel;
import com.example.allergydiary.R;
import com.ramotion.fluidslider.FluidSlider;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import kotlin.Unit;

public class DiaryFragment extends Fragment {
    private long date;
    private int fluidProgress;
    private FluidSlider slider;
    private Switch simpleSwitch;
    private CalendarView calendarView;
    private final int cornerRadius = 40;
    private AllergicSymptomViewModel symptomViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        symptomViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AllergicSymptomViewModel.class);

        calendarView = view.findViewById(R.id.calendarView);

        calendarView();

        simpleSwitch = view.findViewById(R.id.Switch);
        slider = getActivity().findViewById(R.id.fluidSlider);
        getCurrDate();

        int colorFrom = ContextCompat.getColor(getActivity(), R.color.bright_red);
        int colorTo = ContextCompat.getColor(getActivity(), R.color.bright_green);
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);

        colorAnimation.addUpdateListener(animation -> {
            View view1 = getActivity().findViewById(R.id.switchLayout);
            GradientDrawable shape = new GradientDrawable();
            shape.setColor((int) animation.getAnimatedValue());
            shape.setCornerRadius(cornerRadius);
            view1.setBackground(shape);
        });

        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) colorAnimation.start();
            else colorAnimation.reverse();
            addData();
        });

        final int max = 10;
        slider.setEndText(String.valueOf(max));
        slider.setPositionListener(pos -> {
            fluidProgress = (int)(max * pos);
            slider.setBubbleText(String.valueOf(fluidProgress));
            return Unit.INSTANCE;
        });

        slider.setEndTrackingListener(() -> {
            addData();
            return Unit.INSTANCE;
        });

        symptomViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AllergicSymptomViewModel.class);
    }

    private void getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
        date = cal.getTimeInMillis();
        setSavedValues();
    }

    private void setSwitchBackground(boolean b) {
        simpleSwitch.setChecked(b);
        simpleSwitch.jumpDrawablesToCurrentState();

        View view = Objects.requireNonNull(getActivity()).findViewById(R.id.switchLayout);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(cornerRadius);
        int color = b ? ContextCompat.getColor(getActivity(), R.color.bright_green) :
                ContextCompat.getColor(getActivity(), R.color.bright_red);
        shape.setColor(color);
        view.setBackground(shape);
    }

    private void setSavedValues() {
        AllergicSymptom liveData = symptomViewModel.getDataBaseContents(date);
        if (liveData == null) {//then there is no record with current date
            setSwitchBackground(false);
            slider.setPosition(0);
            slider.setBubbleText("0");
            return;
        }
        int feeling = liveData.getFeeling();

        slider.setPosition((float)(feeling/10.0));
        slider.setBubbleText(String.valueOf(feeling));

        boolean medicine = liveData.isMedicine();
        setSwitchBackground(medicine);
    }

    private void calendarView() {
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
            date = cal.getTimeInMillis();
            setSavedValues();
        });
    }

    private void addData() {
        int[] seekBarValues = new int[2];
        seekBarValues[0] = fluidProgress;
        seekBarValues[1] = simpleSwitch.isChecked() ? 1 : 0;

        AllergicSymptom symptom = new AllergicSymptom(date, seekBarValues[0], simpleSwitch.isChecked());
        symptomViewModel.upsert(symptom);
    }
}