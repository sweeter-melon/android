package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by mryhc on 2017/7/27.
 */

public class MyRecyclerView extends RecyclerView {

    private boolean canScroll;

    private GestureDetector mGestureDetector;


    public MyRecyclerView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle){
        mGestureDetector = new GestureDetector(getContext(), new YScrollDetector());
        canScroll = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            canScroll = true;
        }
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if(canScroll){
                if(Math.abs(distanceY)>= Math.abs(distanceX)){
                    canScroll = true;
                }else{
                    canScroll = false;
                }
            }
            return canScroll;
        }

    }
}
