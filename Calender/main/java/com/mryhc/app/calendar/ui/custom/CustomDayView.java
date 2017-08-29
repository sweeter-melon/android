package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.db.DBUtils;
import com.mryhc.app.calendar.entity.EventEntity;
import com.mryhc.app.calendar.entity.MyDate;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/7/15.
 */

public class CustomDayView extends ScrollView {

    private int childHeight;
    private int scrollLocation;

    private Calendar calendar;
    private Calendar queryCalendar;
    private int curHour;

    private CustomDayHourView hourView;
    private int todayYear;
    private int todayMonth;
    private int today;

    private boolean isToday;

    private LinearLayout thisChild;
    private View allDayEventPartLayout;
    private LinearLayout allDayItemParentLayout;

    private List<EventEntity> allDayEventList;
    private List<EventEntity> normalEventList;

    private LayoutInflater inflater;



    private boolean canScroll;
    private GestureDetector mGestureDetector;

    public CustomDayView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        mGestureDetector = new GestureDetector(getContext(), new CustomDayView.YScrollDetector());
        canScroll = true;

        allDayEventList = new ArrayList<>();
        normalEventList = new ArrayList<>();

        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        if(queryCalendar == null){
            queryCalendar = Calendar.getInstance();
        }
        Calendar todayCalendar = Calendar.getInstance();
        todayYear = todayCalendar.get(Calendar.YEAR);
        todayMonth = todayCalendar.get(Calendar.MONTH);
        today = todayCalendar.get(Calendar.DAY_OF_MONTH);

        curHour = calendar.get(Calendar.HOUR_OF_DAY);

        inflater = LayoutInflater.from(context);
        allDayEventPartLayout = inflater.inflate(R.layout.day_view_all_day_event, null);
        allDayItemParentLayout = (LinearLayout) allDayEventPartLayout.findViewById(R.id.day_view_all_day_event_parent);

        hourView = new CustomDayHourView(context);
        LinearLayout.LayoutParams hourViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hourView.setLayoutParams(hourViewLayoutParams);

        thisChild = new LinearLayout(context);
        thisChild.setOrientation(LinearLayout.VERTICAL);
        ScrollView.LayoutParams lllp = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thisChild.setLayoutParams(lllp);
        thisChild.addView(hourView);

        this.addView(thisChild);

        setInitViewOverListener();

        getEventFromDB();
    }

    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
        if(queryCalendar == null){
            queryCalendar = Calendar.getInstance();
        }
        queryCalendar.setTime(calendar.getTime());


        if(todayYear == calendar.get(Calendar.YEAR) && todayMonth == calendar.get(Calendar.MONTH) && today == calendar.get(Calendar.DAY_OF_MONTH)){
            isToday = true;
        }else{
            isToday = false;
        }

        hourView.setCalendar(calendar, isToday);
        hourView.invalidate();
        getEventFromDB();
        invalidate();
    }

    private void getEventFromDB(){
        queryCalendar.set(Calendar.HOUR_OF_DAY, 0);
        queryCalendar.set(Calendar.MINUTE, 0);
        queryCalendar.set(Calendar.SECOND, 0);

        long start = (queryCalendar.getTimeInMillis()) / 1000 * 1000;
        queryCalendar.add(Calendar.DAY_OF_YEAR, 1);
        long end = (queryCalendar.getTimeInMillis()) / 1000 * 1000;

        allDayEventList.clear();
        normalEventList.clear();
        List<EventEntity> tempList = DBUtils.getEventByDate(start, end);
        for(EventEntity eventEntity : tempList){
            if(eventEntity.getAllDay() == 1){
                allDayEventList.add(eventEntity);
            }else{
                normalEventList.add(eventEntity);
            }

        }
        hourView.setEventEntityList(normalEventList);

        if(allDayEventList.size() > 0){
            if(thisChild.getChildCount() == 1){
                thisChild.addView(allDayEventPartLayout, 0);

                allDayItemParentLayout.removeAllViews();
                for(EventEntity eventEntity:allDayEventList){
                    TextView tv = (TextView) inflater.inflate(R.layout.event_text_view, null);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.topMargin = 2;
                    lp.bottomMargin = 2;
                    tv.setLayoutParams(lp);
                    tv.setText(eventEntity.getDetail());
                    allDayItemParentLayout.addView(tv);
                }
            }
        }else{
            if(thisChild.getChildCount() > 1){
                thisChild.removeView(allDayEventPartLayout);
            }
        }
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
    }

    private void setInitViewOverListener(){

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                if(isToday){
                    curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    scrollLocation = childHeight / 24 * curHour;
                }else{
                    scrollLocation = childHeight / 24 * 8;
                }
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


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            canScroll = true;
        }
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if(canScroll){
                if(Math.abs(distanceY)>= Math.abs(distanceX)){
                    canScroll = true;
                }else{
                    canScroll = false;
                }
            }
            return canScroll;
        }

    }
}
