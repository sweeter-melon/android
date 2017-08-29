package com.mryhc.app.calendar.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.mryhc.app.calendar.entity.MyDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mryhc on 2017/7/5.
 * App开发中的工具类
 */

public class AppUtils {

    private static StringBuilder stringBuilder = new StringBuilder();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    private static SimpleDateFormat sdfAllDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat sdfNormal = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    /**
     * 将dp转换为px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale + 0.5f);
    }

    /**
     *
     * @param hour
     * @return
     */
    public static String getHourText(int hour){
        stringBuilder.setLength(0);
        if(hour < 10){
            stringBuilder.append(0);
        }
        stringBuilder.append(hour);
        stringBuilder.append(":00");

        return stringBuilder.toString();
    }

    /**
     * 获取当前时间（yyyyMMddHHmmss）
     * @return
     */
    public static String getCurrentTimeString(){
        return sdf.format(new Date());
    }

    /**
     * 根据时间字符串获取Calendar
     * @param time
     * @return
     */
    public static Calendar getTimeCalendar(String time) {
        Date timeDate = null;
        try {
            timeDate = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        if (timeDate != null) {
            calendar.setTime(timeDate);
        }

        return calendar;
    }

    /**
     * 将时间字符串转换成MyDate
     * @param time
     * @return
     */
    public static MyDate timeToMyDate(String time){
        Calendar calendar = getTimeCalendar(time);
        MyDate myDate = new MyDate();
        myDate.setYear(calendar.get(Calendar.YEAR));
        myDate.setMonth(calendar.get(Calendar.MONTH));
        myDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        myDate.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        myDate.setMinute(calendar.get(Calendar.MINUTE));
        myDate.setSecond(calendar.get(Calendar.SECOND));

        return myDate;
    }

    /**
     * 获取提醒的创建时间
     * @param createDate
     * @return
     */
    public static String getNoticeCreateTimeStr(MyDate createDate){
        Calendar today = Calendar.getInstance();
        stringBuilder.setLength(0);
        int year = createDate.getYear();
        if(year != (today.get(Calendar.YEAR))){
            stringBuilder.append(year).append("年");
        }

        stringBuilder.append(createDate.getMonth() + 1).append("月");
        stringBuilder.append(createDate.getDay()).append("日");

        return stringBuilder.toString();
    }

    /**
     * 截取时间字符串获取年
     * @param timeString
     * @return
     */
    public static String timeStringToYear(String timeString){
        return timeString.substring(0, 4);
    }

    /**
     * 截取时间字符串获取月
     * @param timeString
     * @return
     */
    public static String timeStringToMonth(String timeString){
        return timeString.substring(4, 6);
    }

    /**
     * 截取时间字符串获取年日
     * @param timeString
     * @return
     */
    public static String timeStringToDay(String timeString){
        return timeString.substring(6, 8);
    }


    /**
     * 将时间字符串转化成long
     * @param time
     * @return
     */
    public static long timeStrToLong(String time){
        Date date = null;
        try{
            if(time.length() == 10){
                date = sdfAllDay.parse(time);
            }else{
                date = sdfNormal.parse(time);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        long result = -1;

        if(date != null){
            result = date.getTime();
        }

        return result;
    }

    /**
     * long转换成mydate
     *
     * @param time
     * @return
     */
    public static MyDate long2MyDate(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        MyDate myDate = new MyDate();
        myDate.setYear(calendar.get(Calendar.YEAR));
        myDate.setMonth(calendar.get(Calendar.MONTH) + 1);
        myDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        myDate.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        myDate.setMinute(calendar.get(Calendar.MINUTE));

        return myDate;
    }
}
