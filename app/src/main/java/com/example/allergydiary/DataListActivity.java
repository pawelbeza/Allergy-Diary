package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DataListActivity extends Fragment {
    private MyCursorAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.datalist_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());
        listView = view.findViewById(R.id.listView1);
        mCursor = db.getDataBaseContents();

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new MyCursorAdapter(
                        getActivity(),
                        mCursor);

                listView.setAdapter(customAdapter);
            }

        });
    }

}
