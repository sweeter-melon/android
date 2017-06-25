package com.mryhc.calendarpro.bean;

/**
 * Created by mryhc on 2017/6/17.
 */

public class DayHourItemBean {
    private int hour;
    private boolean first;
    private boolean last;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
