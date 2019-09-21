package com.example.allergydiary.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.allergydiary.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnboardingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(() -> {
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    R.anim.fast_fade_in, R.anim.fast_fade_out).replace(R.id.main_container,
                    new MainFragment()).commit();
        });
    }

    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        String[] titleText = {getResources().getString(R.string.diary), getResources().getString(R.string.statistics),
                getResources().getString(R.string.forecast)};
        String[] descriptionText = {getResources().getString(R.string.diaryDescription),
                getResources().getString(R.string.statisticsDescription),
                getResources().getString(R.string.forecastDescription)};
        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage(titleText[0], descriptionText[0],
                Color.parseColor("#69AEED"), R.drawable.ic_planning, R.drawable.ic_round_diary_24px);
        PaperOnboardingPage scr2 = new PaperOnboardingPage(titleText[1], descriptionText[1],
                Color.parseColor("#68CDCD"), R.drawable.ic_scrapbook, R.drawable.ic_round_bar_chart_24px);
        PaperOnboardingPage scr3 = new PaperOnboardingPage(titleText[2], descriptionText[2],
                Color.parseColor("#66E6B2"), R.drawable.ic_sunset, R.drawable.ic_round_cloud_queue_24px);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;
    }
}
