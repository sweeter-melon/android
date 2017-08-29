package com.mryhc.app.calendar.db;

/**
 * Created by mryhc on 2017/7/21.
 * Description：构造SQL语句的工厂类
 */

public class SQLFactory {

    public static final String DB_NAME = "CalendarPro.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME_NOTICE = "TableNotice";
    public static final String TABLE_NAME_EVENT = "TableEvent";
    public static final String TABLE_NAME_ATTACH = "TableEventAttach";

    public static final String NOTICE_ID0 = "id0";
    public static final String NOTICE_DETAIL = "detail";
    public static final String NOTICE_CREATE_TIME = "create_time";
    public static final String NOTICE_HIDE_STATE = "hide_state";

    public static final String EVENT_ID0 = "id0";
    public static final String EVENT_DETAIL = "detail";
    public static final String EVENT_DESC = "desc";
    public static final String EVENT_START_TIME = "start_time";
    public static final String EVENT_END_TIME = "end_time";
    public static final String EVENT_ALL_DAY = "all_day";
    public static final String EVENT_COLOR = "color";
    public static final String EVENT_ATTACH_ID = "attach_id";

    public static final String ATTACH_ID0 = "id0";
    public static final String ATTACH_PATH = "path";
    public static final String ATTACH_ATTACH_TYPE = "attach_type";

    /**
     * 创建提醒表
     * @return
     */
    public static String getCreateNoticeTableSql(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE TableNotice ( ");
        stringBuilder.append("id0 INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuilder.append("detail TEXT, ");
        stringBuilder.append("create_time TEXT, ");
        stringBuilder.append("hide_state INTEGER )");

        return stringBuilder.toString();
    }

    /**
     * 创建事件表
     * @return
     */
    public static String getCreateEventTableSql(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE TableEvent ( ");
        stringBuilder.append("id0 INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuilder.append("detail TEXT, ");
        stringBuilder.append("desc TEXT, ");
        stringBuilder.append("start_time INTEGER, ");
        stringBuilder.append("end_time INTEGER, ");
        stringBuilder.append("all_day INTEGER, ");
        stringBuilder.append("color INTEGER, ");
        stringBuilder.append("attach_id INTEGER)");

        return stringBuilder.toString();
    }

    /**
     * 事件附件表
     * @return
     */
    public static String getCreateEventAttachTableSql(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE TableEventAttach ( ");
        stringBuilder.append("id0 INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuilder.append("path TEXT, ");
        stringBuilder.append("attach_type INTEGER)");

        return stringBuilder.toString();
    }


}
