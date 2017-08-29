package com.mryhc.app.calendar.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.adapter.CommonAdapter;
import com.mryhc.app.calendar.adapter.ViewHolder;
import com.mryhc.app.calendar.db.DBUtils;
import com.mryhc.app.calendar.entity.NoticeEntity;
import com.mryhc.app.calendar.ui.activity.MainActivity;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/7/20.
 */

public class NoticeDialog extends Dialog implements View.OnClickListener {
    private MainActivity activity;
    private LayoutInflater inflater;
    private LinearLayout wholeView;
    private LinearLayout addNoticeView;
    private ImageView removeView;

    private ListView listView;

    private EditText etAddNotice;

    private Window dialogWindow;

    private WindowManager.LayoutParams dialogLayoutParams;

    private int maxHeight;

    private CommonAdapter<NoticeEntity> adapter;
    private List<NoticeEntity> dataList;

    private boolean doRemove;

    public NoticeDialog(MainActivity activity) {
        super(activity, R.style.MyDialogTheme);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
        wholeView = (LinearLayout) findViewById(R.id.dialog_notice_view);
        addNoticeView = (LinearLayout) inflater.inflate(R.layout.edit_for_notice, null);
        ImageView addView = (ImageView) findViewById(R.id.dialog_notice_add);
        removeView = (ImageView) findViewById(R.id.dialog_notice_remove);

        ImageView addNotice = (ImageView) addNoticeView.findViewById(R.id.notice_add_notice);
        etAddNotice = (EditText) addNoticeView.findViewById(R.id.notice_edit);

        addView.setOnClickListener(this);
        removeView.setOnClickListener(this);
        addNotice.setOnClickListener(this);
        initSize();
        wholeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                MyLog.i(Common.TAG, "dialog height -> " + wholeView.getMeasuredHeight());

                if(wholeView.getMeasuredHeight() > maxHeight){
                    resetViewSize();
                }
            }
        });
        doRemove = false;
        initListView();
    }

    private void initListView(){
        listView = (ListView) findViewById(R.id.dialog_notice_list_view);
        dataList = new ArrayList<>();
        adapter = new CommonAdapter<NoticeEntity>(activity, dataList, R.layout.notice_item_layout) {
            @Override
            protected void convert(ViewHolder viewHolder, int pos, NoticeEntity item) {
                viewHolder.setText(R.id.notice_item_num, String.valueOf(pos + 1));
                viewHolder.setText(R.id.notice_item_content, item.getDetail());
                if(doRemove){
                    viewHolder.setImageVisiable(R.id.notice_item_delete, View.VISIBLE);
                    viewHolder.setViewPosTag(R.id.notice_item_delete, pos);
                    viewHolder.setViewClickListener(R.id.notice_item_delete, NoticeDialog.this);

                }else{
                    viewHolder.setImageVisiable(R.id.notice_item_delete, View.GONE);
                }
            }
        };
        listView.setAdapter(adapter);
        refreshData();
    }

    private void resetViewSize(){
        dialogLayoutParams.height = maxHeight;
        dialogWindow.setAttributes(dialogLayoutParams);
    }

    private void initSize(){
        dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        dialogWindow.setWindowAnimations(R.style.NoticeDialogAnim);
        dialogLayoutParams = dialogWindow.getAttributes();
        int screenWidth = activity.getScreenWidth();

        maxHeight = activity.getScreenHeight() * 1 / 2;
        dialogLayoutParams.width = (int)(screenWidth * 0.66);
        dialogLayoutParams.y = activity.getBottomLayoutHeight() + 10;

        dialogWindow.setAttributes(dialogLayoutParams);
    }

    private void includeAddNoticeView(){
        if(wholeView.getChildAt(3) == null){
            wholeView.addView(addNoticeView);
        }
    }

    private void removeAddNoticeView(){
        wholeView.removeView(addNoticeView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_notice_add:
                includeAddNoticeView();
                break;
            case R.id.dialog_notice_remove:
                startRemove();
                break;
            case R.id.notice_add_notice:
                addNoticeOperation();
                break;
            case R.id.notice_item_delete:
                removeNoticeOperation(v);
                break;
        }
    }

    private void addNoticeOperation(){
        String notice = etAddNotice.getText().toString();
        if(notice.length() == 0){
            activity.showToast("新增提醒内容不能为空", false);
            return ;
        }

        if(!DBUtils.insertNotice(notice)){
            activity.showToast("添加提醒失败", false);
            return;
        }
        closeKeyBoard();
        removeAddNoticeView();
        etAddNotice.setText("");
        refreshData();
    }

    private void removeNoticeOperation(View v){
        int pos = (int)(v.getTag(R.id.view_tag_01));
        MyLog.i(Common.TAG, "remove item pos -> " + pos);
        if(DBUtils.removeNoticeById(dataList.get(pos).getId0())){
            refreshData();
        }else{
            activity.showToast("删除失败", true);
        }
    }

    private void startRemove(){
        if(doRemove){
            doRemove = false;
            removeView.setImageResource(R.mipmap.ic_remove);
        }else{
            doRemove = true;
            removeView.setImageResource(R.mipmap.ic_back_gray);
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshData(){
        List<NoticeEntity> tempList = DBUtils.getNoticeList();
        dataList.clear();
        for(NoticeEntity item : tempList){
            dataList.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void closeKeyBoard(){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
