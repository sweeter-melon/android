package com.mryhc.app.calendar.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.CommonAdapter;
import com.mryhc.app.calendar.adapter.ViewPagerAdapter;
import com.mryhc.app.calendar.db.DBUtils;
import com.mryhc.app.calendar.entity.HourBean;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.ui.custom.CustomColumnWeekView;
import com.mryhc.app.calendar.ui.custom.CustomDayView;
import com.mryhc.app.calendar.ui.custom.DayBackgroundView;
import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mryhc on 2017/6/28.
 */

public class DayFragment extends BaseFragment {
    private List<View> viewList;
    private CustomDayView customDayView01;
    private CustomDayView customDayView02;
    private CustomDayView customDayView03;

    private int curPosition;
    private int dayInterval;
    private int curPageIndex;

    private Calendar todayCalendar;
    private Calendar curPageCalendar;
    private Calendar tempCalendar;
    private Calendar queryCalendar;

    private Calendar curViewTargetCalendar;

    private boolean leftMove;
    private boolean rightMove;

    private SimpleDateFormat sdf;

    @Override
    protected int getLayout() {
        return R.layout.fragment_for_loop;
    }

    @Override
    protected int getViewPagerId() {
        return R.id.loop_view_pager;
    }

    @Override
    protected void initView() {
        initData();
        LayoutInflater inflater = LayoutInflater.from(activity);
        customDayView01 = (CustomDayView) inflater.inflate(R.layout.custom_day_view, null);
        customDayView02 = (CustomDayView) inflater.inflate(R.layout.custom_day_view, null);
        customDayView03 = (CustomDayView) inflater.inflate(R.layout.custom_day_view, null);
        tempCalendar.add(Calendar.DAY_OF_YEAR, -1);
        updateCustomDayViewData(customDayView01, tempCalendar);
        tempCalendar.setTime(todayCalendar.getTime());
        updateCustomDayViewData(customDayView02, tempCalendar);
        tempCalendar.setTime(todayCalendar.getTime());
        tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
        updateCustomDayViewData(customDayView03, tempCalendar);

        viewList = new ArrayList<>();
        viewList.add(customDayView01);
        viewList.add(customDayView02);
        viewList.add(customDayView03);

        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        curPosition = Integer.MAX_VALUE / 2 + 1;
        curPageIndex = curPosition % 3;
        viewPager.setCurrentItem(curPosition);
        setViewPagerListener();
        activity.setActTitle(todayCalendar, MainActivity.FLAG_DAY);
    }

    private void initData(){
        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        todayCalendar = Calendar.getInstance();
        curPageCalendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();

        if(curViewTargetCalendar != null){
            todayCalendar.setTime(curViewTargetCalendar.getTime());
            curPageCalendar.setTime(curViewTargetCalendar.getTime());
            tempCalendar.setTime(curViewTargetCalendar.getTime());
        }

        dayInterval = 0;
        leftMove = false;
        rightMove = false;
    }



    private void setViewPagerListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                leftMove = false;
                rightMove = false;
                if(position > curPosition) {
                    MyLog.i(Common.TAG, "右滑 " + (curPosition % 3) + " --> " + (position % 3));
                    rightMove = true;
                    dayInterval++;
                }else if(position < curPosition){
                    MyLog.i(Common.TAG, "左滑 " + (curPosition % 3) + " --> " + (position % 3));
                    leftMove = true;
                    dayInterval--;
                }
                curPosition = position;
                curPageIndex = position % 3;
                MyLog.i(Common.TAG, "curPage " + curPageIndex + "  dayinteral " + dayInterval);
                refreshData(curPageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshData(int pageIndex){
        todayCalendar.add(Calendar.DAY_OF_YEAR, dayInterval);
        curPageCalendar.setTime(todayCalendar.getTime());
        todayCalendar.add(Calendar.DAY_OF_YEAR, -dayInterval);
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_DAY);
        if(leftMove){
            curPageCalendar.add(Calendar.DAY_OF_YEAR, -1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.DAY_OF_YEAR, 1);
            switch (pageIndex){
                case 0:
                    updateCustomDayViewData(customDayView03, tempCalendar);
                    break;
                case 1:
                    updateCustomDayViewData(customDayView01, tempCalendar);
                    break;
                case 2:
                    updateCustomDayViewData(customDayView02, tempCalendar);
                    break;
            }
        }
        if(rightMove){
            curPageCalendar.add(Calendar.DAY_OF_YEAR, 1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.DAY_OF_YEAR, -1);
            switch (pageIndex){
                case 0:
                    updateCustomDayViewData(customDayView02, tempCalendar);
                    break;
                case 1:
                    updateCustomDayViewData(customDayView03, tempCalendar);
                    break;
                case 2:
                    updateCustomDayViewData(customDayView01, tempCalendar);
                    break;
            }
        }
    }

    private void updateCustomDayViewData(CustomDayView customDayView, Calendar calendar){
        customDayView.setCalendar(calendar);
    }

    @Override
    public void isVisibleInActivity() {
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_DAY);
    }

    public void setTargetCalendar(Calendar targetCalendar){
        if(viewList != null){   // 当前视图已经被创建过
            todayCalendar.setTime(targetCalendar.getTime());
            curPageCalendar.setTime(targetCalendar.getTime());
            activity.setActTitle(curPageCalendar, MainActivity.FLAG_DAY);

            Calendar left = Calendar.getInstance();
            Calendar middle = Calendar.getInstance();
            Calendar right = Calendar.getInstance();

            middle.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.DAY_OF_YEAR, -1);
            left.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.DAY_OF_YEAR, 2);
            right.setTime(targetCalendar.getTime());

            switch (curPageIndex){
                case 0:
                    updateCustomDayViewData(customDayView03, left);
                    updateCustomDayViewData(customDayView01, middle);
                    updateCustomDayViewData(customDayView02, right);
                    break;
                case 1:
                    updateCustomDayViewData(customDayView01, left);
                    updateCustomDayViewData(customDayView02, middle);
                    updateCustomDayViewData(customDayView03, right);
                    break;
                case 2:
                    updateCustomDayViewData(customDayView02, left);
                    updateCustomDayViewData(customDayView03, middle);
                    updateCustomDayViewData(customDayView01, right);
                    break;
            }
            dayInterval = 0;
        }else{
            curViewTargetCalendar = targetCalendar;
        }
    }
}
