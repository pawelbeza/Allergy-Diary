package com.example.allergydiary;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingsFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        assignClickHandler(view, R.id.everyDay, R.id.switch1);
        assignClickHandler(view, R.id.morning, R.id.switch2);
        assignClickHandler(view, R.id.evening, R.id.switch3);
    }

    public void assignClickHandler(View view, int tvID, int swID) {
        final TextView textView = view.findViewById(tvID);
        final Switch sw = view.findViewById(swID);
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
    }

    @Override
    public void onPause() {
        super.onPause();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < switchIDs.length; i++) {
            Switch sw = view.findViewById(switchIDs[i]);
            editor.putBoolean("PopUpScheduleChecked" + i, sw.isChecked());

            TextView tv = view.findViewById(tvIDs[i]);
            editor.putString("PopUpSchedule" + i, tv.getText().toString());
        }

        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < 3; i++) {
            Boolean isChecked = sharedPref.getBoolean("PopUpScheduleChecked" + i, false);
            Switch sw = view.findViewById(switchIDs[i]);
            sw.setChecked(isChecked);

            String hour = sharedPref.getString("PopUpSchedule" + i, "12:00");
            TextView tv = view.findViewById(tvIDs[i]);
            tv.setText(hour);
        }
    }
}
