package com.mryhc.app.calendar.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.MonthViewAdapter;
import com.mryhc.app.calendar.adapter.MyPagerAdapter;
import com.mryhc.app.calendar.adapter.ViewPagerAdapter;
import com.mryhc.app.calendar.entity.MonthDayItemBean;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.ui.custom.BanScrollLayoutManager;
import com.mryhc.app.calendar.ui.custom.CustomMonthView;
import com.mryhc.app.calendar.ui.custom.CustomYearView;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/6/28.
 */

public class MonthFragment extends BaseFragment {
    private List<View> viewList;
    private CustomMonthView customMonthView01;
    private CustomMonthView customMonthView02;
    private CustomMonthView customMonthView03;

    private int curPosition;
    private int monthInterval;
    private int curPageIndex;

    private Calendar todayCalendar;
    private Calendar curPageCalendar;
    private Calendar tempCalendar;

    private boolean leftMove;
    private boolean rightMove;

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
        customMonthView01 = (CustomMonthView) inflater.inflate(R.layout.custom_month_view, null);
        customMonthView02 = (CustomMonthView) inflater.inflate(R.layout.custom_month_view, null);
        customMonthView03 = (CustomMonthView) inflater.inflate(R.layout.custom_month_view, null);

        tempCalendar.add(Calendar.MONTH, -1);
        customMonthView01.setCalendar(tempCalendar);

        tempCalendar.setTime(todayCalendar.getTime());
        customMonthView02.setCalendar(tempCalendar);

        tempCalendar.setTime(todayCalendar.getTime());
        tempCalendar.add(Calendar.MONTH, 1);
        customMonthView03.setCalendar(tempCalendar);

        setViewClickListener(customMonthView01);
        setViewClickListener(customMonthView02);
        setViewClickListener(customMonthView03);

        viewList = new ArrayList<>();
        viewList.add(customMonthView01);
        viewList.add(customMonthView02);
        viewList.add(customMonthView03);

        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        curPosition = Integer.MAX_VALUE / 2 + 1;
        curPageIndex = curPosition % 3;
        viewPager.setCurrentItem(curPosition);
        setViewPagerListener();
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_MONTH);
    }

    private void initData(){
        if(todayCalendar == null){
            todayCalendar = Calendar.getInstance();
            curPageCalendar = Calendar.getInstance();
        }else{
            curPageCalendar.setTime(todayCalendar.getTime());
        }
        tempCalendar = Calendar.getInstance();
        monthInterval = 0;
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
                    MyLog.i(Common.TAG, "右滑 " + (curPosition % 3) + " --> " + (position % 3) + " pos " + position);
                    rightMove = true;
                    monthInterval++;
                }else if(position < curPosition){
                    MyLog.i(Common.TAG, "左滑 " + (curPosition % 3) + " --> " + (position % 3) + " pos " + position);
                    leftMove = true;
                    monthInterval--;
                }
                curPosition = position;
                curPageIndex = position % 3;
                MyLog.i(Common.TAG, "curPage " + curPageIndex + "  monthInterval " + monthInterval);
                refreshData(curPageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshData(int pageIndex){
        todayCalendar.add(Calendar.MONTH, monthInterval);
        curPageCalendar.setTime(todayCalendar.getTime());
        todayCalendar.add(Calendar.MONTH, -monthInterval);
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_MONTH);
        if(leftMove){
            curPageCalendar.add(Calendar.MONTH, -1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.MONTH, 1);
            switch (pageIndex){
                case 0:
                    updateCustomMonthViewData(customMonthView03, tempCalendar);
                    break;
                case 1:
                    updateCustomMonthViewData(customMonthView01, tempCalendar);
                    break;
                case 2:
                    updateCustomMonthViewData(customMonthView02, tempCalendar);
                    break;
            }
        }
        if(rightMove){
            curPageCalendar.add(Calendar.MONTH, 1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.MONTH, -1);
            switch (pageIndex){
                case 0:
                    updateCustomMonthViewData(customMonthView02, tempCalendar);
                    break;
                case 1:
                    updateCustomMonthViewData(customMonthView03, tempCalendar);
                    break;
                case 2:
                    updateCustomMonthViewData(customMonthView01, tempCalendar);
                    break;
            }
        }
    }

    private void updateCustomMonthViewData(CustomMonthView customMonthView, Calendar calendar){
        customMonthView.setCalendar(calendar);
    }

    private void setViewClickListener(CustomMonthView view){
        view.setDayCellClickListener(new CustomMonthView.OnDayCellClickListener() {
            @Override
            public void onDayCellClick(Calendar targetCalendar) {
                activity.jumpToDayView(targetCalendar);
            }
        });
    }

    @Override
    public void isVisibleInActivity() {
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_MONTH);
    }

    public void setTargetCalendar(Calendar targetCalendar){
        if(viewList != null){   // 当前视图已经被创建过
            todayCalendar.setTime(targetCalendar.getTime());
            curPageCalendar.setTime(targetCalendar.getTime());
            activity.setActTitle(curPageCalendar, MainActivity.FLAG_MONTH);

            Calendar left = Calendar.getInstance();
            Calendar middle = Calendar.getInstance();
            Calendar right = Calendar.getInstance();

            middle.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.MONTH, -1);
            left.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.MONTH, 2);
            right.setTime(targetCalendar.getTime());

            MyLog.i(Common.TAG, "MonthFragment setTargetCalendar -> " + curPageIndex);
            MyLog.i(Common.TAG, "MonthFragment left -> " + left.get(Calendar.MONTH));
            MyLog.i(Common.TAG, "MonthFragment middle -> " + middle.get(Calendar.MONTH));
            MyLog.i(Common.TAG, "MonthFragment right -> " + right.get(Calendar.MONTH));

            switch (curPageIndex){
                case 0:
                    updateCustomMonthViewData(customMonthView03, left);
                    updateCustomMonthViewData(customMonthView01, middle);
                    updateCustomMonthViewData(customMonthView02, right);
                    break;
                case 1:
                    updateCustomMonthViewData(customMonthView01, left);
                    updateCustomMonthViewData(customMonthView02, middle);
                    updateCustomMonthViewData(customMonthView03, right);
                    break;
                case 2:
                    updateCustomMonthViewData(customMonthView02, left);
                    updateCustomMonthViewData(customMonthView03, middle);
                    updateCustomMonthViewData(customMonthView01, right);
                    break;
            }
            monthInterval = 0;
        }
    }

    public void responseSettingChanged(){
        customMonthView01.responseSettingChanged();
        customMonthView02.responseSettingChanged();
        customMonthView03.responseSettingChanged();
    }
}
