package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mryhc.app.calendar.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mryhc on 2017/7/9.
 */

public class CustomDayOfMonthInYearView extends FrameLayout {

    private ImageView busyFlagView;
    private CircleImageView todayFlagView;
    private TextView tvDay;

    private int busyFlag;
    private boolean todayFlag;
    private String dayText;

    public CustomDayOfMonthInYearView(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomDayOfMonthInYearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomDayOfMonthInYearView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomDayOfMonthInYearView, defStyleAttr, 0);
        for(int i=0; i<typedArray.getIndexCount(); i++){
            int attrIndex = typedArray.getIndex(i);
            switch (attrIndex){
                case R.styleable.CustomDayOfMonthInYearView_busy_flag:
                    busyFlag = typedArray.getInt(attrIndex, 0);
                    break;
                case R.styleable.CustomDayOfMonthInYearView_today_flag:
                    todayFlag = typedArray.getBoolean(attrIndex, false);
                    break;
                case R.styleable.CustomDayOfMonthInYearView_day_text:
                    dayText = typedArray.getString(attrIndex);
                    break;
            }
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        busyFlagView = (ImageView) inflater.inflate(R.layout.image_view, this, false);
        todayFlagView = (CircleImageView) inflater.inflate(R.layout.circle_image_view, this, false);
        tvDay = (TextView) inflater.inflate(R.layout.text_view, this, false);
        tvDay.setTextSize(10);
        this.addView(busyFlagView);
        this.addView(todayFlagView);
        this.addView(tvDay);

        setBusyFlag(busyFlag);
        setTodayFlag(todayFlag);
        setDayText(dayText);
    }

    public void setBusyFlag(int busyFlag){
        if(busyFlag > 0){
            busyFlagView.setVisibility(VISIBLE);
        }else{
            busyFlagView.setVisibility(INVISIBLE);
        }
    }

    public void setTodayFlag(boolean todayFlag){
        if(todayFlag){
            todayFlagView.setVisibility(VISIBLE);
        }else{
            todayFlagView.setVisibility(INVISIBLE);
        }
    }

    public void setDayText(String dayText){
        tvDay.setText(dayText);
    }
}
