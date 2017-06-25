package com.mryhc.calendarpro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by mryhc on 2017/6/21.
 */

public class LoopAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public LoopAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
