package com.mryhc.calendarpro.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewTreeObserver;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.adapter.MonthViewAdapter;
import com.mryhc.calendarpro.bean.MonthDayItemBean;
import com.mryhc.calendarpro.ui.self.BanScrollLayoutManager;
import com.mryhc.calendarpro.utils.Common;
import com.mryhc.calendarpro.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/6/15.
 */

public class MonthInnerFragment extends BaseFragment {
    private static final int SET_DATA_OK = 1;

    private MonthFragment parentFragment;
    private RecyclerView recyclerView;
    private MonthViewAdapter adapter;
    private List<MonthDayItemBean> dayList;

    private Handler handler;

    private Calendar curMonthCalendar;

    private boolean isVisible;

    @Override
    protected int setLayout() {
        return R.layout.fragment_month_inner;
    }

    @Override
    protected void initView() {
        parentFragment = (MonthFragment) getParentFragment();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case SET_DATA_OK:
//                        adapter.notifyDataSetChanged();
                        setData();
                        break;
                }
                return false;
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.month_inner_recycler_view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        dayList = new ArrayList<>();
        BanScrollLayoutManager layoutManager = new BanScrollLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthViewAdapter(dayList);
        recyclerView.setAdapter(adapter);
//        setData();
        startThread();
    }

    private void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(300);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(SET_DATA_OK);
            }
        }).start();
    }

    private void setData(){
        curMonthCalendar = Calendar.getInstance();
        curMonthCalendar.setTime(parentFragment.getMonthCalendar().getTime());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                dayList.clear();
                int curMonth = curMonthCalendar.get(Calendar.MONTH);
                int today = parentFragment.getCurDay();
                int month = -1;
                int day = -1;
                curMonthCalendar.add(Calendar.DAY_OF_YEAR, 1-today);
                int firstDayOfWeek = curMonthCalendar.get(Calendar.DAY_OF_WEEK);

                curMonthCalendar.add(Calendar.DAY_OF_YEAR, 1- firstDayOfWeek);
                for (int i = 0; i < 42; i++) {
                    MonthDayItemBean monthDayItemBean = new MonthDayItemBean();
                    monthDayItemBean.setYear(curMonthCalendar.get(Calendar.YEAR));
                    monthDayItemBean.setDayOfWeek(curMonthCalendar.get(Calendar.DAY_OF_WEEK));
                    month = curMonthCalendar.get(Calendar.MONTH);
                    monthDayItemBean.setMonth(month);
                    day = curMonthCalendar.get(Calendar.DAY_OF_MONTH);
                    monthDayItemBean.setDay(day);
                    if(month == curMonth){
                        monthDayItemBean.setCurMonth(true);
                    }
                    if(month == parentFragment.getCurMonth() && day == today){
                        monthDayItemBean.setToday(true);
                    }
                    dayList.add(monthDayItemBean);
                    curMonthCalendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                adapter.notifyDataSetChanged();
//                handler.sendEmptyMessage(SET_DATA_OK);
//            }
//        }).start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(recyclerView != null && isVisibleToUser){
//            setData();
            isVisible = true;
        }
    }
}
