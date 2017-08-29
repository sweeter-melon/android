package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.Calendar;

/**
 * Created by mryhc on 2017/7/9.
 */

public class CustomYearView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater inflater;
    private Calendar calendar;
    private OnItemClickListener listener;
    private int curYear;

    public CustomYearView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }

    public CustomYearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomYearView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle){
        inflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        fillView();
    }

    private void fillView(){
        this.removeAllViews();
        calendar.set(Calendar.MONTH, 0);
        int row = -1;
        for(int i = 0; i< 12; i++){
            row = i / 3;
            LinearLayout linearLayout = (LinearLayout) getChildAt(row);
            if(linearLayout == null){
                linearLayout = (LinearLayout) inflater.inflate(R.layout.null_content_linearlayout, this, false);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                this.addView(linearLayout);
            }
            CustomMonthInYearAnotherView customMonthInYearView = (CustomMonthInYearAnotherView) inflater.inflate(R.layout.custom_month_in_year_view, null);
            customMonthInYearView.setCalendar(calendar);
            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            itemLayoutParams.setMargins(15, 10, 15, 10);
            customMonthInYearView.setLayoutParams(itemLayoutParams);
            customMonthInYearView.setTag(R.id.view_tag_01, i);
            customMonthInYearView.setOnClickListener(this);
            linearLayout.addView(customMonthInYearView);

            calendar.add(Calendar.MONTH, 1);
        }
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        curYear = calendar.get(Calendar.YEAR);
        fillView();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.custom_month_in_year_another_view){
            if(listener != null){
                listener.onClick(curYear, (int)(v.getTag(R.id.view_tag_01)));
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(int year, int month);
    }
}
