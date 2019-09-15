package com.example.allergydiary.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.allergydiary.R;

abstract public class PickerWidget extends LinearLayout {
    protected LinearLayout layout;
    protected Button btnPrev;
    protected Button btnNext;
    protected MyOnClickListener myOnClickListener;

    protected Animation inLeft;
    protected Animation inRight;
    protected Animation outLeft;
    protected Animation outRight;

    public PickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);

        inLeft = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        inRight = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        outLeft = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
        outRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
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
