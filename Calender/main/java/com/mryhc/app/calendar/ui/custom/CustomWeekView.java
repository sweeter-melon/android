package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.entity.DayInWeekBean;

import java.util.List;

/**
 * Created by mryhc on 2017/7/9.
 */

public class CustomWeekView extends LinearLayout implements View.OnLongClickListener {

    private List<DayInWeekBean> dayInWeekBeanList;

    private LayoutInflater inflater;

    private OnItemLongClickListener listener;

    public CustomWeekView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    public CustomWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    public CustomWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
    }

    public void setDayInWeekBeanList(List<DayInWeekBean> dayInWeekBeanList){
        this.dayInWeekBeanList = dayInWeekBeanList;
        refreshView();
    }

    private void refreshView(){
        this.removeAllViews();
        for(int i=0; i< dayInWeekBeanList.size(); i++){
            if(i==0){
                this.addView(createItemView(dayInWeekBeanList.get(i), i, true));
            }else{
                switch (i){
                    case 1:
                    case 2:
                        LinearLayout layout1 = (LinearLayout) this.getChildAt(1);

                        if(layout1 == null){
                            layout1 = (LinearLayout) inflater.inflate(R.layout.null_content_linearlayout, this, false);
                            layout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                            this.addView(layout1);
                        }
                        layout1.addView(createItemView(dayInWeekBeanList.get(i), i, false));
                        break;
                    case 3:
                    case 4:
                        LinearLayout layout2 = (LinearLayout) this.getChildAt(2);

                        if(layout2 == null){
                            layout2 = (LinearLayout) inflater.inflate(R.layout.null_content_linearlayout, this, false);
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                            this.addView(layout2);
                        }
                        layout2.addView(createItemView(dayInWeekBeanList.get(i), i, false));
                    break;
                    case 5:
                    case 6:
                        LinearLayout layout3 = (LinearLayout) this.getChildAt(3);

                        if(layout3 == null){
                            layout3 = (LinearLayout) inflater.inflate(R.layout.null_content_linearlayout, this, false);
                            layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                            this.addView(layout3);
                        }
                        layout3.addView(createItemView(dayInWeekBeanList.get(i), i, false));
                    break;
                }
            }
        }
    }

    private View createItemView(DayInWeekBean dayInWeekBean, int index, boolean isFirst){
        CustomWeekDayView customWeekDayView = (CustomWeekDayView) inflater.inflate(R.layout.custom_week_day_view, this, false);
        String numStr = "";
        switch (dayInWeekBean.getWeekNum()){
            case 1:
                numStr = "星期日";
                break;
            case 2:
                numStr = "星期一";
                break;
            case 3:
                numStr = "星期二";
                break;
            case 4:
                numStr = "星期三";
                break;
            case 5:
                numStr = "星期四";
                break;
            case 6:
                numStr = "星期五";
                break;
            case 7:
                numStr = "星期六";
                break;
        }

        customWeekDayView.setWeekNum(numStr);
        customWeekDayView.setWeekDate(dayInWeekBean.getDateStr());
        customWeekDayView.setFirstDay(isFirst);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(isFirst ? ViewGroup.LayoutParams.MATCH_PARENT : 0, isFirst ? 0:ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        lllp.setMargins(5, 5, 5, 10);
        if(isFirst){
            lllp.weight = 1.5f;
        }
        customWeekDayView.setLayoutParams(lllp);
        customWeekDayView.setTag(R.id.view_tag_01, index);
        customWeekDayView.setOnLongClickListener(this);

        return customWeekDayView;
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.custom_week_day_view){
            listener.onLongClick((int)(v.getTag(R.id.view_tag_01)));
        }
        return true;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.listener = listener;
    }

    public interface OnItemLongClickListener{
        void onLongClick(int position);
    }
}
