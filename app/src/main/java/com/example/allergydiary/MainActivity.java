package com.example.allergydiary;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    //TODO Read about navController
    //TODO Read about Onboarding https://material.io/design/communication/onboarding.html#
    //TODO Read about Cards https://material.io/design/components/cards.html#usage
    //TODO Divide java files into widgets and ...
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
                switch (view.getId()) {
                    case R.id.toDiary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new DiaryFragment()).commit();
                        break;
                    case R.id.toForecast:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ForecastFragment()).commit();
                        break;
                    case R.id.toCharts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ChartsFragment()).commit();
                        break;
                    case R.id.toSettings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingsFragment()).commit();
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
