package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/14.
 */

public class CustomColumnWeekContentView extends ScrollView {

    private int childHeight;
    private int scrollLocation;

    private Calendar calendar;
    private int curHour;

    private CustomColumnWeekHourView hourView;

    public CustomColumnWeekContentView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomColumnWeekContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomColumnWeekContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        calendar = Calendar.getInstance();
        curHour = calendar.get(Calendar.HOUR_OF_DAY);

        hourView = new CustomColumnWeekHourView(context);
        ScrollView.LayoutParams hourViewLayoutParams = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hourView.setLayoutParams(hourViewLayoutParams);
        this.addView(hourView);

        setInitViewOverListener();
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        hourView.setCalendar(calendar);
        hourView.invalidate();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View child = getChildAt(0);
        childHeight = child.getMeasuredHeight();
        scrollLocation = childHeight / 24 * curHour;
        MyLog.i(Common.TAG, "childHeight" + childHeight);
    }

    private void setInitViewOverListener(){

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                scrollToAppointedLocation();
            }
        });
    }

    private void scrollToAppointedLocation(){
        this.post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(0, scrollLocation - (childHeight / 48));
            }
        });
    }
}
