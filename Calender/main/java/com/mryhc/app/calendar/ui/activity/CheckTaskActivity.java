package com.mryhc.app.calendar.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mryhc.app.calendar.R;

/**
 * Created by mryhc on 2017/7/19.
 * Description: 查看任务列表
 */

public class CheckTaskActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected int initLayout() {
        return R.layout.activity_check_task;
    }

    @Override
    protected void initView() {
        initToolBar();
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_check_task_toolbar);
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
}
