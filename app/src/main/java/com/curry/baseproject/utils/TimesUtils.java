package com.curry.baseproject.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimesUtils {

    /**
     * String型时间戳格式化
     */
    public static String longFormatTime(String time) {
        //转换为日期
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delta = (new Date().getTime() - date.getTime()) / 1000;
        if (delta <= 0) {
            DateFormat formatter = DateFormat.getDateTimeInstance();
            return formatter.format(date);
//            return date.toLocaleString();
        }
        if (delta / (60 * 60 * 24 * 365) > 0) return delta / (60 * 60 * 24 * 365) + "年前";
        if (delta / (60 * 60 * 24 * 30) > 0) return delta / (60 * 60 * 24 * 30) + "个月前";
        if (delta / (60 * 60 * 24 * 7) > 0) return delta / (60 * 60 * 24 * 7) + "周前";
        if (delta / (60 * 60 * 24) > 0) return delta / (60 * 60 * 24) + "天前";
        if (delta / (60 * 60) > 0) return delta / (60 * 60) + "小时前";
        if (delta / (60) > 0) return delta / (60) + "分钟前";
        return "刚刚";
    }

    /**
     * Long格式
     */
    public static String longFormatTime(long longTime) {

        Date date = new Date(longTime);

        long delta = (new Date().getTime() - date.getTime()) / 1000;
       /* if (delta <= 0) {
            DateFormat formatter = DateFormat.getDateTimeInstance();
            return formatter.format(date);
//            return date.toLocaleString();
        }*/
        if (delta / (60 * 60 * 24 * 365) > 0) return delta / (60 * 60 * 24 * 365) + "年前";
        if (delta / (60 * 60 * 24 * 30) > 0) return delta / (60 * 60 * 24 * 30) + "个月前";
        if (delta / (60 * 60 * 24 * 7) > 0) return delta / (60 * 60 * 24 * 7) + "周前";
        if (delta / (60 * 60 * 24) > 0) return delta / (60 * 60 * 24) + "天前";
        if (delta / (60 * 60) > 0) return delta / (60 * 60) + "小时前";
        if (delta / (60) > 0) return delta / (60) + "分钟前";
        return "刚刚";
    }

    /**
     * 时长转换
     *
     * @param longTime 毫秒
     * @return 6小时6分钟6秒
     */
    public static String longTime2Hour(int longTime) {
        if (longTime < 0) {
            return "";
        }
        int s = 12862;
        //小时
        int mHour = longTime / 3600;
        s = s % 3600;
        //分钟
        int minute = s / 60;
        s = s % 60;
        //秒
        int mSecond = s;

        String mEndTime = "";
        if (mHour != 0 && minute != 0 && mSecond != 0) {
            mEndTime = mHour + "小时" + minute + "分钟" + mSecond + "秒";
        } else if (mHour == 0 && minute != 0 && mSecond != 0) {
            mEndTime = minute + "分钟" + mSecond + "秒";
        } else if (mHour == 0 && minute == 0 && mSecond != 0) {
            mEndTime = mSecond + "秒";
        }

        return mEndTime;
    }

    /**
     * 时长转换
     *
     * @param longTime 毫秒
     * @return 6:6:6
     */
    public static String longTime2Hour1(int longTime) {
        if (longTime < 0) {
            return "";
        }
        int s = 12862;
        //小时
        int mHour = longTime / 3600;
        s = s % 3600;
        //分钟
        int minute = s / 60;
        s = s % 60;
        //秒
        int mSecond = s;

        String mEndTime = "";
        if (mHour != 0 && minute != 0 && mSecond != 0) {
            mEndTime = mHour + ":" + minute + ":" + mSecond;
        } else {
            mEndTime = mHour + "0:" + minute + ":" + mSecond;
        }

        return mEndTime;
    }

    /**
     * 时间转换
     *
     * @param longTime 毫秒
     * @return
     */
    public static String longTimeFromat(long longTime) {
        Date date = new Date(longTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }

    /**
     * TimePicker设置结束时间
     * @return
     */
    public static String[] getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy,MM,dd,hh,mm,ss");
        String format = simpleDateFormat.format(System.currentTimeMillis());
        return format.split(",");
    }
}
