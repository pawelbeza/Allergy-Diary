package com.example.allergydiary.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.allergydiary.Fragments.ChartsFragment;
import com.example.allergydiary.Fragments.DiaryFragment;
import com.example.allergydiary.Fragments.ForecastFragment;
import com.example.allergydiary.Fragments.SettingsFragment;
import com.example.allergydiary.R;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;

public class MainActivity extends AppCompatActivity {
    private static int lastLaunchedFragment;
    private DrawerLayout drawer;

    //TODO Change PDF layout
    //TODO Read about Onboarding https://material.io/design/communication/onboarding.html#
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
                if (view.getId() == lastLaunchedFragment)
                    return true;
                switch (view.getId()) {
                    case R.id.toDiary:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_out_right, R.anim.slide_in_right).replace(R.id.fragment_container,
                                new DiaryFragment()).addToBackStack("Diary").commit();
                        break;
                    case R.id.toForecast:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_out_right, R.anim.slide_in_right).replace(R.id.fragment_container,
                                new ForecastFragment()).addToBackStack("Forecast").commit();
                        break;
                    case R.id.toCharts:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_out_right, R.anim.slide_in_right).replace(R.id.fragment_container,
                                new ChartsFragment()).addToBackStack("Charts").commit();
                        break;
                    case R.id.toSettings:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                                R.anim.slide_out_right, R.anim.slide_in_right).replace(R.id.fragment_container,
                                new SettingsFragment()).addToBackStack("Settings").commit();
                        break;
                }
                lastLaunchedFragment = view.getId();
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
            lastLaunchedFragment = R.id.toDiary;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen((GravityCompat.START)))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
