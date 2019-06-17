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

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tview1 = view.findViewById(R.id.tview1);
        TextView tview2 = view.findViewById(R.id.tview2);

        String date = cursor.getString(cursor.getColumnIndex("DATE"));
        String feeling = cursor.getString(cursor.getColumnIndex("FEELING"));

        tview1.setText(date);
        tview2.setText(feeling);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.cursorlist_layout, parent, false);
    }
}
