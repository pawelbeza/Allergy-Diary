package com.example.allergydiary.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.allergydiary.Fragments.StatisticsFragment;
import com.example.allergydiary.Fragments.DiaryFragment;
import com.example.allergydiary.Fragments.ForecastFragment;
import com.example.allergydiary.Fragments.SettingsFragment;
import com.example.allergydiary.R;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onResume() {
        super.onResume();
    }

    //TODO Read about Onboarding https://material.io/design/communication/onboarding.html#
    //TODO Add language choice

    private boolean isSameFragment(Fragment fragment, int id) {
        return id == R.id.toDiary && fragment instanceof DiaryFragment
                || id == R.id.toForecast && fragment instanceof ForecastFragment
                || id == R.id.toStatistics && fragment instanceof StatisticsFragment
                || id == R.id.toSettings && fragment instanceof SettingsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SideBar leftSideBar = findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view, int index) {
                return false;
            }

            @Override
            public boolean onSelect(View view, int index) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (isSameFragment(fragment, view.getId())) return false;
                switch (view.getId()) {
                    case R.id.toDiary:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.fragment_container,
                                new DiaryFragment()).addToBackStack("Diary").commit();
                        break;
                    case R.id.toForecast:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.fragment_container,
                                new ForecastFragment()).addToBackStack("Forecast").commit();
                        break;
                    case R.id.toStatistics:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.fragment_container,
                                new StatisticsFragment()).addToBackStack("Charts").commit();
                        break;
                    case R.id.toSettings:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.fragment_container,
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen((GravityCompat.START)))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
