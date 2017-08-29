package com.mryhc.app.calendar.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.RecyclerWithSwipeAdapter;
import com.mryhc.app.calendar.db.DBUtils;
import com.mryhc.app.calendar.entity.NoticeEntity;
import com.mryhc.app.calendar.io.Attributes;
import com.mryhc.app.calendar.ui.custom.MyRecyclerView;
import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mryhc on 2017/7/19.
 */

public class AddNoticeActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private TextView titleView;
    private LinearLayout llOperate;
    private TextView tvDelete;

    private ImageView removeView;
    private ImageView addView;
    private LinearLayout addLayout;
    private EditText editText;
    private ImageView okView;

    private MyRecyclerView recyclerView;

    private RecyclerWithSwipeAdapter<NoticeEntity> adapter;
    private List<NoticeEntity> noticeEntityList;
    private List<Integer> selectPosList;

    private boolean removing;   // 进行删除操作

    @Override
    protected int initLayout() {
        return R.layout.activity_add_notice;
    }

    @Override
    protected void initView() {
        initToolBar();

        removing = false;

        titleView = (TextView) findViewById(R.id.add_notice_title_view);
        llOperate = (LinearLayout) findViewById(R.id.add_notice_ll_operate);
        tvDelete = (TextView) findViewById(R.id.add_notice_tv_delete);

        removeView = (ImageView) findViewById(R.id.add_notice_remove_view);
        addView = (ImageView) findViewById(R.id.add_notice_add_view);
        okView = (ImageView) findViewById(R.id.notice_add_ok_view);
        editText = (EditText) findViewById(R.id.notice_add_edit_text);
        addLayout = (LinearLayout) findViewById(R.id.notice_add_layout);

        removeView.setOnClickListener(this);
        addView.setOnClickListener(this);
        okView.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

        initRecyclerView();

    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_add_notice_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView(){
        noticeEntityList = new ArrayList<>();
        selectPosList = new ArrayList<>();
        recyclerView = (MyRecyclerView) findViewById(R.id.add_notice_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerWithSwipeAdapter<NoticeEntity>(noticeEntityList, R.layout.notice_item_in_swipe_layout) {
            @Override
            public void convertView(ViewHolder holder, int pos, NoticeEntity item) {
                holder.setTVText(R.id.notice_item_in_swipe_notice_content, item.getDetail());
                holder.setTVText(R.id.notice_item_in_swipe_create_time, AppUtils.getNoticeCreateTimeStr(item.getCreateTime()));
                holder.setCheckBoxCheckState(R.id.notice_item_in_swipe_check_box, item.isSelected());
                if(removing){
                    holder.setVisible(R.id.notice_item_in_swipe_check_box, View.VISIBLE);
                }else{
                    holder.setVisible(R.id.notice_item_in_swipe_check_box, View.GONE);
                }
            }
        };

        adapter.setListener(new RecyclerWithSwipeAdapter.OnOperateListener() {
            @Override
            public void delete(int pos) {
                DBUtils.removeNoticeById(noticeEntityList.get(pos).getId0());
                refreshNoticeList();
            }

            @Override
            public void hide(int pos) {
                DBUtils.hideNoticeById(noticeEntityList.get(pos).getId0());
                refreshNoticeList();
            }

            @Override
            public void clickItem(int pos) {
                NoticeEntity clickItem = noticeEntityList.get(pos);
                if(removing){
                    if(clickItem.isSelected()){
                        clickItem.setSelected(false);
                        selectPosList.remove((Object)pos);
                    }else{
                        clickItem.setSelected(true);
                        selectPosList.add(pos);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Intent intent = new Intent(AddNoticeActivity.this, ModifyNoticeActivity.class);
                    intent.putExtra("notice_id", clickItem.getId0());
                    intent.putExtra("notice_detail", clickItem.getDetail());
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void longClickItem(int pos) {
            }
        });

        adapter.setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(adapter);

        refreshNoticeList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(removing){
                    removing = false;
                    adapter.notifyDataSetChanged();
                    resetSelect();
                    titleView.setText("提醒列表");
                    llOperate.setVisibility(View.VISIBLE);
                    tvDelete.setVisibility(View.GONE);
                }else{
                    finish();
                }
                break;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_notice_remove_view:
                if(!removing){
                    removing = true;
                    adapter.notifyDataSetChanged();
                    titleView.setText("删除提醒");
                    llOperate.setVisibility(View.GONE);
                    tvDelete.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.add_notice_add_view:
                if(addLayout.getVisibility() != View.VISIBLE){
                    addLayout.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    llOperate.setVisibility(View.GONE);
                }
                break;
            case R.id.notice_add_ok_view:
                addNotice();
                refreshNoticeList();
                break;
            case R.id.add_notice_tv_delete:
                batchDelete();
                break;
        }
    }

    /**
     * 添加提醒
     */
    private void addNotice(){
        String notice = editText.getText().toString();
        if(notice.length() != 0){
            DBUtils.insertNotice(editText.getText().toString());
            editText.setText("");
            closeKeyBoard();
            addLayout.setVisibility(View.GONE);
            llOperate.setVisibility(View.VISIBLE);
        }else{
            showToast("提醒内容不能为空!", false);
        }
    }

    private void refreshNoticeList(){
        List<NoticeEntity> tempList = DBUtils.getNoticeList();
        noticeEntityList.clear();
        for(NoticeEntity noticeEntity:tempList){
            noticeEntityList.add(noticeEntity);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 批量删除
     */
    private void batchDelete(){
        removing = false;
        List<Integer> idList = new ArrayList<>();
        for(int pos : selectPosList){
            idList.add(noticeEntityList.get(pos).getId0());
        }
        DBUtils.batchRemoveNoticeById(idList);
        selectPosList.clear();
        titleView.setText("提醒列表");
        refreshNoticeList();
        llOperate.setVisibility(View.VISIBLE);
        tvDelete.setVisibility(View.GONE);
    }

    private void resetSelect(){
        for(int pos: selectPosList){
            noticeEntityList.get(pos).setSelected(false);
        }
        selectPosList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    refreshNoticeList();
                }
                break;
        }
    }
}
