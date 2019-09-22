package com.example.allergydiary.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.allergydiary.Notifications.Notifications;
import com.example.allergydiary.R;
import com.jaredrummler.android.widget.AnimatedSvgView;

import java.util.Objects;

public class SplashScreenFragment extends Fragment {
    private String prefName = "onboarding";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AnimatedSvgView svgView = Objects.requireNonNull(getActivity()).findViewById(R.id.animated_svg_view);
        svgView.start();

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            boolean showedOnboarding = sharedPreferences.getBoolean(prefName, false);

            if (showedOnboarding) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new MainFragment()).commit();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(prefName, true);

                //setting default reminder to fill questionnaire
                String defaultReminder = "20:00";
                String[] notificationContent = getActivity().getResources().getStringArray(
                        R.array.questionnaireNotification);

                editor.putBoolean("PopUpScheduleChecked0", true);
                editor.putString("PopUpSchedule0", defaultReminder);

                Notifications.setAlarm(getActivity(), true, defaultReminder, 0, notificationContent);

                editor.apply();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new OnboardingFragment()).commit();
            }
        }, 1200);
    }

}
