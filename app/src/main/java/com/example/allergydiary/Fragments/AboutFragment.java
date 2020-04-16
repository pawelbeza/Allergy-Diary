package com.example.allergydiary.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allergydiary.AboutRecycleView.AboutAdapter;
import com.example.allergydiary.R;
import com.stone.vega.library.VegaLayoutManager;

public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] authors = {"Freepik", "Maxim Basinski", "Maxim Basinski", "Freepik", "Smashicons",
                "Roundicons", "Smashicons", "Smashicons", "Eucalyp", "smalllikeart", "Smashicons",
                "Freepik"};
        int[] ids = {R.drawable.ic_launcher, R.drawable.ic_checked, R.drawable.ic_cancel,
                R.drawable.ic_flower_attr, R.drawable.ic_graph, R.drawable.ic_info, R.drawable.ic_notebook_4,
                R.drawable.ic_pill, R.drawable.ic_planning_attr, R.drawable.ic_scrapbook_attr, R.drawable.ic_settings_4,
                R.drawable.ic_sunset_attr};

        RecyclerView recyclerView = requireActivity().findViewById(R.id.recycler_credits);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new VegaLayoutManager());
        AboutAdapter pickerAdapter = new AboutAdapter(getActivity(), ids, authors);
        recyclerView.setAdapter(pickerAdapter);
    }
}
