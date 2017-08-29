package com.mryhc.app.calendar.entity;

/**
 * Created by mryhc on 2017/7/18.
 * Description: 事件的java bean
 */

public class EventBean {
    private int id;             // 事件 id
    private String title;       // 事件title
    private String content;     // 事件内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
