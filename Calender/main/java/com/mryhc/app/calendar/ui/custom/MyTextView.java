package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by mryhc on 2017/7/27.
 */

public class MyTextView extends AppCompatTextView {

    private boolean mScrolling;
    private float touchDownX;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        r
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mScrolling = false;
                break;

            case MotionEvent.ACTION_MOVE:
                mScrolling = false;
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
            default:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }
}
