package com.mryhc.app.calendar.application;

import android.app.Application;

import com.mryhc.app.calendar.db.DBUtils;

/**
 * Created by mryhc on 2017/7/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBUtils.instance(getApplicationContext());
    }
}
