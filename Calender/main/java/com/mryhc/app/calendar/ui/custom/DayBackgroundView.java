package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.entity.HourBean;

import java.util.List;

/**
 * Created by mryhc on 2017/7/5.
 */

public class DayBackgroundView extends LinearLayout implements View.OnLongClickListener{

    private List<HourBean> hourBeanList;
    private LayoutInflater inflater;
    private OnItemLongClickListener listener;

    public DayBackgroundView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    public DayBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    public DayBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    private void refreshView(){
        this.removeAllViews();
        for(int i=0; i<hourBeanList.size(); i++){
            View itemView = inflater.inflate(R.layout.hour_item_layout, this, false);
            itemView.setTag(R.id.view_tag_01, i);

            TextView tvHour = (TextView) itemView.findViewById(R.id.hour_item_tv_time);
            View topLine = itemView.findViewById(R.id.hour_item_top_line);
            if(hourBeanList.get(i).isZero()){
                topLine.setVisibility(VISIBLE);
            }else{
                topLine.setVisibility(INVISIBLE);
            }
            tvHour.setText(hourBeanList.get(i).getHour() < 10 ? "0" + hourBeanList.get(i).getHour() : "" + hourBeanList.get(i).getHour());
            itemView.setOnLongClickListener(this);
            this.addView(itemView);
        }
        requestLayout();//重新绘制布局
        invalidate();//刷新当前界面
    }

    public List<HourBean> getHourBeanList() {
        return hourBeanList;
    }

    public void setHourBeanList(List<HourBean> hourBeanList) {
        this.hourBeanList = hourBeanList;
        refreshView();

    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.listener = listener;
    }

    public interface OnItemLongClickListener{
        void onLongClick(int position);
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.hour_item_view){
            listener.onLongClick((int)(v.getTag(R.id.view_tag_01)));
        }
        return true;
    }
}
