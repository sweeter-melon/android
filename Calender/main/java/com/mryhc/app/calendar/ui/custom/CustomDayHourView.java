package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.entity.DayViewTimeQuantum;
import com.mryhc.app.calendar.entity.EventEntity;
import com.mryhc.app.calendar.entity.HourMinute;
import com.mryhc.app.calendar.entity.MyDate;
import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/7/15.
 */

public class CustomDayHourView extends View {

    private int viewWidth;
    private int viewHeight;
    private int hourItemHeight;
    private int leftPartWidth;

    private Paint linePaint;
    private Paint rectPaint;
    private Paint textPaint;

    private Paint eventBgPaint;
    private TextPaint eventTextPaint;

    private Rect textRect;

    private int lineColor;
    private int leftPartColor;
    private int hourTextColor;
    private int eventColor;

    private Context context;

    private Calendar calendar;
    private int curHour;
    private int curMinute;
    private boolean isToday;

    private int eventTextSize;

    private LongClickRunnable longClickRunnable;

    private List<EventEntity> eventEntityList;
    private List<DayViewTimeQuantum> timeQuantumList;

    public CustomDayHourView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomDayHourView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomDayHourView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr){

        timeQuantumList = new ArrayList<>();

        longClickRunnable = new LongClickRunnable();

        this.context = context;
        linePaint = new Paint();
        rectPaint = new Paint();
        textPaint = new Paint();

        eventBgPaint = new Paint();
        eventTextPaint = new TextPaint();

        textPaint.setAntiAlias(true);
        eventBgPaint.setAntiAlias(true);
        eventTextPaint.setAntiAlias(true);

        eventBgPaint.setFilterBitmap(true);

        textRect = new Rect();

        lineColor = Color.parseColor("#999999");
        leftPartColor = Color.parseColor("#F5F5F5");
        hourTextColor = Color.parseColor("#00B0FF");
        eventColor = Color.parseColor("#FFFFFF");


        rectPaint.setColor(leftPartColor);
        eventTextPaint.setColor(eventColor);

        calendar = Calendar.getInstance();
        curHour = calendar.get(Calendar.HOUR_OF_DAY);
        curMinute = calendar.get(Calendar.MINUTE);
        isToday = true;

    }

    public void setCalendar(Calendar calendar, boolean isToday){
        this.calendar = calendar;
        this.isToday = isToday;
        invalidate();
    }

    public void setEventEntityList(List<EventEntity> eventEntityList){
        this.eventEntityList = eventEntityList;

        timeQuantumList.clear();

        timeQuantumList.add(new DayViewTimeQuantum(viewWidth - leftPartWidth));

        boolean added;
        for(EventEntity eventEntity: eventEntityList){
            added = false;
            // 遍历每一个时间段，查看时间的开始时间是否属于该时间段，如果属于，则将该时间添加到时间段中（时间段会自动修改结束时间）
            for(int i=0; i<timeQuantumList.size(); i++){
                DayViewTimeQuantum timeQuantum = timeQuantumList.get(i);
                if(eventEntity.getStartTime() > timeQuantum.getStartTime() && (timeQuantum.getEndTime() == 0 || eventEntity.getStartTime() < timeQuantum.getEndTime())){
                    timeQuantum.addEventEntity(eventEntity);
                    added = true;
                }
            }
            // 如果该事件不属于已经存在的任何一个时间段，则新增时间段，并将事件添加到该时间段中
            if(!added){
                DayViewTimeQuantum timeQuantum = new DayViewTimeQuantum(viewWidth - leftPartWidth);
                timeQuantum.addEventEntity(eventEntity);
                timeQuantumList.add(timeQuantum);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        hourItemHeight = viewWidth / 5;
        viewHeight = hourItemHeight * 24;

        leftPartWidth = viewWidth / 10;

        int textSize = leftPartWidth / 4;

        textPaint.setTextSize(textSize);
        textPaint.setColor(hourTextColor);

        eventTextSize = leftPartWidth / 3;

        eventTextPaint.setTextSize(eventTextSize);

        for(DayViewTimeQuantum timeQuantum:timeQuantumList){
            timeQuantum.setWholeWidth(viewWidth - leftPartWidth);
        }

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, leftPartWidth, viewHeight, rectPaint);

        linePaint.setColor(lineColor);
        canvas.drawLine(leftPartWidth, 0, leftPartWidth, viewHeight, linePaint);

        for(int i=0; i <= 24; i++){
            drawHourText(canvas, i);
            canvas.drawLine(0, hourItemHeight * i, viewWidth, hourItemHeight * i, linePaint);
        }
        drawEventList(canvas);
        if(isToday){
            linePaint.setColor(Color.RED);
            float linePos = getCurTimeLinePos(curHour, curMinute);
            canvas.drawCircle(leftPartWidth, linePos, 5, linePaint);
            canvas.drawLine(leftPartWidth, linePos, viewWidth, linePos, linePaint);
        }
    }

    private void drawEventList(Canvas canvas){
        if(eventEntityList != null && eventEntityList.size() > 0){
            for(DayViewTimeQuantum timeQuantum : timeQuantumList){
                List<EventEntity> tempList = timeQuantum.getEventEntityList();
                for(int i=0; i< tempList.size(); i++){
                    drawEvent(canvas, tempList.get(i), i, timeQuantum.getViewWidth());
                }
//                for(EventEntity eventEntity : timeQuantum.getEventEntityList()){
//                    drawEvent(canvas, eventEntity, timeQuantum.getViewWidth());
//                }
            }
//            for(EventEntity eventEntity:eventEntityList){
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.event_bg_01);
//                NinePatch np = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
//                Rect eventRect = new Rect(leftPartWidth + 2, 20, leftPartWidth + 202, 160);
//                np.draw(canvas, eventRect);
//
//                String str = eventEntity.getDetail() + "黄阿斯蒂芬哈师大回复";
//
//                int strLength = (160 / eventTextSize) * (100 / eventTextSize);
//                StaticLayout layout1 = new StaticLayout(str, 0, strLength,eventTextPaint, 160, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
//                canvas.translate(leftPartWidth + 22,40);
//                layout1.draw(canvas);
//                drawEvent(canvas, eventEntity, 300);
//            }
        }
    }

    private void drawEvent(Canvas canvas, EventEntity eventEntity, int index, int eventViewWidth){
        int startWidth = leftPartWidth + 10 + eventViewWidth * index;
        int startHeight = getEventHeight(eventEntity.getStartDate());
        int endHeight = getEventHeight(eventEntity.getEndDate());

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.event_bg_01);
        NinePatch np = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
        Rect eventRect = new Rect(startWidth, startHeight, startWidth + eventViewWidth - 3, endHeight);
        np.draw(canvas, eventRect);

        String str = eventEntity.getDetail();

        int canShowLength = getCanShowStrLength(eventViewWidth, endHeight - startHeight);

        StaticLayout layout1 = new StaticLayout(str, 0, canShowLength > str.length() ? str.length() : canShowLength,
                eventTextPaint, eventViewWidth - 30, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        canvas.save();
        canvas.translate(startWidth + 15,startHeight + 15);
        layout1.draw(canvas);
        canvas.restore();//把当前画布返回（调整）到上一个save()状态之前
    }

    private int getCanShowStrLength(int eventViewWidth, int eventViewHeight ){
        //                     一行多长                              可以显示的行数
        return (eventViewWidth - 33 / eventTextSize) * (eventViewHeight - 30 / eventTextSize);
    }

    private int getEventHeight(MyDate myDate){
        return (int)(myDate.getHour() * hourItemHeight + (myDate.getMinute() / 60.0f) * hourItemHeight);
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
            Toast.makeText(context, "长按单元格：" + y / hourItemHeight + "时", Toast.LENGTH_SHORT).show();
        }
    }

    private void postCheckForLongTouch(float x, float y) {

        longClickRunnable.setClickPos(x, y);

        postDelayed(longClickRunnable, 500);
    }

}
