package com.mryhc.calendarpro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mryhc.calendarpro.ui.fragment.BaseFragment;

import java.util.List;

/**
 * Created by mryhc on 2017/5/22.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    /**
     * 获取Fragment集合
     */
    private List<BaseFragment> fragmentList ;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 返回对应位置的Fragment
     */
    @Override
    public BaseFragment getItem(int location) {
        return fragmentList.get(location);
    }
    /**
     * 得到Fragment的总数
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
