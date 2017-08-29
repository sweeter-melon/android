package com.mryhc.app.calendar.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.ui.activity.MainActivity;

/**
 * Created by mryhc on 2017/6/28.
 */

public class DialogChangeView extends Dialog implements View.OnClickListener {

    private MainActivity activity;

    private OnItemSelectedListener listener;

    private LinearLayout llDay;
    private LinearLayout llWeek;
    private LinearLayout llColumnWeek;
    private LinearLayout llMonth;
    private LinearLayout llYear;

    public DialogChangeView(MainActivity activity, OnItemSelectedListener listener){
        super(activity, R.style.MyDialogTheme);
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_view);
        initSize();
        initView();
    }

    private void initView(){
        llDay = (LinearLayout) findViewById(R.id.dialog_day);
        llWeek = (LinearLayout) findViewById(R.id.dialog_week);
        llColumnWeek = (LinearLayout) findViewById(R.id.dialog_column_week);
        llMonth = (LinearLayout) findViewById(R.id.dialog_month);
        llYear = (LinearLayout) findViewById(R.id.dialog_year);

        llDay.setOnClickListener(this);
        llWeek.setOnClickListener(this);
        llColumnWeek.setOnClickListener(this);
        llMonth.setOnClickListener(this);
        llYear.setOnClickListener(this);
    }

    private void initSize(){
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        dialogWindow.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int screenWidth = activity.getScreenWidth();
//        lp.x = 20;
        lp.y = activity.getBottomLayoutHeight() + 10;
        lp.width = (int)(screenWidth * 0.33);
        dialogWindow.setAttributes(lp);
    }

    public interface OnItemSelectedListener{
        void onSelect(int flag);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        switch (v.getId()){
            case R.id.dialog_day:
                listener.onSelect(MainActivity.FLAG_DAY);
                break;
            case R.id.dialog_week:
                listener.onSelect(MainActivity.FLAG_WEEK);
                break;
            case R.id.dialog_column_week:
                listener.onSelect(MainActivity.FLAG_COLUMN_WEEK);
                break;
            case R.id.dialog_month:
                listener.onSelect(MainActivity.FLAG_MONTH);
                break;
            case R.id.dialog_year:
                listener.onSelect(MainActivity.FLAG_YEAR);
                break;
        }
    }
}
