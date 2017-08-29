package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

/**
 * Created by mryhc on 2017/7/27.
 */

public class MyLinearLayout extends LinearLayout {

    private boolean mScrolling;
    private float touchDownX;

    private ViewDragHelper viewDragHelper;

    public MyLinearLayout(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return true;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                touchDownX = event.getX();
//                mScrolling = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(
//                        getContext()).getScaledTouchSlop()) {
//                    MyLog.i(Common.TAG, "Action move");
//                    mScrolling = true;
//                } else {
//                    mScrolling = false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                mScrolling = true;
//                break;
//        }
//        return mScrolling;
//        return viewDragHelper.shouldInterceptTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }
}
