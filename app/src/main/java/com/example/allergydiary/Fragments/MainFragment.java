package com.example.allergydiary.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.allergydiary.R;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;

import java.util.Objects;

public class MainFragment extends Fragment {
    private DrawerLayout drawer;

    @Override
    public void onResume() {
        super.onResume();
    }

    //TODO set daily notification by default
    //TODO closing nav drawer

    private boolean isSameFragment(Fragment fragment, int id) {
        return id == R.id.toDiary && fragment instanceof DiaryFragment
                || id == R.id.toForecast && fragment instanceof ForecastFragment
                || id == R.id.toStatistics && fragment instanceof StatisticsFragment
                || id == R.id.toSettings && fragment instanceof SettingsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SideBar leftSideBar = getActivity().findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view, int index) {
                return false;
            }

            @Override
            public boolean onSelect(View view, int index) {
                Fragment fragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (isSameFragment(fragment, view.getId())) return false;
                switch (view.getId()) {
                    case R.id.toDiary:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new DiaryFragment()).addToBackStack("Diary").commit();
                        break;
                    case R.id.toForecast:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new ForecastFragment()).addToBackStack("Forecast").commit();
                        break;
                    case R.id.toStatistics:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new StatisticsFragment()).addToBackStack("Charts").commit();
                        break;
                    case R.id.toSettings:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new SettingsFragment()).addToBackStack("Settings").commit();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            @Override
            public void onCancel() {

            }
        });

        if (savedInstanceState == null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).commit();
        }
    }

//    @Override
//    public void onBackPressed() {
//        if(drawer.isDrawerOpen((GravityCompat.START)))
//            drawer.closeDrawer(GravityCompat.START);
//        else
//            super.onBackPressed();
//    }
}
