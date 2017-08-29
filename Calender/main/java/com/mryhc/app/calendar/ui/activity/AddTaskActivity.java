package com.mryhc.app.calendar.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.ui.custom.SwipeLayout;

/**
 * Created by mryhc on 2017/7/19.
 */

public class AddTaskActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected int initLayout() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void initView() {
        initToolBar();
        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipe_layout);
        TextView tvHide = (TextView) swipeLayout.findViewById(R.id.tv_hide);
        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("隐藏", false);
            }
        });
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_add_task_toolbar);
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
