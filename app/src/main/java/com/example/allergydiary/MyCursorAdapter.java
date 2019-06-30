package com.example.allergydiary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    MyCursorAdapter(Context context, Cursor c) {
        this(context, c, 0);
    }

    private MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int[] arr = {R.id.tv1, R.id.tv2};
        for (int i = 0; i < arr.length; i++) {
            TextView tv = view.findViewById(arr[i]);
            tv.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLS[i])));
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.cursorlist_layout, parent, false);
    }
}
