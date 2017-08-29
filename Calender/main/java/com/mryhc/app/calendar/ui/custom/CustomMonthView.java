package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.Calendar;

/**
 * Created by Yunhao on 2017/7/12.
 */

public class CustomMonthView extends View{
    private static final int COL_NUM = 7;
    private static final int ROW_NUM = 6;

    private Paint textPaint;        // 画每一天的日期
    private Paint redCirclePaint;   // 今天的红色圆圈
    private Paint linePaint;        // 横纵线
    private Paint weekPartPaint;    // 周标题背景
    private Paint selectRectPaint;    // 周标题背景

    private int viewWidth;
    private int viewHeight;

    private int weekTitleViewHeight;    // 显示周几的位置高度
    private int weekNumViewWidth;       // 显示第几周的位置宽度

    private int cellWidth;              // 每一天所对应的的view的宽度
    private int cellHeight;             // 每一天所对应的的view的高度
    private int dayNumViewSize;         // 每一天所对应的的view的高度

    private int curMonthDayColor;
    private int otherMonthDayColor;
    private int lineColor;
    private int weekPartBackColor;
    private int selectRectColor;

    private int weekNumColor;

    private Rect textRect;

    private WeekTitleCell[] weekTitleCells;
    private WeekNumCell[] weekNumCells;
    private DayCell[] dayCells;

    private Calendar calendar;  // 当前月份的Calendar
    private Calendar curPageCalendar;

    private int todayYear;
    private int todayMonth;
    private int today;
    private int curMonth;

    private int weekFirstDayIn; // 本月1号所在的周

    private LongClickRunnable longClickRunnable;
    private Context context;

    private long touchDownTime;

    private OnDayCellClickListener dayCellClickListener;
    private OnDayCellLongClickListener dayCellClickLongListener;

    private SharedPreferences sharedPreferences;
    private boolean showWeekNum;
    private int weekStartNum;

    private int clickedPos;

    public CustomMonthView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        this.context = context;

        clickedPos = -1;

        sharedPreferences = context.getSharedPreferences(Common.SP_NAME, Context.MODE_PRIVATE);
        showWeekNum = sharedPreferences.getBoolean(Common.SHOW_WEEK_NUM, false);
        weekStartNum = sharedPreferences.getInt(Common.WEEK_START, 7);
        MyLog.i(Common.TAG, "每周起始日" + weekStartNum);

        longClickRunnable = new LongClickRunnable();

        calendar = Calendar.getInstance();
        curPageCalendar = Calendar.getInstance();
        todayYear = calendar.get(Calendar.YEAR);
        todayMonth = calendar.get(Calendar.MONTH);
        today = calendar.get(Calendar.DAY_OF_MONTH);

        curMonthDayColor = Color.BLACK;
        otherMonthDayColor = Color.parseColor("#BDBDBD");
        lineColor = Color.parseColor("#999999");
        weekNumColor = Color.parseColor("#00B0FF");
        weekPartBackColor = Color.parseColor("#F5F5F5");
        selectRectColor = Color.parseColor("#B3E5FC");

        textPaint = new Paint();
        redCirclePaint = new Paint();
        linePaint = new Paint();
        weekPartPaint = new Paint();
        selectRectPaint = new Paint();

        textPaint.setAntiAlias(true);
        redCirclePaint.setAntiAlias(true);
        weekPartPaint.setAntiAlias(true);
        selectRectPaint.setAntiAlias(true);

        redCirclePaint.setColor(Color.RED);
        linePaint.setColor(lineColor);
        weekPartPaint.setColor(weekPartBackColor);
        selectRectPaint.setColor(selectRectColor);

        textRect = new Rect();

        weekTitleCells = new WeekTitleCell[COL_NUM];
        weekNumCells = new WeekNumCell[ROW_NUM];
        dayCells = new DayCell[ROW_NUM * COL_NUM];

        updateViewData();
    }

    private void updateViewData(){

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        weekFirstDayIn = calendar.get(Calendar.WEEK_OF_YEAR);

        for(int i=0; i< COL_NUM; i++){      // 初始化周数
            weekTitleCells[i] = new WeekTitleCell(i);
            if(i < 6){
                weekNumCells[i] = new WeekNumCell(i);
            }
        }

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        curMonth = calendar.get(Calendar.MONTH);
        if(weekStartNum == 1){
            calendar.add(Calendar.DAY_OF_YEAR, -firstDayOfWeek+2);
        }else{
            calendar.add(Calendar.DAY_OF_YEAR, -firstDayOfWeek+1);
        }
        for(int i=0; i< COL_NUM * ROW_NUM; i++){
            dayCells[i] = new DayCell(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        int tempTitleHeight = h / 20;
        cellHeight = (h - tempTitleHeight) / ROW_NUM;
        int heightRemainder= (h - tempTitleHeight) % 6;
        weekTitleViewHeight = tempTitleHeight + heightRemainder;

        if(showWeekNum){
            int tempNumWidth = w / 20;
            cellWidth = (w - tempNumWidth) / COL_NUM ;
            int widthRemainder = (w - tempNumWidth) % 7;
            weekNumViewWidth = tempNumWidth + widthRemainder;
        }else{
            cellWidth = w / COL_NUM;
            weekNumViewWidth = 0;
        }

        dayNumViewSize = cellWidth / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                touchDownTime = event.getDownTime();
                postCheckForLongTouch(event.getX(), event.getY());
            }

            if(event.getAction() == MotionEvent.ACTION_UP){
                if(event.getEventTime() - touchDownTime < 100){
                    int row = ((int)event.getY() - weekTitleViewHeight) / cellHeight;
                    int col = ((int)event.getX() - weekNumViewWidth) / cellWidth;
                    int clickPos = row * COL_NUM + col;
                    if(clickPos == clickedPos){
                        DayCell dayCell = dayCells[clickPos];
                        Calendar targetCalendar = Calendar.getInstance();
                        targetCalendar.set(Calendar.YEAR, dayCell.year);
                        targetCalendar.set(Calendar.MONTH, dayCell.month);
                        targetCalendar.set(Calendar.DAY_OF_MONTH, dayCell.day);
                        if(dayCellClickListener != null){
                            dayCellClickListener.onDayCellClick(targetCalendar);
                        }
                    }else{
                        invalidate();
                    }

                    clickedPos = clickPos;
                }
            }
        }

        return true;
    }

    private void postCheckForLongTouch(float x, float y) {

        longClickRunnable.setClickPos(x, y);

        postDelayed(longClickRunnable, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制标题背景
        canvas.drawRect(0, 0, viewWidth, weekTitleViewHeight, weekPartPaint);
        canvas.drawRect(0, 0, weekNumViewWidth, viewHeight, weekPartPaint);

        // 绘制标题
        textPaint.setTextSize(dayNumViewSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        measureText("日");
        if(showWeekNum){
            drawText(canvas, "周", 0, -1);
        }

        // 绘制网格线
        canvas.drawLine(weekNumViewWidth, 0, weekNumViewWidth, viewHeight, linePaint);  // 竖线
        for(int i = 0; i < COL_NUM; i++){
            weekTitleCells[i].drawSelf(canvas);
        }

        for(int i=0; i< ROW_NUM * COL_NUM; i++){
            if( i < 6){
                canvas.drawLine(0, i * cellHeight + weekTitleViewHeight, viewWidth, i * cellHeight + weekTitleViewHeight, linePaint);      // 横线
                canvas.drawLine((i+1) * cellWidth + weekNumViewWidth, 0, (i+1) * cellWidth + weekNumViewWidth, viewHeight, linePaint);  // 竖线
                if(showWeekNum){
                    weekNumCells[i].drawSelf(canvas);
                }
            }
            dayCells[i].drawSelf(canvas);
        }
    }

    private void drawText(Canvas canvas, String text, int textType, int pos){
        if(textType != 1){
            measureText(text);
        }
        switch (textType){
            case 0: // 最左上角的文字
                textPaint.setColor(weekNumColor);
                canvas.drawText(text, weekNumViewWidth / 2 - (textRect.width() / 2), weekTitleViewHeight / 2 + (textRect.height() / 2), textPaint);
                break;
            case 1: // 星期几的文字
                if ("六".equals(text) || "日".equals(text)) {
                    textPaint.setColor(weekNumColor);
                } else {
                    textPaint.setColor(otherMonthDayColor);
                }
                canvas.drawText(text, weekNumViewWidth + cellWidth * (pos) + cellWidth / 2 - (textRect.width() / 2),
                        weekTitleViewHeight / 2 + (textRect.height() / 2), textPaint);
                break;
            case 2: // 第几周的文字
                textPaint.setColor(weekNumColor);
                canvas.drawText(text, weekNumViewWidth / 2 - (textRect.width() / 2),
                        weekTitleViewHeight + cellHeight * pos + weekNumViewWidth / 2 + (textRect.height() / 2), textPaint);
                break;
        }
    }

    private void measureText(String text){
        textPaint.getTextBounds(text, 0, text.length(), textRect);
    }

    private void drawDayNum(Canvas canvas, String text, int pos, boolean isToday, boolean isCurMonth){
        textPaint.setTextSize(dayNumViewSize *3 / 4);
        measureText(text);
        if(isToday){
            textPaint.setColor(Color.WHITE);
        }else if(isCurMonth){
            textPaint.setColor(curMonthDayColor);
        }else{
            textPaint.setColor(otherMonthDayColor);
        }
        canvas.drawText(text, weekNumViewWidth + cellWidth * (pos % COL_NUM) + dayNumViewSize / 2 - (textRect.width() / 2),
                weekTitleViewHeight + cellHeight * (pos / COL_NUM) + dayNumViewSize / 2 + (textRect.height() / 2), textPaint);
    }

    /**
     * 设置当前View的Calendar
     * @param calendar
     */
    public void setCalendar(Calendar calendar){
        clickedPos = -1;
        this.calendar = calendar;
        curPageCalendar.setTime(calendar.getTime());
        updateViewData();
        invalidate();
    }

    /**
     * 设置单元格点击监听
     * @param listener
     */
    public void setDayCellClickListener(OnDayCellClickListener listener){
        this.dayCellClickListener = listener;
    }

    /**
     * 设置单元格长按监听
     * @param longListener
     */
    public void setDayCellClickLongListener(OnDayCellLongClickListener longListener){
        this.dayCellClickLongListener = longListener;
    }

    class DayCell{
        public int year;
        public int month;
        public int day;
        public int pos;
        public boolean isToday;
        public boolean isCurMonth;

        public DayCell(int year, int month, int day, int pos){
            this.year = year;
            this.month = month;
            this.day = day;
            this.pos = pos;
            if(month == curMonth){
                isCurMonth = true;
            }
            if(year == todayYear && month == todayMonth && day == today){
                    isToday = true;
            }
        }

        public void drawSelf(Canvas canvas){
            if(!isCurMonth){
                int left = weekNumViewWidth + cellWidth * (pos % COL_NUM);
                int top = weekTitleViewHeight + cellHeight * (pos / COL_NUM);
                canvas.drawRect(left+1, top+1, left + cellWidth-1, top + cellHeight-1, weekPartPaint);
            }

            // 画选中的位置所在日期框
            if(clickedPos == pos){
                int left = weekNumViewWidth + cellWidth * (pos % COL_NUM);
                int top = weekTitleViewHeight + cellHeight * (pos / COL_NUM);
                canvas.drawRect(left+1, top+1, left + cellWidth-1, top + cellHeight-1, selectRectPaint);
            }

            if(isToday){
                canvas.drawCircle(weekNumViewWidth + cellWidth * (pos % COL_NUM) + dayNumViewSize / 2 + 1,
                        weekTitleViewHeight + cellHeight * (pos / COL_NUM) + dayNumViewSize / 2  + 1, dayNumViewSize / 2, redCirclePaint);
            }
            drawDayNum(canvas, String.valueOf(day), pos, isToday, isCurMonth);
        }
    }

    class WeekTitleCell{
        public String titleStr;
        public int pos;

        public WeekTitleCell(int pos){
            this.pos = pos;
            int caseValue = pos;
            if(weekStartNum == 1){ // 每周起始日为周一
                caseValue = (pos + 1) % 7;
            }
            switch (caseValue){
                case 0:
                    titleStr = "日";
                    break;
                case 1:
                    titleStr = "一";
                    break;
                case 2:
                    titleStr = "二";
                    break;
                case 3:
                    titleStr = "三";
                    break;
                case 4:
                    titleStr = "四";
                    break;
                case 5:
                    titleStr = "五";
                    break;
                case 6:
                    titleStr = "六";
                    break;
            }
        }

        public void drawSelf(Canvas canvas){
            drawText(canvas, titleStr, 1, pos);
        }
    }

    class WeekNumCell{
        public int pos;

        public WeekNumCell(int pos){
            this.pos = pos;
        }

        public void drawSelf(Canvas canvas){
            drawText(canvas, String.valueOf(weekFirstDayIn + pos), 2, pos);
        }
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
            int row = (y - weekTitleViewHeight) / cellHeight;
            int col = (x - weekNumViewWidth) / cellWidth;
            Toast.makeText(context, "长按单元格：" + dayCells[row * COL_NUM + col].day + "日", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnDayCellClickListener{
        void onDayCellClick(Calendar targetCalendar);
    }

    public interface OnDayCellLongClickListener{
        void onDayCellLongClick();
    }

    /**
     * 响应设置更改
     * 在设置更改后调用
     */
    public void responseSettingChanged(){
        showWeekNum = sharedPreferences.getBoolean(Common.SHOW_WEEK_NUM, false);
        weekStartNum = sharedPreferences.getInt(Common.WEEK_START, 7);
        calendar.setTime(curPageCalendar.getTime());


        if(showWeekNum){
            int tempNumWidth = viewWidth / 20;
            cellWidth = (viewWidth - tempNumWidth) / COL_NUM ;
            int widthRemainder = (viewWidth - tempNumWidth) % 7;
            weekNumViewWidth = tempNumWidth + widthRemainder;
        }else{
            cellWidth = viewWidth / COL_NUM;
            weekNumViewWidth = 0;
        }

        dayNumViewSize = cellWidth / 4;

        updateViewData();
    }
}
