package com.mryhc.app.calendar.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by mryhc on 2017/6/30.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<RecyclerView> viewList;

    public MyPagerAdapter(List<RecyclerView> viewList){
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

        RecyclerView recyclerView = viewList.get(index);

        ViewParent vp = recyclerView.getParent();

        if(vp != null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(recyclerView);
        }

        container.addView(recyclerView);

        return recyclerView;
    }
}
