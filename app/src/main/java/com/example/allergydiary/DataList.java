package com.example.allergydiary;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataList extends AppCompatActivity {
    private static final String TAG = "DataList";
    private MyCursorAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalist_layout);

        DatabaseHelper db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView1);
        mCursor = db.getDataBaseContents();
        mCursor.moveToNext();
        customAdapter = new MyCursorAdapter(
                DataList.this,
                mCursor,
                0);
        listView.setAdapter(customAdapter);
//        if(data.getCount() == 0){
//            Toast.makeText(DataList.this, "There is no data here", Toast.LENGTH_LONG).show();
//        }
//        else{
//            while(data.moveToNext())
//            {
//                MyCursorAdapter listAdapter = new MyCursorAdapter(this, data, 0);
//                lview1.setAdapter(listAdapter);
//            }
//        }
    }

//    new Handler().post(new Runnable() {
//
//        @Override
//        public void run() {
//            customAdapter = new MyCursorAdapter(
//                    MainActivity.this,
//                    mCursor,
//                    0);
//
//            listView.setAdapter(customAdapter);
//        }

  //  });
}
