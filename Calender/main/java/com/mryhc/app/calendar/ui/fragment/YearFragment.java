package com.mryhc.app.calendar.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.ViewPagerAdapter;
import com.mryhc.app.calendar.entity.DayInWeekBean;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.ui.custom.CustomWeekView;
import com.mryhc.app.calendar.ui.custom.CustomYearView;
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

public class YearFragment extends BaseFragment {
    private List<View> viewList;
    private CustomYearView customYearView01;
    private CustomYearView customYearView02;
    private CustomYearView customYearView03;

    private int curPosition;
    private int yearInterval;
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
        customYearView01 = (CustomYearView) inflater.inflate(R.layout.custom_year_view, null);
        customYearView02 = (CustomYearView) inflater.inflate(R.layout.custom_year_view, null);
        customYearView03 = (CustomYearView) inflater.inflate(R.layout.custom_year_view, null);

        tempCalendar.add(Calendar.YEAR, -1);
        customYearView01.setCalendar(tempCalendar);

        tempCalendar.setTime(todayCalendar.getTime());
        customYearView02.setCalendar(tempCalendar);

        tempCalendar.setTime(todayCalendar.getTime());
        tempCalendar.add(Calendar.YEAR, 1);
        customYearView03.setCalendar(tempCalendar);

        setViewClickListener(customYearView01);
        setViewClickListener(customYearView02);
        setViewClickListener(customYearView03);

        viewList = new ArrayList<>();
        viewList.add(customYearView01);
        viewList.add(customYearView02);
        viewList.add(customYearView03);

        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        curPosition = Integer.MAX_VALUE / 2 + 1;
        curPageIndex = curPosition % 3;
        viewPager.setCurrentItem(curPosition);
        setViewPagerListener();
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_YEAR);
    }

    private void initData(){
        todayCalendar = Calendar.getInstance();
        curPageCalendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();
        yearInterval = 0;
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
                    yearInterval++;
                }else if(position < curPosition){
                    MyLog.i(Common.TAG, "左滑 " + (curPosition % 3) + " --> " + (position % 3));
                    leftMove = true;
                    yearInterval--;
                }
                curPosition = position;
                curPageIndex = position % 3;
                MyLog.i(Common.TAG, "curPage " + curPageIndex + "  yearInterval " + yearInterval);
                refreshData(curPageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshData(int pageIndex){
        todayCalendar.add(Calendar.YEAR, yearInterval);
        curPageCalendar.setTime(todayCalendar.getTime());
        todayCalendar.add(Calendar.YEAR, -yearInterval);
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_YEAR);
        if(leftMove){
            curPageCalendar.add(Calendar.YEAR, -1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.YEAR, 1);
            switch (pageIndex){
                case 0:
                    updateCustomYearViewData(customYearView03, tempCalendar);
                    break;
                case 1:
                    updateCustomYearViewData(customYearView01, tempCalendar);
                    break;
                case 2:
                    updateCustomYearViewData(customYearView02, tempCalendar);
                    break;
            }
        }
        if(rightMove){
            curPageCalendar.add(Calendar.YEAR, 1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.YEAR, -1);
            switch (pageIndex){
                case 0:
                    updateCustomYearViewData(customYearView02, tempCalendar);
                    break;
                case 1:
                    updateCustomYearViewData(customYearView03, tempCalendar);
                    break;
                case 2:
                    updateCustomYearViewData(customYearView01, tempCalendar);
                    break;
            }
        }
    }

    private void updateCustomYearViewData(CustomYearView customYearView, Calendar calendar){
        customYearView.setCalendar(calendar);
    }

    private void setViewClickListener(CustomYearView view){
        view.setOnItemClickListener(new CustomYearView.OnItemClickListener() {
            @Override
            public void onClick(int year, int month) {
                Calendar targetCalendar = Calendar.getInstance();
                targetCalendar.set(Calendar.YEAR, year);
                targetCalendar.set(Calendar.MONTH, month);
                activity.jumpToMonthView(targetCalendar);
                activity.showToast("点击 -->" + year + "年" + (month + 1) + "月", false);
            }
        });
    }

    @Override
    public void isVisibleInActivity() {
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_YEAR);
    }

    public void setTargetCalendar(Calendar targetCalendar){
        if(viewList != null){   // 当前视图已经被创建过
            todayCalendar.setTime(targetCalendar.getTime());
            curPageCalendar.setTime(targetCalendar.getTime());
            activity.setActTitle(curPageCalendar, MainActivity.FLAG_YEAR);

            Calendar left = Calendar.getInstance();
            Calendar middle = Calendar.getInstance();
            Calendar right = Calendar.getInstance();

            middle.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.YEAR, -1);
            left.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.YEAR, 2);
            right.setTime(targetCalendar.getTime());

            switch (curPageIndex){
                case 0:
                    updateCustomYearViewData(customYearView03, left);
                    updateCustomYearViewData(customYearView01, middle);
                    updateCustomYearViewData(customYearView02, right);
                    break;
                case 1:
                    updateCustomYearViewData(customYearView01, left);
                    updateCustomYearViewData(customYearView02, middle);
                    updateCustomYearViewData(customYearView03, right);
                    break;
                case 2:
                    updateCustomYearViewData(customYearView02, left);
                    updateCustomYearViewData(customYearView03, middle);
                    updateCustomYearViewData(customYearView01, right);
                    break;
            }
            yearInterval = 0;
        }
    }
}
