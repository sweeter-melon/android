package com.mryhc.app.calendar.entity;

import com.mryhc.app.calendar.utils.AppUtils;

/**
 * Created by mryhc on 2017/7/30.
 * 日程实体
 */

public class EventEntity {
    private int id0;
    private String detail;
    private String desc;
    private long startTime;
    private long endTime;
    private int allDay;
    private int color;
    private int attachId;

    private MyDate startDate;
    private MyDate endDate;

    public int getId0() {
        return id0;
    }

    public void setId0(int id0) {
        this.id0 = id0;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        setStartDate(AppUtils.long2MyDate(startTime));
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        setEndDate(AppUtils.long2MyDate(endTime));
    }

    public int getAllDay() {
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAttachId() {
        return attachId;
    }

    public void setAttachId(int attachId) {
        this.attachId = attachId;
    }

    public MyDate getStartDate() {
        return startDate;
    }

    public void setStartDate(MyDate startDate) {
        this.startDate = startDate;
    }

    public MyDate getEndDate() {
        return endDate;
    }

    public void setEndDate(MyDate endDate) {
        this.endDate = endDate;
    }
}
