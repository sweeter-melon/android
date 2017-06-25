package com.mryhc.calendarpro.ui.fragment;

import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.adapter.FragmentAdapter;
import com.mryhc.calendarpro.ui.activity.MainActivity;
import com.mryhc.calendarpro.utils.Common;
import com.mryhc.calendarpro.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/6/15.
 */

public class DayFragment extends BaseFragment {
    private MainActivity parentActicity;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private int curIndex;
    private int dateValue;

    private boolean firstOrLast;
    private boolean initOperate;
    private Calendar calendar;

    @Override
    protected int setLayout() {
        return R.layout.fragment_day;
    }

    @Override
    protected void initView() {
        initData();
        viewPager = (ViewPager) view.findViewById(R.id.day_view_pager);
        initViewPager();
    }

    private void initData(){
        dateValue = 17;
        initOperate = true;
        firstOrLast = false;
        calendar = Calendar.getInstance();
        parentActicity = (MainActivity)getActivity();
    }

    private void initViewPager(){
        List<BaseFragment> fragmentList = new ArrayList<>();
        DayInnerFragment fragment00 = new DayInnerFragment();
        DayInnerFragment fragment01 = new DayInnerFragment();
        DayInnerFragment fragment02 = new DayInnerFragment();
        DayInnerFragment fragment03 = new DayInnerFragment();
        DayInnerFragment fragment04 = new DayInnerFragment();
        fragment00.setPosition(2);
        fragment01.setPosition(0);
        fragment02.setPosition(1);
        fragment03.setPosition(2);
        fragment04.setPosition(0);
        fragmentList.add(fragment00);
        fragmentList.add(fragment01);
        fragmentList.add(fragment02);
        fragmentList.add(fragment03);
        fragmentList.add(fragment04);
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

    public void resetDate(int targetPos){
        if(!initOperate && !firstOrLast){
            if((curIndex-targetPos) > 0){
                dateValue--;
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }else{
                dateValue++;
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            parentActicity.setActTitle(calendar, MainActivity.TITLE_FLAG_DAY);
        }
        if(initOperate){
            initOperate = false;
        }
    }

    public int getDateValue(){
        return dateValue;
    }
}
