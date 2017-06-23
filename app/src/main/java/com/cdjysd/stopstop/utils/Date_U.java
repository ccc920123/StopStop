package com.cdjysd.stopstop.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Date_U {
    /**
     * 方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Long datayyyMMdd(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        if (time.contains("/")) {
            time = time.replaceAll("/", "-");
        }
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        long times = 0;
        try {
            date = sdr.parse(time);
            long ltime = date.getTime();
//            String stf = String.valueOf(l);
            times = ltime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @return
     */
    public static String getDateTimeByMillisecond() {

        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

        String time = format.format(date);

        return time;
    }

    /**
     * 根据传递的类型格式化时间
     *
     * @return
     */
    public static String getDateTimeByMilli() {

        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = format.format(date);

        return time;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String dateToTimestamp(String user_time) {
        if (TextUtils.isEmpty(user_time)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(user_time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    /**
     * @param
     * @return
     * @throws NullPointerException
     * @功能描述：将传入的时间字符串转化为固定格式的字符串
     */
    public static String TimestampToTimestamp(String user_time) {
        SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!TextUtils.isEmpty(user_time)) {
            //            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdfdata.parse(user_time);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return sdfdata.format(date);
        } else {
            return sdfdata.format(new Date().getTime());
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * @param
     * @return
     * @throws NullPointerException
     * @功能描述： <p>得到當前系統的hms</P>
     */
    public static String getCurrentTimeHuors() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return " " + sdf.format(new Date());
    }

    //    /**
//     * @param
//     * @return
//     * @功能描述： <p>得到當前系統的hms</P>
//     */
//    public static Date getCurrentTimeYYYYHHMM() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return sdf.parse(new Date());
//    }
    //时间戳转字符串
    public static String getStrTime(long timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeString = sdf.format(new Date(timeStamp));//单位秒
        return timeString;
    }

    public static String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date + "_");
        else {
            str = str + String.valueOf(date + "_");
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }


    public static String getDateTimeFromMillisecond(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
}