package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/13.
 */

public class CustomColumnWeekHourView extends View {
    private static final int HOUR_ITEM_SIZE = 24;
    private static final int DAY_ITEM_SIZE = 7;

    private int hourItemHeight;

    private int viewWidth;
    private int viewHeight;

    private int weekItemWidth;
    private int leftPartWidth;

    private Paint linePaint;
    private Paint rectPaint;
    private Paint textPaint;

    private Rect textRect;

    private int colorFirst;
    private int colorSecond;
    private int colorThird;
    private int lineColor;
    private int leftPartColor;
    private int hourTextColor;

    private int topOrBottomMargin;

    private LongClickRunnable longClickRunnable;

    private Context context;

    private Calendar calendar;
    private int curHour;
    private int curMinute;

    private DayItem[] dayItems;

    public CustomColumnWeekHourView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomColumnWeekHourView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomColumnWeekHourView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        this.context = context;
        linePaint = new Paint();
        rectPaint = new Paint();
        textPaint = new Paint();

        textRect = new Rect();

        colorFirst = Color.parseColor("#59dbe0");
        colorSecond = Color.parseColor("#f57f68");
        colorThird = Color.parseColor("#87d288");
        lineColor = Color.parseColor("#999999");
        leftPartColor = Color.parseColor("#F5F5F5");
        hourTextColor = Color.parseColor("#00B0FF");

        linePaint.setColor(lineColor);
        rectPaint.setColor(leftPartColor);
        textPaint.setColor(hourTextColor);

        textPaint.setAntiAlias(true);

        calendar = Calendar.getInstance();
        curHour = calendar.get(Calendar.HOUR_OF_DAY);
        curMinute = calendar.get(Calendar.MINUTE);

        longClickRunnable = new LongClickRunnable();

        dayItems = new DayItem[DAY_ITEM_SIZE];
        updateViewData();
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        updateViewData();
        invalidate();
    }

    private void updateViewData(){
        for(int i=0; i<DAY_ITEM_SIZE; i++){
            dayItems[i] = new DayItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        hourItemHeight = viewWidth / 5;

        int tempLeft = viewWidth / 10;
        weekItemWidth = (viewWidth - tempLeft) / 7;
        leftPartWidth = viewWidth - (weekItemWidth * 7);

        topOrBottomMargin = hourItemHeight / 4;
        viewHeight = hourItemHeight * 24;

        int textSize = leftPartWidth / 4;

        textPaint.setTextSize(textSize);

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, leftPartWidth, viewHeight, rectPaint);
        linePaint.setColor(lineColor);

        for(int i=0; i <= 24; i++){
            drawHourText(canvas, i);
            canvas.drawLine(0, hourItemHeight * (i + 1), viewWidth, hourItemHeight * (i + 1), linePaint);
            if(i<7){
                canvas.drawLine(leftPartWidth + weekItemWidth * i, 0, leftPartWidth + weekItemWidth * i, viewHeight, linePaint);
            }
        }

        linePaint.setColor(Color.RED);
        float linePos = getCurTimeLinePos(curHour, curMinute);
        canvas.drawCircle(leftPartWidth, linePos, 5, linePaint);
        canvas.drawLine(leftPartWidth, linePos, viewWidth, linePos, linePaint);
    }

    private float getCurTimeLinePos(int hour, int minute){
        return hourItemHeight * (minute / 60.0f + hour);
    }

    private void drawHourText(Canvas canvas, int hour){
        String str = AppUtils.getHourText(hour);
        measureText(str);
        canvas.drawText(str, 5, hourItemHeight * hour + textRect.height() + 5, textPaint);
    }

    private void measureText(String str){
        textPaint.getTextBounds(str, 0, str.length(), textRect);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (longClickRunnable != null) {
            //TODO 这里可以增加一些规则，比如：模糊XY的判定，使长按更容易触发
            if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
                if(longClickRunnable.getX() != -1 && longClickRunnable.getY() != -1){
                    if(Math.abs(event.getX() - longClickRunnable.getX()) < 40 && Math.abs(event.getY() - longClickRunnable.getY()) < 40){
                        MotionEvent eventClone = MotionEvent.obtain(event);
                        eventClone.setAction(MotionEvent.ACTION_CANCEL);
                        eventClone.offsetLocation(longClickRunnable.getX(), longClickRunnable.getY());
                        return super.onTouchEvent(event);
                    }
                }
            }

            removeCallbacks(longClickRunnable);

            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && event.getPointerCount() == 1) {
                postCheckForLongTouch(event.getX(), event.getY());
            }
        }

        return true;
    }

    private class LongClickRunnable implements Runnable{

        private int x = -1;
        private int y = -1;

        public void setClickPos(float x, float y){
            this.x = (int)x;
            this.y = (int)y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public void run() {
            int posX = (x - leftPartWidth) / weekItemWidth;
            int posY = y / hourItemHeight;
            Toast.makeText(context, "长按单元格：" + dayItems[posX].day + "日 " + dayItems[posX].hourItems[posY].hour + "时", Toast.LENGTH_SHORT).show();
        }
    }

    private void postCheckForLongTouch(float x, float y) {

        longClickRunnable.setClickPos(x, y);

        postDelayed(longClickRunnable, 500);
    }

    private class DayItem{
        public int year;
        public int month;
        public int day;
        public int horizonPos;

        public HourItem[] hourItems;

        public DayItem(int year, int month, int day, int horizonPos){
            this.year = year;
            this.month = month;
            this.day = day;
            this.horizonPos = horizonPos;
            hourItems = new HourItem[HOUR_ITEM_SIZE];
            initHourItem();
        }

        private void initHourItem(){
            for(int i=0; i< HOUR_ITEM_SIZE; i++){
                hourItems[i] = new HourItem(year, month, day, i);
            }
        }
    }

    private class HourItem{
        public int year;
        public int month;
        public int day;
        public int hour;

        public HourItem(int year, int month, int day, int hour){
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
        }
    }


}
