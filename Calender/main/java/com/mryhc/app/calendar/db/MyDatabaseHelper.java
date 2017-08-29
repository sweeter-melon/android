package com.mryhc.app.calendar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

/**
 * Created by mryhc on 2017/7/21.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static MyDatabaseHelper myDatabaseHelper = null;

    private MyDatabaseHelper(Context context) {
        super(context, SQLFactory.DB_NAME, null, SQLFactory.DB_VERSION);
        this.context = context;
    }

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static MyDatabaseHelper getInstance(Context context){

        if(myDatabaseHelper == null){
            synchronized (MyDatabaseHelper.class){
                if(myDatabaseHelper == null){
                    myDatabaseHelper = new MyDatabaseHelper(context);
                }
            }
        }

        return myDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLFactory.getCreateNoticeTableSql());
        db.execSQL(SQLFactory.getCreateEventTableSql());
        db.execSQL(SQLFactory.getCreateEventAttachTableSql());
        MyLog.i(Common.TAG, "create table success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
