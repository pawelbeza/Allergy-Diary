package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.ramotion.fluidslider.FluidSlider;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";
    private long date;
    private DatabaseHelper db;
//    private SeekBar seekBar;
    private int fluidProgress;
    private FluidSlider slider;
    private Switch simpleSwitch;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());

        calendarView = view.findViewById(R.id.calendarView);

        calendarView();

        simpleSwitch = view.findViewById(R.id.Switch);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addData();
            }
        });

        final int max = 10;

        slider = getActivity().findViewById(R.id.fluidSlider);

        slider.setEndText(String.valueOf(max));

        slider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float pos) {
                fluidProgress = (int)(max * pos);
                slider.setBubbleText(String.valueOf(fluidProgress));
                Log.d("D", "setPositionTrackingListener" + fluidProgress);
                return Unit.INSTANCE;
            }
        });

        slider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.d("D", "setBeginTrackingListener");
                addData();
                return Unit.INSTANCE;
            }
        });
//        Button btnToDataBase = view.findViewById(R.id.btnToDataBase);
//        btnToDataBase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new DataListActivity()).commit();
//            }
//        });

        getCurrDate();
    }

    private void getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
        date = cal.getTimeInMillis();
        setSavedValues();
    }

    private void setSavedValues() {
        Cursor cursor = db.getDataBaseContents(date);
        if (cursor == null || cursor.getCount() == 0) {//then there is no record with current date
            slider.setPosition(0);
            simpleSwitch.setChecked(false);
            return;
        }
        cursor.moveToNext();
        int feeling = cursor.getInt(cursor.getColumnIndex("FEELING"));

        slider.setPosition((float)(feeling/10.0));

        int medicine = cursor.getInt(cursor.getColumnIndex("MEDICINE"));
        simpleSwitch.setChecked(medicine == 1);
    }

    private void calendarView() {
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
                date = cal.getTimeInMillis();
                setSavedValues();
            }
        });
    }

    private void addData() {
        int[] seekBarValues = new int[2];
        seekBarValues[0] = fluidProgress;
        seekBarValues[1] = simpleSwitch.isChecked() ? 1 : 0;

        boolean insertData = db.addData(date, seekBarValues);
        if (insertData) {
            Log.d(TAG, "addData: " + "Insertion successful");
        } else {
            Log.d(TAG, "addData: " + "Insertion failure");
        }
    }
}