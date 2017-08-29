package com.mryhc.app.calendar.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/8/2.
 */

public class DayViewTimeQuantum {
    private List<EventEntity> eventEntityList;
    private long startTime;
    private long endTime;
    private int viewWidth;
    private int wholeWidth;

    public DayViewTimeQuantum(int wholeWidth){
        eventEntityList = new ArrayList<>();
        startTime = 0;
        endTime = 0;
        this.wholeWidth = wholeWidth - 20;
    }

    public List<EventEntity> getEventEntityList() {
        return eventEntityList;
    }

    public void addEventEntity(EventEntity eventEntity) {
        eventEntityList.add(eventEntity);
        if(eventEntityList.size() == 0){
            startTime = eventEntity.getStartTime();
            endTime = eventEntity.getEndTime();
        }else{
            if(eventEntity.getEndTime() > endTime){
                endTime = eventEntity.getEndTime();
            }
        }
        viewWidth = wholeWidth / eventEntityList.size();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setWholeWidth(int wholeWidth1) {
        this.wholeWidth = wholeWidth1 - 20;
        if(eventEntityList.size() > 0){
            viewWidth = wholeWidth / eventEntityList.size();
        }
    }
}
