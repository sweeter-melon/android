package com.mryhc.calendarpro.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by mryhc on 2017/6/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initData();
        initView();
    }

    /**
     * 初始化布局
     * @return
     */
    protected abstract int initLayout();

    /**
     * 初始化数据，成员变量初始化 获取intent等等
     */
    protected abstract void initData();

    /**
     * 初始化布局
     */
    protected abstract void initView();



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( null != this.getCurrentFocus() ){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示toast
     * @param msg
     * @param isLong
     */
    public void showToast(String msg, boolean isLong) {
        if(isLong){
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public int getScreenWidth(){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();// 测量高度
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public int getScrrenHeight(){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();// 测量高度
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

}
