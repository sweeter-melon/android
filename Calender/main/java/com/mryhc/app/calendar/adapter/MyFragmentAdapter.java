package com.mryhc.app.calendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mryhc on 2017/5/22.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    /**
     * 获取Fragment集合
     */
    private List<Fragment> fragmentList ;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 返回对应位置的Fragment
     */
    @Override
    public Fragment getItem(int location) {
        int index = 0;
        index = location % fragmentList.size();
        if(index < 0){
            index = index + fragmentList.size();
        }
        return fragmentList.get(index);
    }
    /**
     * 得到Fragment的总数
     */
    @Override
    public int getCount() {
//        return fragmentList.size();
        return Integer.MAX_VALUE;
    }
}
