package com.mryhc.app.calendar.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.ViewPagerAdapter;
import com.mryhc.app.calendar.entity.DayInWeekBean;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.ui.custom.CustomWeekView;
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

public class WeekFragment extends BaseFragment {
    private List<View> viewList;
    private CustomWeekView customWeekView01;
    private CustomWeekView customWeekView02;
    private CustomWeekView customWeekView03;


    private int curPosition;
    private int weekInterval;
    private int curPageIndex;

    private Calendar todayCalendar;
    private Calendar curPageCalendar;
    private Calendar tempCalendar;

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
        customWeekView01 = (CustomWeekView) inflater.inflate(R.layout.custom_week_view, null);
        customWeekView02 = (CustomWeekView) inflater.inflate(R.layout.custom_week_view, null);
        customWeekView03 = (CustomWeekView) inflater.inflate(R.layout.custom_week_view, null);
        tempCalendar.add(Calendar.WEEK_OF_YEAR, -1);
        updateCustomWeekViewData(customWeekView01, tempCalendar);
        tempCalendar.setTime(todayCalendar.getTime());
        updateCustomWeekViewData(customWeekView02, tempCalendar);
        tempCalendar.setTime(todayCalendar.getTime());
        tempCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        updateCustomWeekViewData(customWeekView03, tempCalendar);

        viewList = new ArrayList<>();
        viewList.add(customWeekView01);
        viewList.add(customWeekView02);
        viewList.add(customWeekView03);

        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        curPosition = Integer.MAX_VALUE / 2 + 1;
        curPageIndex = curPosition % 3;
        viewPager.setCurrentItem(curPosition);
        setViewPagerListener();
        activity.setActTitle(todayCalendar, MainActivity.FLAG_WEEK);
    }

    private void initData(){
        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        todayCalendar = Calendar.getInstance();
        curPageCalendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();
        weekInterval = 0;
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
                    weekInterval++;
                }else if(position < curPosition){
                    MyLog.i(Common.TAG, "左滑 " + (curPosition % 3) + " --> " + (position % 3));
                    leftMove = true;
                    weekInterval--;
                }
                curPosition = position;
                curPageIndex = position % 3;
                MyLog.i(Common.TAG, "curPage " + curPageIndex + "  dayinteral " + weekInterval);
                refreshData(curPageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshData(int pageIndex){
        todayCalendar.add(Calendar.WEEK_OF_YEAR, weekInterval);
        curPageCalendar.setTime(todayCalendar.getTime());
        todayCalendar.add(Calendar.WEEK_OF_YEAR, -weekInterval);
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_WEEK);
        if(leftMove){
            curPageCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            switch (pageIndex){
                case 0:
                    updateCustomWeekViewData(customWeekView03, tempCalendar);
                    break;
                case 1:
                    updateCustomWeekViewData(customWeekView01, tempCalendar);
                    break;
                case 2:
                    updateCustomWeekViewData(customWeekView02, tempCalendar);
                    break;
            }
        }
        if(rightMove){
            curPageCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            tempCalendar.setTime(curPageCalendar.getTime());
            curPageCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            switch (pageIndex){
                case 0:
                    updateCustomWeekViewData(customWeekView02, tempCalendar);
                    break;
                case 1:
                    updateCustomWeekViewData(customWeekView03, tempCalendar);
                    break;
                case 2:
                    updateCustomWeekViewData(customWeekView01, tempCalendar);
                    break;
            }
        }
    }

    private void updateCustomWeekViewData(CustomWeekView customWeekView, Calendar calendar){
        final List<DayInWeekBean> dayInWeekBeenList = new ArrayList<>();
        for(int i=0; i< 7; i++){
            DayInWeekBean dayInWeekBean = new DayInWeekBean();
            dayInWeekBean.setWeekNum(calendar.get(Calendar.DAY_OF_WEEK));
            dayInWeekBean.setDateStr(sdf.format(calendar.getTime()));
            dayInWeekBeenList.add(dayInWeekBean);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        customWeekView.setDayInWeekBeanList(dayInWeekBeenList);
        customWeekView.setOnItemLongClickListener(new CustomWeekView.OnItemLongClickListener() {
            @Override
            public void onLongClick(int position) {
                activity.showToast("当前点击 --> " + dayInWeekBeenList.get(position).getDateStr(), false);
            }
        });
    }

    @Override
    public void isVisibleInActivity() {
        activity.setActTitle(curPageCalendar, MainActivity.FLAG_WEEK);
    }

    public void setTargetCalendar(Calendar targetCalendar){
        if(viewList != null){   // 当前视图已经被创建过
            todayCalendar.setTime(targetCalendar.getTime());
            curPageCalendar.setTime(targetCalendar.getTime());
            activity.setActTitle(curPageCalendar, MainActivity.FLAG_WEEK);

            Calendar left = Calendar.getInstance();
            Calendar middle = Calendar.getInstance();
            Calendar right = Calendar.getInstance();

            middle.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            left.setTime(targetCalendar.getTime());

            targetCalendar.add(Calendar.WEEK_OF_YEAR, 2);
            right.setTime(targetCalendar.getTime());

            switch (curPageIndex){
                case 0:
                    updateCustomWeekViewData(customWeekView03, left);
                    updateCustomWeekViewData(customWeekView01, middle);
                    updateCustomWeekViewData(customWeekView02, right);
                    break;
                case 1:
                    updateCustomWeekViewData(customWeekView01, left);
                    updateCustomWeekViewData(customWeekView02, middle);
                    updateCustomWeekViewData(customWeekView03, right);
                    break;
                case 2:
                    updateCustomWeekViewData(customWeekView02, left);
                    updateCustomWeekViewData(customWeekView03, middle);
                    updateCustomWeekViewData(customWeekView01, right);
                    break;
            }
            weekInterval = 0;
        }
    }
}
