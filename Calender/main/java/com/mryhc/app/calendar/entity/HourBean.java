package com.mryhc.app.calendar.entity;

/**
 * Created by mryhc on 2017/7/5.
 * 小时类，用于在日视图中显示时间背景
 */

public class HourBean {
    private boolean zero;
    private int hour;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isZero() {
        return zero;
    }

    public void setZero(boolean zero) {
        this.zero = zero;
    }
}
