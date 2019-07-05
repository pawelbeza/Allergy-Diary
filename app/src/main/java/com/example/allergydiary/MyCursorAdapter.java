package com.example.allergydiary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MyCursorAdapter extends CursorAdapter {
    private static final String TAG = "MyCursorAdapter";
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
        long date = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLS[0]));
        String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
        TextView tv1 = view.findViewById(R.id.tv1);
        tv1.setText(formattedDate);

        TextView tv2 = view.findViewById(R.id.tv2);
        tv2.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLS[1])));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.cursorlist_layout, parent, false);
    }
}
