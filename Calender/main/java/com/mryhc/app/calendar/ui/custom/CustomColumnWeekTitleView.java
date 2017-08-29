package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/13.
 */

public class CustomColumnWeekTitleView extends View {
    private int viewWidth;
    private int viewHeight;

    private int weekItemWidth;
    private int leftPartWidth;

    private float textSize;

    private Paint rectPaint;
    private Paint textPaint;
    private Paint linePaint;

    private Rect textRect;

    private int titleRectColor;
    private int normalTextColor;
    private int otherTextColor;
    private int lineColor;

    private Context context;

    private Calendar dayCalendar;

    private int todayYear;
    private int todayMonth;
    private int today;

    private TitleCell[] titleCells;

    public CustomColumnWeekTitleView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomColumnWeekTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, null, 0);
    }

    public CustomColumnWeekTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, null, 0);
    }

    private void initialize(Context context,AttributeSet attrs, int defStyleAttr){
        this.context = context;
        rectPaint = new Paint();
        textPaint = new Paint();
        linePaint = new Paint();

        rectPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);

        textRect = new Rect();

        titleRectColor = Color.parseColor("#F5F5F5");
        normalTextColor = Color.parseColor("#BDBDBD");
        otherTextColor = Color.parseColor("#00B0FF");
        lineColor = Color.parseColor("#999999");

        linePaint.setColor(lineColor);

        dayCalendar = Calendar.getInstance();
        todayYear = dayCalendar.get(Calendar.YEAR);
        todayMonth = dayCalendar.get(Calendar.MONTH);
        today = dayCalendar.get(Calendar.DAY_OF_MONTH);

        titleCells = new TitleCell[7];
        updateViewData();
    }

    private void updateViewData(){
        for(int i = 0; i<7; i++){
            titleCells[i] = new TitleCell(dayCalendar.get(Calendar.DAY_OF_WEEK), dayCalendar.get(Calendar.YEAR),
                    dayCalendar.get(Calendar.MONTH), dayCalendar.get(Calendar.DAY_OF_MONTH), i);
            dayCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public void setDayCalendar(Calendar calendar){
        this.dayCalendar = calendar;
        updateViewData();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);

        int tempLeft = viewWidth / 10;

        weekItemWidth = (viewWidth - tempLeft) / 7;

        leftPartWidth = viewWidth - (weekItemWidth * 7);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                viewHeight = viewWidth / 16;
                break;
        }

        textSize = viewHeight / 2.0f;
        textPaint.setTextSize(textSize);

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectPaint.setColor(titleRectColor);
        rectPaint.setColor(titleRectColor);
        canvas.drawRect(0, 0, viewWidth, viewHeight, rectPaint);

        measureText("周");
        textPaint.setColor(otherTextColor);
        canvas.drawText("周", leftPartWidth / 2.0f - (textRect.width() / 2.0f),
                viewHeight / 2.0f + (textRect.height() / 2.0f), textPaint);

        for(int i=0; i< 7; i++){
            canvas.drawLine(leftPartWidth + weekItemWidth * i, 0, leftPartWidth + weekItemWidth * i, viewHeight, linePaint);
            titleCells[i].drawSelf(canvas);
        }
        canvas.drawLine(0, viewHeight-1, viewWidth, viewHeight-1, linePaint);
    }

    private class TitleCell{
        public String weekNumStr;
        public int weekNum;
        public int year;
        public int month;
        public int day;
        public int pos;
        public boolean isToady;

        public TitleCell(int weekNum, int year, int month, int day, int pos){
            this.weekNum = weekNum;
            this.year = year;
            this.month = month;
            this.day = day;
            this.pos = pos;
            if(year == todayYear && month == todayMonth && day == today){
                isToady = true;
            }
            switch (weekNum){
                case 1:
                    weekNumStr = context.getString(R.string.week_01);
                    break;
                case 2:
                    weekNumStr = context.getString(R.string.week_02);
                    break;
                case 3:
                    weekNumStr = context.getString(R.string.week_03);
                    break;
                case 4:
                    weekNumStr = context.getString(R.string.week_04);
                    break;
                case 5:
                    weekNumStr = context.getString(R.string.week_05);
                    break;
                case 6:
                    weekNumStr = context.getString(R.string.week_06);
                    break;
                case 7:
                    weekNumStr = context.getString(R.string.week_07);
                    break;
            }
        }

        public void drawSelf(Canvas canvas){
            measureText(String.valueOf(day));
            if (weekNum == 1 || weekNum == 7) {
                textPaint.setColor(otherTextColor);
            } else {
                textPaint.setColor(normalTextColor);
            }

            if(isToady){
                rectPaint.setColor(Color.RED);
                canvas.drawCircle(leftPartWidth + weekItemWidth * (pos + 1) - viewHeight / 2.0f, viewHeight / 2.0f, viewHeight * 3 / 8.0f, rectPaint);
            }

            canvas.drawText(weekNumStr, leftPartWidth + weekItemWidth * pos + viewHeight / 2.0f - (textRect.width() / 2.0f),
                    viewHeight / 2.0f + (textRect.height() / 2.0f), textPaint);

            if(isToady){
                textPaint.setColor(Color.WHITE);
            }

            canvas.drawText(String.valueOf(day), leftPartWidth + weekItemWidth * (pos + 1) - viewHeight / 2.0f - (textRect.width() / 2.0f),
                    viewHeight / 2.0f + (textRect.height() / 2.0f), textPaint);
        }
    }

    private void measureText(String str){
        textPaint.getTextBounds(str, 0, str.length(), textRect);
    }


}
