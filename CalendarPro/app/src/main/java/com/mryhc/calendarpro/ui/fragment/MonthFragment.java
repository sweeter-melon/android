package com.mryhc.calendarpro.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.Switch;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.adapter.FragmentAdapter;
import com.mryhc.calendarpro.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/6/15.
 */

public class MonthFragment extends BaseFragment {
    private static final int INIT_FRAGMENT_LIST_OK = 1;

    private MainActivity parentActivity;
    private ViewPager viewPager;
    private List<BaseFragment> fragmentList;
    private FragmentAdapter adapter;
    private int curIndex;
    private int dateValue;

    private boolean firstOrLast;
    private boolean initOperate;
    private Calendar calendar;
    private int curYear;
    private int curMonth;
    private int curDay;

    private Handler handler;

    @Override
    protected int setLayout() {
        return R.layout.fragment_day;
    }

    @Override
    protected void initView() {
        initData();
        viewPager = (ViewPager) view.findViewById(R.id.day_view_pager);
        startThread();
    }

    private void initData(){
        dateValue = 17;
        initOperate = true;
        firstOrLast = false;
        calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        parentActivity = (MainActivity)getActivity();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case INIT_FRAGMENT_LIST_OK:
                        initViewPager();
                        break;
                }
                return true;
            }
        });
        fragmentList = new ArrayList<>();
    }

    private void initViewPager(){
        adapter = new FragmentAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetDate(position);
                switch (position){
                    case 0:
                        firstOrLast = true;
                        position = 3;
                        viewPager.setCurrentItem(position, false);
                        break;
                    case 1:
                        firstOrLast = false;
                        break;
                    case 2:
                        firstOrLast = false;
                        break;
                    case 3:
                        firstOrLast = false;
                        break;
                    case 4:
                        firstOrLast = true;
                        position = 1;
                        viewPager.setCurrentItem(position, false);
                        break;
                }
                curIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(1);
    }

    private void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MonthInnerFragment fragment00 = new MonthInnerFragment();
                MonthInnerFragment fragment01 = new MonthInnerFragment();
                MonthInnerFragment fragment02 = new MonthInnerFragment();
                MonthInnerFragment fragment03 = new MonthInnerFragment();
                MonthInnerFragment fragment04 = new MonthInnerFragment();
                fragmentList.add(fragment00);
                fragmentList.add(fragment01);
                fragmentList.add(fragment02);
                fragmentList.add(fragment03);
                fragmentList.add(fragment04);
                try{
                    Thread.sleep(300);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(INIT_FRAGMENT_LIST_OK);
            }
        }).start();
    }

    public void resetDate(int targetPos){
        if(!initOperate && !firstOrLast){
            if((curIndex-targetPos) > 0){
                dateValue--;
                calendar.add(Calendar.MONTH, -1);
            }else{
                dateValue++;
                calendar.add(Calendar.MONTH, 1);
            }
            parentActivity.setActTitle(calendar, MainActivity.TITLE_FLAG_MONTH);
        }
        if(initOperate){
            initOperate = false;
        }
    }

    public Calendar getMonthCalendar(){
        return calendar;
    }

    public int getCurDay() {
        return curDay;
    }

    public int getCurMonth() {
        return curMonth;
    }

    public int getCurYear() {
        return curYear;
    }
}
