package com.mryhc.app.calendar.entity;

/**
 * Created by mryhc on 2017/7/22.
 * Description：提醒的实体类
 */

public class NoticeEntity {
    private int id0;            // id
    private String detail;      // 提醒内容
    private MyDate createTime;  // 创建时间
    private int hideState;      // 隐藏状态
    private boolean selected;   // 是否选中

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

    public MyDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(MyDate createTime) {
        this.createTime = createTime;
    }

    public int getHideState() {
        return hideState;
    }

    public void setHideState(int hideState) {
        this.hideState = hideState;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
