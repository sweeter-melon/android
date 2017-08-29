package com.mryhc.app.calendar.ui.custom;

import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by mryhc on 2017/6/21.
 */

public class BanScrollLayoutManager extends StaggeredGridLayoutManager {
    public BanScrollLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
