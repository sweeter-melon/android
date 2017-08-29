package com.mryhc.app.calendar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.ui.activity.MainActivity;

/**
 * Created by mryhc on 2017/6/28.
 */

public abstract class BaseFragment extends Fragment {
    protected MainActivity activity;
    protected View view;
    protected ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(), container, false);
        activity = (MainActivity) getActivity();
        viewPager = (ViewPager) view.findViewById(getViewPagerId());
        initView();
        return view;
    }

    protected abstract int getLayout();

    protected abstract int getViewPagerId();

    protected abstract void initView();

    public abstract void isVisibleInActivity();

}
