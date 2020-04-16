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
    private static DrawerLayout drawer;

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isSameFragment(Fragment fragment, int id) {
        return id == R.id.toDiary && fragment instanceof DiaryFragment
                || id == R.id.toForecast && fragment instanceof ForecastFragment
                || id == R.id.toStatistics && fragment instanceof StatisticsFragment
                || id == R.id.toSettings && fragment instanceof SettingsFragment
                || id == R.id.toAbout && fragment instanceof AboutFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer = requireActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SideBar leftSideBar = requireActivity().findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view, int index) {
                return false;
            }

            @Override
            public boolean onSelect(View view, int index) {
                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (isSameFragment(fragment, view.getId())) return false;
                switch (view.getId()) {
                    case R.id.toDiary:
                        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new DiaryFragment()).addToBackStack("Diary").commit();
                        break;
                    case R.id.toForecast:
                        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new ForecastFragment()).addToBackStack("Forecast").commit();
                        break;
                    case R.id.toStatistics:
                        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new StatisticsFragment()).addToBackStack("Charts").commit();
                        break;
                    case R.id.toSettings:
                        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new SettingsFragment()).addToBackStack("Settings").commit();
                        break;
                    case R.id.toAbout:
                        requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right)
                                .replace(R.id.fragment_container,
                                        new AboutFragment()).addToBackStack("About").commit();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            @Override
            public void onCancel() {

            }
        });

        if (savedInstanceState == null) {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).commitAllowingStateLoss();
        }
    }

    public static boolean onBackPressed() {
        if(drawer.isDrawerOpen((GravityCompat.START))) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else {
            return false;
        }
    }
}
