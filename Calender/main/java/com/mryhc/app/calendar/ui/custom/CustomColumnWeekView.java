package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/13.
 */

public class CustomColumnWeekView extends LinearLayout {
    private int viewWidth;
    private int titleHeight;
    private Paint rectPaint;

    private int titleRectColor;

    private Calendar calendar;

    private CustomColumnWeekTitleView titleView;

    private CustomColumnWeekContentView contentView;

    public CustomColumnWeekView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomColumnWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomColumnWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        this.setOrientation(VERTICAL);
        rectPaint = new Paint();
        titleRectColor = Color.parseColor("#F5F5F5");

//        calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 1);

        titleView = new CustomColumnWeekTitleView(context);
        LinearLayout.LayoutParams tvlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView.setLayoutParams(tvlp);
        this.addView(titleView);

        contentView = new CustomColumnWeekContentView(context);
        LinearLayout.LayoutParams cvlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        contentView.setLayoutParams(cvlp);
        this.addView(contentView);

//        titleView.setDayCalendar(calendar);
//        contentView.setCalendar(calendar);
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(calendar.getTime());
        titleView.setDayCalendar(tempCalendar);
        tempCalendar.setTime(calendar.getTime());
        contentView.setCalendar(calendar);
    }
}
