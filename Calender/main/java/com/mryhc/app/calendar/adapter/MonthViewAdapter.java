package com.mryhc.app.calendar.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.entity.MonthDayItemBean;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mryhc on 2017/6/17.
 */

public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.ViewHolder> {
    private int itemHeight;

    private List<MonthDayItemBean> dayList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout wholeItemView;
        View topLine;
        View leftLine;
        CircleImageView dayBg;
        TextView tvDay;

        public ViewHolder(View view) {
            super(view);
            wholeItemView = (LinearLayout) view.findViewById(R.id.month_inner_rv_item_ll);
            topLine = view.findViewById(R.id.month_inner_rv_item_top_line);
            leftLine = view.findViewById(R.id.month_inner_rv_item_left_line);
            dayBg = (CircleImageView) view.findViewById(R.id.month_inner_recycler_view_day_bg);
            tvDay = (TextView) view.findViewById(R.id.month_inner_recycler_view_day);
        }
    }

    public MonthViewAdapter(List<MonthDayItemBean> dayList) {
        this.dayList = dayList;
        itemHeight = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_inner_recycler_view_item, parent, false);
        if(itemHeight == -1){
            itemHeight = parent.getHeight() / 6;
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.wholeItemView.getLayoutParams();
        lp.height = itemHeight;
        holder.wholeItemView.setLayoutParams(lp);
        MonthDayItemBean dayItem = dayList.get(position);
        holder.tvDay.setText(dayItem.getDay()+"");
        if(dayItem.getDay() == 1){
            holder.leftLine.setBackgroundColor(Color.parseColor("#F44336"));
        }
        if(dayItem.getDay()<=7){
            holder.topLine.setBackgroundColor(Color.parseColor("#F44336"));
        }else{
            holder.topLine.setBackgroundColor(Color.parseColor("#999999"));
        }
        if(dayItem.getDayOfWeek() == 1){
            holder.wholeItemView.setBackgroundColor(Color.parseColor("#FAEFEF"));
        }else if(dayItem.getDayOfWeek() == 7){
            holder.wholeItemView.setBackgroundColor(Color.parseColor("#EEF3F9"));
        }else{
            holder.wholeItemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if(dayItem.isCurMonth()){
            if(dayItem.isToday()){
                holder.tvDay.setTextColor(Color.parseColor("#FFFFFF"));
                holder.dayBg.setVisibility(View.VISIBLE);
            }else{
                holder.tvDay.setTextColor(Color.parseColor("#000000"));
                holder.dayBg.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.tvDay.setTextColor(Color.parseColor("#BDBDBD"));
            holder.dayBg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }
}
