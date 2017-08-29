package com.mryhc.app.calendar.entity;

/**
 * Created by mryhc on 2017/7/9.
 */

public class DayInWeekBean {

    private int weekNum;
    private String dateStr;
    private MyDate myDate;

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public MyDate getMyDate() {
        return myDate;
    }

    public void setMyDate(MyDate myDate) {
        this.myDate = myDate;
    }
}
