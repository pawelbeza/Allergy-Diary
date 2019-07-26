package com.example.allergydiary;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void assignClickHandler(int tvID, int swID, int toBeColored) {
        final TextView textView = getActivity().findViewById(tvID);
        final Switch sw = getActivity().findViewById(swID);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView.setText(hourOfDay + ":" + minute);
                        sw.setChecked(true);
                    }
                }, 0, 0, DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
            }
        });

        assignSwitchOnClickListener((Switch) getActivity().findViewById(swID), getActivity().findViewById(toBeColored));
    }

    private void assignSwitchOnClickListener(Switch simpleSwitch, final View view) {
        int colorFrom = ContextCompat.getColor(getActivity(), R.color.bright_red);
        int colorTo = ContextCompat.getColor(getActivity(), R.color.bright_green);
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)   colorAnimation.start();
                else    colorAnimation.reverse();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < switchIDs.length; i++) {
            Switch sw = getActivity().findViewById(switchIDs[i]);
            editor.putBoolean("PopUpScheduleChecked" + i, sw.isChecked());

            TextView tv = getActivity().findViewById(tvIDs[i]);
            editor.putString("PopUpSchedule" + i, tv.getText().toString());
        }

        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] toBeColoredIDs = {R.id.switch1Layout, R.id.switch2Layout, R.id.switch3Layout};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPref;
        try {
            sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            getActivity().findViewById(R.id.switch1Layout).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bright_red));
            getActivity().findViewById(R.id.switch2Layout).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bright_red));
            getActivity().findViewById(R.id.switch3Layout).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bright_red));
            return;
        }

        for (int i = 0; i < 3; i++) {
            Boolean isChecked = sharedPref.getBoolean("PopUpScheduleChecked" + i, false);
            Switch sw = getActivity().findViewById(switchIDs[i]);
            View view = getActivity().findViewById(toBeColoredIDs[i]);
            sw.setChecked(isChecked);
            sw.jumpDrawablesToCurrentState();

            if (isChecked)
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bright_green));
            else
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bright_red));

            String hour = sharedPref.getString("PopUpSchedule" + i, "12:00");
            TextView tv = view.findViewById(tvIDs[i]);
            tv.setText(hour);
        }
        assignClickHandler(R.id.everyDay, R.id.switch1, R.id.switch1Layout);
        assignClickHandler(R.id.morning, R.id.switch2, R.id.switch2Layout);
        assignClickHandler(R.id.evening, R.id.switch3, R.id.switch3Layout);
    }
}
