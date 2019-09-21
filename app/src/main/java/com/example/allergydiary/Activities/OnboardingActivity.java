package com.example.allergydiary.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.allergydiary.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();

        final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(() -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
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
