package com.example.allergydiary.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.allergydiary.Notifications.DeviceBootReceiver;
import com.example.allergydiary.Notifications.Notifications;
import com.example.allergydiary.R;

import java.util.Locale;

public class SettingsFragment extends Fragment {
    private final int cornerRadius = 40;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void assignClickHandler(int tvID, int swID, int toBeColored) {
        final TextView textView = requireActivity().findViewById(tvID);
        final Switch sw = requireActivity().findViewById(swID);
        textView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
                textView.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                sw.setChecked(true);
            }, 0, 0, DateFormat.is24HourFormat(getActivity()));
            timePickerDialog.show();
        });

        assignSwitchOnClickListener(requireActivity().findViewById(swID), requireActivity().findViewById(toBeColored));
    }

    private void assignSwitchOnClickListener(Switch simpleSwitch, final View view) {
        int colorFrom = ContextCompat.getColor(requireActivity(), R.color.bright_red);
        int colorTo = ContextCompat.getColor(requireActivity(), R.color.bright_green);
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);

        colorAnimation.addUpdateListener(animation -> {
            GradientDrawable shape = new GradientDrawable();
            shape.setColor((int) animation.getAnimatedValue());
            shape.setCornerRadius(cornerRadius);
            view.setBackground(shape);
        });

        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)   colorAnimation.start();
            else    colorAnimation.reverse();
        });
    }

    private void setSwitchBackground(boolean b, int toBeColoredID, int switchID) {
        Switch simpleSwitch = requireActivity().findViewById(switchID);
        simpleSwitch.setChecked(b);
        simpleSwitch.jumpDrawablesToCurrentState();

        View view = requireActivity().findViewById(toBeColoredID);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(cornerRadius);
        int color = b ? ContextCompat.getColor(requireActivity(), R.color.bright_green) :
                ContextCompat.getColor(requireActivity(), R.color.bright_red);
        shape.setColor(color);
        view.setBackground(shape);
    }

    @Override
    public void onPause() {
        super.onPause();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};

        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < switchIDs.length; i++) {
            Switch sw = requireActivity().findViewById(switchIDs[i]);
            editor.putBoolean("PopUpScheduleChecked" + i, sw.isChecked());

            TextView tv = requireActivity().findViewById(tvIDs[i]);
            editor.putString("PopUpSchedule" + i, tv.getText().toString());

            String[] notificationContent = (i == 0) ? requireActivity().getResources().getStringArray(
                    R.array.questionnaireNotification) :
                    requireActivity().getResources().getStringArray(R.array.medicineNotification);

            Notifications.setAlarm(getActivity(), sw.isChecked(), tv.getText().toString(), i, notificationContent);
        }

        setDeviceBootReceiver();

        editor.apply();
    }

    private void setDeviceBootReceiver() {
        PackageManager pm = requireActivity().getPackageManager();
        ComponentName receiver = new ComponentName(requireActivity(), DeviceBootReceiver.class);

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};

        for (int id : switchIDs) {
            Switch sw = requireActivity().findViewById(id);
            if (sw.isChecked()) {
                //To enable Boot Receiver class
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                return;
            }
        }

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onResume() {
        super.onResume();

        int[] switchIDs = {R.id.switch1, R.id.switch2, R.id.switch3};
        int[] toBeColoredIDs = {R.id.switch1Layout, R.id.switch2Layout, R.id.switch3Layout};
        int[] tvIDs = {R.id.everyDay, R.id.morning, R.id.evening};
        String[] time = {"20:00", "8:00", "20:00"};

        SharedPreferences sharedPref;
        try {
            sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            for (int i = 0; i < switchIDs.length; i++)
                setSwitchBackground(false, toBeColoredIDs[i], switchIDs[i]);
            return;
        }

        for (int i = 0; i < 3; i++) {
            boolean isChecked = sharedPref.getBoolean("PopUpScheduleChecked" + i, false);
            View view = requireActivity().findViewById(toBeColoredIDs[i]);

            if (isChecked)
                setSwitchBackground(true, toBeColoredIDs[i], switchIDs[i]);
            else
                setSwitchBackground(false, toBeColoredIDs[i], switchIDs[i]);

            String hour = sharedPref.getString("PopUpSchedule" + i, time[i]);
            TextView tv = view.findViewById(tvIDs[i]);
            tv.setText(hour);
        }
        assignClickHandler(R.id.everyDay, R.id.switch1, R.id.switch1Layout);
        assignClickHandler(R.id.morning, R.id.switch2, R.id.switch2Layout);
        assignClickHandler(R.id.evening, R.id.switch3, R.id.switch3Layout);
    }
}
