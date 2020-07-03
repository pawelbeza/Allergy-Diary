package com.example.allergydiary.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.allergydiary.Notifications.Notifications;
import com.example.allergydiary.R;
import com.jaredrummler.android.widget.AnimatedSvgView;

public class SplashScreenFragment extends Fragment {
    private String prefName = "onboarding";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AnimatedSvgView svgView = requireActivity().findViewById(R.id.animated_svg_view);
        svgView.start();

        new Handler().postDelayed(() -> {
            if (getActivity() == null)
                return;
            SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
            boolean showedOnboarding = sharedPreferences.getBoolean(prefName, false);

            if (showedOnboarding) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new MainFragment()).commitAllowingStateLoss();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(prefName, true);

                //setting default reminder to fill questionnaire
                String defaultReminder = "20:00";
                String[] notificationContent = requireActivity().getResources().getStringArray(
                        R.array.questionnaireNotification);

                editor.putBoolean("PopUpScheduleChecked0", true);
                editor.putString("PopUpSchedule0", defaultReminder);

                Notifications.setAlarm(getActivity(), true, defaultReminder, 0, notificationContent);

                editor.apply();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                        new OnboardingFragment()).commitAllowingStateLoss();
            }
        }, 1200);
    }

}
