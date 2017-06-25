package com.mryhc.calendarpro.utils;

import android.util.Log;

/**
 * Created by mryhc on 2017/5/20.
 */

public class MyLog {
    private static boolean IS_DEBUG = true;
    public static void i(String tag, String msg){
        if(IS_DEBUG){
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(IS_DEBUG){
            Log.e(tag, msg);
        }
    }
}
