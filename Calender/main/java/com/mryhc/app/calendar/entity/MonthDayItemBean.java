package com.mryhc.app.calendar.entity;

/**
 * Created by mryhc on 2017/6/17.
 */

public class MonthDayItemBean {

    private int year;

    private int month;

    private int day;

    private int dayOfWeek;

    private boolean curMonth;

    private boolean today;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCurMonth() {
        return curMonth;
    }

    public void setCurMonth(boolean curMonth) {
        this.curMonth = curMonth;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
