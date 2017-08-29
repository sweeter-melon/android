package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/10.
 */

public class CustomMonthInYearAnotherView extends View {

    private static final int COL_NUM = 7; // 7列
    private static final int ROW_NUM = 6; // 6行

    private Calendar calendar;  // 当前月份的Calendar

    private int year;           // 当前年份
    private int month;          // 当前月份

    private int todayYear;
    private int todayMonth;
    private int today;

    private int viewWidth;      // 整个view的宽度
    private int titleHeight;
    private int cellWidth;      // 单元格宽
    private int cellHeight;     // 单元格高

    private Paint titlePaint;
    private Paint titleLinePaint;
    private Paint busyFlagPaint;
    private Paint redCirclePaint;
    private Paint textPaint;
    private Rect titleRect;
    private String titleStr;

    private RowElement[] rowElements ;

    public CustomMonthInYearAnotherView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomMonthInYearAnotherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomMonthInYearAnotherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        titlePaint = new Paint();
        titleLinePaint = new Paint();
        busyFlagPaint = new Paint();
        redCirclePaint = new Paint();
        textPaint = new Paint();
        titlePaint.setColor(Color.parseColor("#517693"));
        titleLinePaint.setColor(Color.parseColor("#517693"));
        redCirclePaint.setColor(Color.RED);
        titlePaint.setAntiAlias(true);
        busyFlagPaint.setAntiAlias(true);
        redCirclePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        rowElements = new RowElement[ROW_NUM];
        calendar = Calendar.getInstance();
        todayYear = calendar.get(Calendar.YEAR);
        todayMonth = calendar.get(Calendar.MONTH);
        today = calendar.get(Calendar.DAY_OF_MONTH);
        titleRect = new Rect();
        year = 2017;
        month = 6;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fillView();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fillView();
        invalidate();
    }

    private void fillView(){
        titleStr = (month + 1) +"月";
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.get(Calendar.)
        int dayCountInMonth ;
        switch (month+1){
            case 4:
            case 6:
            case 9:
            case 11:
                dayCountInMonth = 30;
                break;
            case 2:
                if(year % 4 == 0){
                    dayCountInMonth = 29;
                }else{
                    dayCountInMonth = 28;
                }
                break;
            default:
                dayCountInMonth = 31;
                break;
        }
        int pos = -1;
        int dayNum = -1;
        for (int i = 0; i < ROW_NUM; i++) {
            rowElements[i] = new RowElement(i);
            for (int j = 0; j < COL_NUM; j++) {
                pos = i * COL_NUM + j + 1;
                if (pos < dayOfWeek || pos >= (dayOfWeek + dayCountInMonth)) {
                    rowElements[i].cells[j] = new DayCell(i, j, -1, 0, true, false);
                } else {
                    dayNum = pos - dayOfWeek + 1;
                    rowElements[i].cells[j] = new DayCell(i, j, dayNum, 0,
                            false, (year == todayYear && month == todayMonth && dayNum == today));
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        titlePaint.getTextBounds(titleStr, 0, titleStr.length(), titleRect);
        canvas.drawText(titleStr, cellWidth / 3, titleHeight / 2 + (titleRect.height() / 2), titlePaint);
        canvas.drawLine(cellWidth / 4, titleHeight, viewWidth - (cellWidth / 4), titleHeight, titleLinePaint);
        for (int i = 0; i < ROW_NUM; i++) {
            if (rowElements[i] != null) {
                rowElements[i].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        titleHeight = h / 6;
        cellWidth = (int) (w * 1.0 / COL_NUM);
        cellHeight = (int) ((h - titleHeight) * 1.0 / ROW_NUM);
        titlePaint.setTextSize(cellHeight * 3 / 4);
        textPaint.setTextSize(cellWidth /2);
    }

    /**
     * Description: 行元素
     */
    class RowElement {
        public int posI;

        RowElement(int posI) {
            this.posI = posI;
        }

        public DayCell[] cells = new DayCell[COL_NUM];

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[j] != null) {
                    cells[j].drawSelf(canvas);
                }
            }
        }

    }

    /**
     * Description: 每一天所对应的的单元格
     */
    class DayCell{
        public int posI;
        public int posJ;
        public int dayNum;
        public int busyFlag;
        public boolean isNull;
        public boolean isToday;
        public String dayStr;
        public Rect textRect;

        public DayCell(int posI, int posJ, int dayNum, int busyFlag, boolean isNull, boolean isToday){
            this.posI = posI;
            this.posJ = posJ;
            this.dayNum = dayNum;
            this.busyFlag = busyFlag;
            this.isNull = isNull;
            this.isToday = isToday;
            dayStr = String.valueOf(dayNum);
            textRect = new Rect();
        }

        public void drawSelf(Canvas canvas){
            if(isNull){
                return;
            }
            switch (busyFlag){
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
            }

            if(isToday){
                textPaint.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawCircle(cellWidth * (posJ + 0.5f), cellHeight * (posI + 0.5f) + titleHeight, cellWidth * 3 / 8.0f, redCirclePaint);
            }else{
                textPaint.setColor(Color.parseColor("#787878"));
            }
            textPaint.getTextBounds(dayStr, 0, dayStr.length(), textRect);
            canvas.drawText(dayStr, (float) ((posJ + 0.5) * cellWidth - textRect.width() / 2),
                    (float) ((posI + 0.5) * cellHeight + textRect.height() / 2) + titleHeight, textPaint);
        }
    }
}
