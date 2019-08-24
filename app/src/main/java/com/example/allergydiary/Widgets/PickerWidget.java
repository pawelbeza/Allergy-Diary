package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

abstract public class PickerWidget extends LinearLayout {
    protected Button btnPrev;
    protected Button btnNext;
    protected MyOnClickListener myOnClickListener;

    public PickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    abstract protected void assignUiElements();

    protected void initInterface() {
        myOnClickListener = new MyOnClickListener() {
            @Override
            public void onClickListener() {
            }
        };
    }

    protected void assignClickHandlers() {
        assignOnClickListener(btnPrev, -1);
        assignOnClickListener(btnNext, 1);
    }

    protected void assignOnClickListener(final Button btn, final int addToPicker) {
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicker(addToPicker);
                myOnClickListener.onClickListener();
                arrowVisibility();
            }
        });
    }

    protected void arrowVisibility(){};

    abstract protected void initControl(Context context);

    abstract public void updatePicker(int addToPicker);

    public interface MyOnClickListener {
        void onClickListener();
    }
}
