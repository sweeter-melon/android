package com.mryhc.app.calendar.ui.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.db.DBUtils;
import com.mryhc.app.calendar.entity.EventEntity;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mryhc on 2017/7/19.
 */

public class AddEventActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText etDetail;
    private LinearLayout llStart;
    private LinearLayout llEnd;
    private LinearLayout llAllDay;
    private TextView tvStart;
    private TextView tvEnd;
    private Switch allDaySwitch;

    private ImageView okView;

    private TimePickerView allDayTimePicker;
    private TimePickerView timePicker;

    private boolean isAllDayEvent;
    private boolean isSetStart;

    private long startTime;
    private long endTime;

    private SimpleDateFormat sdfAllDay;
    private SimpleDateFormat sdf;

    @Override
    protected int initLayout() {
        return R.layout.activity_add_event;
    }

    @Override
    protected void initView() {
        isAllDayEvent = true;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdfAllDay = new SimpleDateFormat("yyyy-MM-dd");
        initTimePicker();
        initToolBar();
        etDetail = (EditText) findViewById(R.id.add_event_et_detail);
        llStart = (LinearLayout) findViewById(R.id.add_event_ll_start);
        llEnd = (LinearLayout) findViewById(R.id.add_event_ll_end);
        llAllDay = (LinearLayout) findViewById(R.id.add_event_ll_all_day);
        tvStart = (TextView) findViewById(R.id.add_event_tv_start);
        tvEnd = (TextView) findViewById(R.id.add_event_tv_end);
        allDaySwitch = (Switch) findViewById(R.id.add_event_switch_all_day);
        okView = (ImageView) findViewById(R.id.add_event_ok);

        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAllDayEvent = isChecked;
            }
        });

        String timeStr = sdfAllDay.format(new Date());
        tvStart.setText(timeStr);
        tvEnd.setText(timeStr);

        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        llAllDay.setOnClickListener(this);
        okView.setOnClickListener(this);
    }

    private void initTimePicker(){

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        final Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        final Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);

        allDayTimePicker = new TimePickerView.Builder(AddEventActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if(isSetStart){
                    startTime = calendar.getTimeInMillis();
                    tvStart.setText(sdfAllDay.format(date));
                }else {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    endTime = calendar.getTimeInMillis();
                    tvEnd.setText(sdfAllDay.format(date));
                }
            }
        }).setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allDayTimePicker.returnData();
                                allDayTimePicker.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allDayTimePicker.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();



        timePicker = new TimePickerView.Builder(AddEventActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(isSetStart){
                    startTime = date.getTime();
                    tvStart.setText(sdf.format(date));
                }else {
                    endTime = date.getTime();
                    tvEnd.setText(sdf.format(date));
                }
            }
        }).setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePicker.returnData();
                                timePicker.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePicker.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_add_event_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        closeKeyBoard();
        switch (v.getId()){
            case R.id.add_event_ll_start:
                showTimePicker(true);
                break;
            case R.id.add_event_ll_end:
                showTimePicker(false);
                break;
            case R.id.add_event_ll_all_day:
                if(allDaySwitch.isChecked()){
                    allDaySwitch.setChecked(false);
                }else {
                    allDaySwitch.setChecked(true);
                }
                break;
            case R.id.add_event_ok:
                addEvent();
                break;
        }
    }

    /**
     * 显示时间选择
     * @param isStart
     */
    private void showTimePicker(boolean isStart){
        isSetStart = isStart;
        if(isAllDayEvent){
            allDayTimePicker.show();
        }else{
            timePicker.show();
        }
    }

    /**
     * 添加事件
     */
    private void addEvent(){
        String detail = etDetail.getText().toString();
        if(detail.length() == 0){
            showToast("内容不能为空!", false);
            return ;
        }else{
            EventEntity eventEntity = new EventEntity();
            eventEntity.setDetail(detail);
            MyLog.i(Common.TAG, "addEvent startTime" + startTime);
            eventEntity.setStartTime(startTime);
            eventEntity.setEndTime(endTime);
            eventEntity.setColor(Color.parseColor("#0099ff"));
            eventEntity.setAllDay(isAllDayEvent ? 1 : 0);
            eventEntity.setAttachId(-1);
            eventEntity.setDesc("");
            DBUtils.insertEvent(eventEntity);
            showToast("添加成功", false);
            setResult(RESULT_OK);
            finish();
        }
    }
}
