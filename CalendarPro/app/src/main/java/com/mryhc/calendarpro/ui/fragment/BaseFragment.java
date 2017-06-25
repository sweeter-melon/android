package com.mryhc.calendarpro.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mryhc on 2017/6/15.
 */

public abstract class BaseFragment extends Fragment {
    protected View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setLayout(), container,false);
        initView();
        return view;
    }

    protected abstract int setLayout();
    protected abstract void initView();
}
