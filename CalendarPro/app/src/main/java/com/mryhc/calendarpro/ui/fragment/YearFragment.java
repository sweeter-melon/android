package com.mryhc.calendarpro.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.adapter.CommonAdapter;
import com.mryhc.calendarpro.adapter.ViewHolder;
import com.mryhc.calendarpro.bean.MonthDayItemBean;
import com.mryhc.calendarpro.ui.self.AdvancedGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/6/15.
 */

public class YearFragment extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.fragment_year;
    }

    @Override
    protected void initView() {
    }
}
