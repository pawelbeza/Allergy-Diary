package com.example.allergydiary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCursorAdapter extends CursorAdapter {
    private static final String TAG = "MyCursorAdapter";
    private LayoutInflater cursorInflater;
    private ArrayList<TextView> arrayList;

    public MyCursorAdapter(Context context, Cursor c) {
        this(context, c, 0);
        arrayList = new ArrayList<TextView>();
    }

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        arrayList.add((TextView) view.findViewById(R.id.tv1));
//        arrayList.get(0).setText(cursor.getString(cursor.getColumnIndex("DATE")));

        int[] arr = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7};
        for (int i = 0; i < arr.length; i++) {
            TextView tv = view.findViewById(arr[i]);
            tv.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLS[i])));
        }

//        for(int i = 0; i < DatabaseHelper.COLS.length(); i++) {
//            arrayList.get(i) = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLS[i]));
//        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.cursorlist_layout, parent, false);
    }
}
