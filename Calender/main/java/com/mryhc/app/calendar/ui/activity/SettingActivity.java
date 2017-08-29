package com.mryhc.app.calendar.ui.activity;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.utils.Common;

public class SettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener{
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMonday;
    private RadioButton radioButtonSunday;
    private Switch showWeekNumSwitch;
    private SharedPreferences sharedPreferences;

    private boolean showWeekNum;
    private int weekStartNum;

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initData();
        initToolBar();
        radioGroup = (RadioGroup) findViewById(R.id.setting_radio_group);
        radioButtonMonday = (RadioButton) findViewById(R.id.setting_radio_monday);
        radioButtonSunday = (RadioButton) findViewById(R.id.setting_radio_sunday);
        showWeekNumSwitch = (Switch) findViewById(R.id.setting_show_week_num_switch);

        if(weekStartNum == 1){
            radioButtonMonday.setChecked(true);
        }else{
            radioButtonSunday.setChecked(true);
        }
        showWeekNumSwitch.setChecked(showWeekNum);

        radioGroup.setOnCheckedChangeListener(this);
        showWeekNumSwitch.setOnCheckedChangeListener(this);
    }

    private void initData(){
        sharedPreferences = getSharedPreferences(Common.SP_NAME, MODE_PRIVATE);
        showWeekNum = sharedPreferences.getBoolean(Common.SHOW_WEEK_NUM, false);
        weekStartNum = sharedPreferences.getInt(Common.WEEK_START,0);
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
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
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.setting_radio_monday:
                setWeekStart(true);
                break;
            case R.id.setting_radio_sunday:
                setWeekStart(false);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.setting_show_week_num_switch:
                sharedPreferences.edit().putBoolean(Common.SHOW_WEEK_NUM, isChecked).commit();
                setResult(RESULT_OK);
                break;
        }
    }

    private void setWeekStart(boolean isMonday){
        sharedPreferences.edit().putInt(Common.WEEK_START, isMonday ? 1 : 7).commit();
        setResult(RESULT_OK);
    }
}
