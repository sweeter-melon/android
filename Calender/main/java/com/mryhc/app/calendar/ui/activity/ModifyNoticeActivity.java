package com.mryhc.app.calendar.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.db.DBUtils;

/**
 * Created by mryhc on 2017/7/19.
 */

public class ModifyNoticeActivity extends BaseActivity {
    private Toolbar toolbar;

    private EditText editText;
    private Button btnModify;
    private int noticeId;
    private String noticeDetail;

    @Override
    protected int initLayout() {
        return R.layout.activity_modify_notify;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        noticeId = intent.getIntExtra("notice_id", -1);
        noticeDetail = intent.getStringExtra("notice_detail");

        initToolBar();
        editText = (EditText) findViewById(R.id.modify_notice_edit_text);
        btnModify = (Button) findViewById(R.id.modify_notice_btn);

        editText.setText(noticeDetail);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if(str.length() != 0){
                    DBUtils.modifyNoticeById(noticeId, editText.getText().toString());
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_modify_notice_toolbar);
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
