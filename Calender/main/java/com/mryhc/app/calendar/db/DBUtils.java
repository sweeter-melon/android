package com.mryhc.app.calendar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mryhc.app.calendar.entity.EventEntity;
import com.mryhc.app.calendar.entity.NoticeEntity;
import com.mryhc.app.calendar.utils.AppUtils;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/7/21.
 * Description：操作数据库工具， 增删改查等
 */

public class DBUtils {

    private static MyDatabaseHelper myDatabaseHelper = null;

    public static void instance(Context context){
        DBUtils.myDatabaseHelper = MyDatabaseHelper.getInstance(context);
    }

    /**
     * 检查数据库实例是否存在
     * @return
     */
    public static boolean  checkHelper(){
        if(myDatabaseHelper == null){
            MyLog.i(Common.TAG, "myDatabaseHelper 没有被初始化， 请调用DBUtils.instance(context) 进行初始化");
            return false;
        }
        return true;
    }

    /**
     * 添加提醒
     * @param noticeDetail
     * @return
     */
    public static boolean insertNotice(String noticeDetail){
        if(!checkHelper()){
            return false;
        }

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLFactory.NOTICE_DETAIL, noticeDetail);
        cv.put(SQLFactory.NOTICE_CREATE_TIME, AppUtils.getCurrentTimeString());
        cv.put(SQLFactory.NOTICE_HIDE_STATE, 0);
        db.insert(SQLFactory.TABLE_NAME_NOTICE, null, cv);
        db.close();

        return true;
    }

    /**
     * 获取提醒列表
     * @return
     */
    public static List<NoticeEntity> getNoticeList(){
        if(!checkHelper()){
            return null;
        }

        List<NoticeEntity> dataList = new ArrayList<>();
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLFactory.TABLE_NAME_NOTICE, null, SQLFactory.NOTICE_HIDE_STATE + " = ?", new String[]{"0"}, null, null, SQLFactory.NOTICE_CREATE_TIME + " desc");
        if(cursor.moveToFirst()){
            do {
                NoticeEntity noticeEntity = new NoticeEntity();
                noticeEntity.setId0(cursor.getInt(cursor.getColumnIndex(SQLFactory.NOTICE_ID0)));
                noticeEntity.setDetail(cursor.getString(cursor.getColumnIndex(SQLFactory.NOTICE_DETAIL)));
                noticeEntity.setCreateTime(AppUtils.timeToMyDate(cursor.getString(cursor.getColumnIndex(SQLFactory.NOTICE_CREATE_TIME))));
                noticeEntity.setHideState(cursor.getInt(cursor.getColumnIndex(SQLFactory.NOTICE_HIDE_STATE)));
                dataList.add(noticeEntity);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return dataList;
    }

    /**
     * 删除提醒
     * @param id0
     * @return
     */
    public static boolean removeNoticeById(int id0){
        if(!checkHelper()){
            return false;
        }

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.delete(SQLFactory.TABLE_NAME_NOTICE, SQLFactory.NOTICE_ID0 + " = ? ", new String[]{String.valueOf(id0)});
        db.close();

        return true;
    }

    /**
     * 批量删除提醒
     * @param id0List
     * @return
     */
    public static boolean batchRemoveNoticeById(List<Integer> id0List){
        if(!checkHelper()){
            return false;
        }

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        for(int id0 : id0List){
            db.delete(SQLFactory.TABLE_NAME_NOTICE, SQLFactory.NOTICE_ID0 + " = ? ", new String[]{String.valueOf(id0)});
        }
        db.close();

        return true;
    }

    /**
     * 隐藏提醒
     * @param id0
     * @return
     */
    public static boolean hideNoticeById(int id0){
        if(!checkHelper()){
            return false;
        }

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLFactory.NOTICE_HIDE_STATE, 1);
        db.update(SQLFactory.TABLE_NAME_NOTICE, cv, SQLFactory.NOTICE_ID0 + " = ? ", new String[]{String.valueOf(id0)});
        db.close();

        return true;
    }

    /**
     * 修改提醒
     * @param id0
     * @param detail
     * @return
     */
    public static boolean modifyNoticeById(int id0, String detail){
        if(!checkHelper()){
            return false;
        }

        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLFactory.NOTICE_DETAIL, detail);
        db.update(SQLFactory.TABLE_NAME_NOTICE, cv, SQLFactory.NOTICE_ID0 + " = ? ", new String[]{String.valueOf(id0)});
        db.close();

        return true;
    }


    public static boolean insertEvent(EventEntity event){
        if(!checkHelper()){
            return false;
        }
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLFactory.EVENT_DETAIL, event.getDetail());
        cv.put(SQLFactory.EVENT_DESC, event.getDesc());
        cv.put(SQLFactory.EVENT_START_TIME, event.getStartTime());
        cv.put(SQLFactory.EVENT_END_TIME, event.getEndTime());
        cv.put(SQLFactory.EVENT_ALL_DAY, event.getAllDay());
        cv.put(SQLFactory.EVENT_COLOR, event.getColor());
        cv.put(SQLFactory.EVENT_ATTACH_ID, event.getAttachId());
        db.insert(SQLFactory.TABLE_NAME_EVENT, null, cv);
        db.close();
        return true;
    }

    public static List<EventEntity> getEventByDate(long start, long end){
        if(!checkHelper()){
            return null;
        }

        List<EventEntity> eventEntityList = new ArrayList<>();
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query(SQLFactory.TABLE_NAME_EVENT, null, SQLFactory.EVENT_START_TIME + "  >= ? and "+SQLFactory.EVENT_START_TIME+" < ? ",
                new String[]{String.valueOf(start), String.valueOf(end)},
                null, null, SQLFactory.EVENT_START_TIME);
        if(cursor.moveToFirst()){
            do{
                EventEntity eventEntity = new EventEntity();
                eventEntity.setDetail(cursor.getString(cursor.getColumnIndex(SQLFactory.EVENT_DETAIL)));
                eventEntity.setStartTime(cursor.getLong(cursor.getColumnIndex(SQLFactory.EVENT_START_TIME)));
                eventEntity.setEndTime(cursor.getLong(cursor.getColumnIndex(SQLFactory.EVENT_END_TIME)));
                eventEntity.setAllDay(cursor.getInt(cursor.getColumnIndex(SQLFactory.EVENT_ALL_DAY)));
                eventEntity.setColor(cursor.getInt(cursor.getColumnIndex(SQLFactory.EVENT_COLOR)));
                eventEntity.setDesc(cursor.getString(cursor.getColumnIndex(SQLFactory.EVENT_DESC)));
                eventEntity.setAttachId(cursor.getInt(cursor.getColumnIndex(SQLFactory.EVENT_ATTACH_ID)));
                eventEntityList.add(eventEntity);
            }while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return eventEntityList;
    }

}


