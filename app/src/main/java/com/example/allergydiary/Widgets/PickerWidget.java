package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

abstract public class PickerWidget extends LinearLayout {
    protected LinearLayout layout;
    protected Button btnPrev;
    protected Button btnNext;
    protected MyOnClickListener myOnClickListener;

    public PickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    abstract protected void assignUiElements(Context context);

    protected void initInterface() {
        myOnClickListener = () -> {
        };
    }

    protected void assignClickHandlers() {
        assignOnClickListener(btnPrev, -1);
        assignOnClickListener(btnNext, 1);
    }

    protected void assignOnClickListener(final Button btn, final int addToPicker) {
        btn.setOnClickListener(v -> updatePicker(addToPicker));
    }

    protected void arrowVisibility(){}

    abstract protected void initControl(Context context);

    public void updatePicker(int addToPicker) {
        myOnClickListener.onClickListener();
        arrowVisibility();
    }

    public interface MyOnClickListener {
        void onClickListener();
    }
}
