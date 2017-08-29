package com.mryhc.app.calendar.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by mryhc on 2017/7/5.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;

    public ViewPagerAdapter(List<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = 0;
        index = position % viewList.size();
        if(index < 0){
            index = index + viewList.size();
        }

        View view = viewList.get(index);

        ViewParent vp = view.getParent();

        if(vp != null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }

        container.addView(view);

        return view;
    }
}
