package com.mryhc.calendarpro.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.mryhc.calendarpro.R;
import com.mryhc.calendarpro.adapter.CommonAdapter;
import com.mryhc.calendarpro.adapter.ViewHolder;
import com.mryhc.calendarpro.bean.DayHourItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/6/17.
 */

public class DayInnerFragment extends BaseFragment {
    private int position;
    private DayFragment parentFragment;
    private List<DayHourItemBean> hourItemList ;
    private CommonAdapter<DayHourItemBean> adapter;
    private ListView listView;

    @Override
    protected int setLayout() {
        return R.layout.fragment_day_inner;
    }

    @Override
    protected void initView() {
        parentFragment = (DayFragment) getParentFragment();
        listView = (ListView) view.findViewById(R.id.day_inner_list_view);
        initListView();
    }

    private void initListView(){
        hourItemList = new ArrayList<>();
        for(int i=0; i<24;i++){
            DayHourItemBean hourItem = new DayHourItemBean();
            hourItem.setHour(i);
            if(i==0){
                hourItem.setFirst(true);
            }
            if(i==23){
                hourItem.setLast(true);
            }
            hourItemList.add(hourItem);
        }
        adapter = new CommonAdapter<DayHourItemBean>(getContext(), hourItemList, R.layout.day_inner_list_view_item) {
            @Override
            protected void convert(ViewHolder viewHolder, DayHourItemBean item) {
                int hour = item.getHour();
                viewHolder.setText(R.id.day_inner_list_view_item_time, hour < 10 ? "0" + hour : "" + hour);
                viewHolder.setViewVisibility(R.id.day_inner_list_view_item_top_line, item.isFirst() ? View.VISIBLE : View.INVISIBLE);
                viewHolder.setViewVisibility(R.id.day_inner_list_view_item_top, item.isFirst() ? View.VISIBLE : View.GONE);
                viewHolder.setViewVisibility(R.id.day_inner_list_view_item_bottom, item.isLast() ? View.VISIBLE : View.GONE);
            }
        };
        listView.setAdapter(adapter);
    }

    public void setPosition(int position){
        this.position = position;
        getParentFragment();
    }
}
