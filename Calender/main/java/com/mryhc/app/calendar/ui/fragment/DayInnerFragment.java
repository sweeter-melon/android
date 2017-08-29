package com.mryhc.app.calendar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.entity.HourBean;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.ui.custom.DayBackgroundView;
import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mryhc on 2017/7/4.
 */

public class DayInnerFragment extends Fragment {
    private View rootView;
    private LinearLayout lineForCurrent;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_day_inner, container, false);
        activity = (MainActivity)(getParentFragment().getActivity());
        initView();
        setCurrentLinePos();
        return rootView;
    }

    private void initView(){
        DayBackgroundView backgroundView = (DayBackgroundView) rootView.findViewById(R.id.day_inner_back_view);
        final List<HourBean> hourBeanList = new ArrayList<>();
        for(int i=0; i<24; i++){
            HourBean hourBean = new HourBean();
            if(i==0){
                hourBean.setZero(true);
            }else{
                hourBean.setZero(false);
            }
            hourBean.setHour(i);
            hourBeanList.add(hourBean);
        }
        backgroundView.setOnItemLongClickListener(new DayBackgroundView.OnItemLongClickListener() {
            @Override
            public void onLongClick(int position) {
                activity.showToast("当前item --> " + position, false);
            }
        });
        backgroundView.setHourBeanList(hourBeanList);
    }

    private void setCurrentLinePos(){
        lineForCurrent = (LinearLayout) rootView.findViewById(R.id.line_for_current);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        MyLog.i(Common.TAG, "时间 --> " + hour+":"+minute);
        FrameLayout.LayoutParams fllp = (FrameLayout.LayoutParams) lineForCurrent.getLayoutParams();
        fllp.topMargin = AppUtils.dp2px(activity, hour * 80 + (minute / 60.0f) * 80 + 15.0f) + hour - 5;
        MyLog.i(Common.TAG, "topmarging --> " + (AppUtils.dp2px(activity, hour * 80 + (minute / 60.0f) * 80 + 15.0f) + hour - 5));
        lineForCurrent.setLayoutParams(fllp);
    }
}
