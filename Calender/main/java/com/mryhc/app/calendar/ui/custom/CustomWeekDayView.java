package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.utils.AppUtils;

/**
 * Created by mryhc on 2017/7/9.
 */

public class CustomWeekDayView extends CardView {

    private String weekNum;
    private String weekDate;
    private int weekBarHeight;
    private int weekNumSize;
    private int weekDateSize;
    private int weekBarBackColor;
    private int weekNumBackColor;
    private int weekDateBackColor;
    private int textColor;
    private boolean firstDay;

    private Paint myPaint;
    private Rect numRect;
    private Rect dateRect;

    public CustomWeekDayView(Context context) {
        this(context, null);
    }

    public CustomWeekDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWeekDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomWeekDayView, defStyleAttr, 0);
        for(int i = 0; i< typedArray.getIndexCount(); i++){
            int attrIndex = typedArray.getIndex(i);
            switch (attrIndex){
                case R.styleable.CustomWeekDayView_week_num:
                    weekNum = typedArray.getString(attrIndex);
                    break;
                case R.styleable.CustomWeekDayView_week_date:
                    weekDate = typedArray.getString(attrIndex);
                    break;
                case R.styleable.CustomWeekDayView_week_bar_height:
                    weekBarHeight = typedArray.getDimensionPixelOffset(attrIndex, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomWeekDayView_week_num_text_size:
                    weekNumSize = typedArray.getDimensionPixelOffset(attrIndex, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomWeekDayView_week_date_text_size:
                    weekDateSize = typedArray.getDimensionPixelOffset(attrIndex, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomWeekDayView_week_bar_back_color:
                    weekBarBackColor = typedArray.getColor(attrIndex, Color.parseColor("#CFD9E7"));
                    break;
                case R.styleable.CustomWeekDayView_week_num_back_color:
                    weekNumBackColor = typedArray.getColor(attrIndex, Color.parseColor("#F2F2F2"));
                    break;
                case R.styleable.CustomWeekDayView_week_date_back_color:
                    weekDateBackColor = typedArray.getColor(attrIndex, Color.parseColor("#F2F2F2"));
                    break;
            }
        }
        typedArray.recycle();

        myPaint = new Paint();
        numRect = new Rect();
        dateRect = new Rect();
        myPaint.setAntiAlias(true);
        myPaint.setTextSize(weekNumSize);
        myPaint.getTextBounds(weekNum, 0, weekNum.length(), numRect);
        myPaint.setTextSize(weekDateSize);
        myPaint.getTextBounds(weekDate, 0, weekDate.length(), dateRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstDay){
            textColor = Color.WHITE;
            weekNumBackColor = Color.parseColor("#FF0000");
            weekDateBackColor = Color.parseColor("#00BA5D");
        }else{
            textColor = Color.parseColor("#555555");
            weekNumBackColor = Color.parseColor("#CFD9E7");
            weekDateBackColor = Color.parseColor("#CFD9E7");
        }
        myPaint.setColor(weekBarBackColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), weekBarHeight, myPaint);
        myPaint.setColor(weekNumBackColor);
        canvas.drawRect(0, 0, numRect.width() + 30, weekBarHeight, myPaint);
        myPaint.setColor(weekDateBackColor);
        canvas.drawRect(getMeasuredWidth() - dateRect.width() - 30, 0, getMeasuredWidth(), weekBarHeight, myPaint);
        myPaint.setColor(textColor);
        myPaint.setTextSize(weekNumSize);
        canvas.drawText(weekNum, 10, weekBarHeight / 2 + (numRect.height() / 2), myPaint);
        myPaint.setTextSize(weekDateSize);
        canvas.drawText(weekDate, getMeasuredWidth() - dateRect.width()-10, weekBarHeight / 2 + (dateRect.height() / 2), myPaint);
    }

    public void setWeekNum(String weekNum){
        this.weekNum = weekNum;
        myPaint.setTextSize(weekNumSize);
        myPaint.getTextBounds(weekNum, 0, weekNum.length(), numRect);
        invalidate();
    }

    public void setWeekDate(String weekDate){
        this.weekDate = weekDate;
        myPaint.setTextSize(weekDateSize);
        myPaint.getTextBounds(weekDate, 0, weekDate.length(), dateRect);
        invalidate();
    }

    public void setFirstDay(boolean firstDay) {
        this.firstDay = firstDay;
        invalidate();
    }
}
