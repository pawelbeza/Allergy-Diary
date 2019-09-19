package com.example.allergydiary.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.PickerAdapter;
import com.example.allergydiary.R;
import com.stone.vega.library.VegaLayoutManager;

public class ChangeAllergensFragment extends Fragment {
    public static String prefName = "pickedAllergens";
    private String[] names;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_allergen_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        names = getResources().getStringArray(R.array.allergen_picker);

        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new VegaLayoutManager());
        PickerAdapter pickerAdapter = new PickerAdapter(getActivity());
        recyclerView.setAdapter(pickerAdapter);

        Button btn = getActivity().findViewById(R.id.saveBtn);
        btn.setOnClickListener(view1 -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(prefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            boolean[] isChecked = pickerAdapter.getIsChecked();
            for (int i = 0; i < isChecked.length; i++)
                editor.putBoolean(names[i], isChecked[i]);

            editor.apply();
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                    R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.fragment_container,
                    new ForecastFragment()).addToBackStack("Forecast").commit();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
