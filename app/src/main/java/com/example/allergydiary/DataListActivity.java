package com.example.allergydiary;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class DataListActivity extends AppCompatActivity {
    private MyCursorAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalist_layout);

        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView1);
        mCursor = db.getDataBaseContents();

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new MyCursorAdapter(
                        DataListActivity.this,
                        mCursor);

                listView.setAdapter(customAdapter);
            }

        });
    }
}
