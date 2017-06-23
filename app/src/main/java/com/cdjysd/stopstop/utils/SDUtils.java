package com.cdjysd.stopstop.utils;

import android.os.Environment;
import android.text.format.Time;

import java.io.File;

/**
 * Created by Administrator on 2017/5/2.
 */
public class SDUtils {

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                "mounted");
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            sdDir = Environment.getRootDirectory();
        }
        return sdDir.toString();
    }

    public static final String PATH = Environment
            .getExternalStorageDirectory().toString() + "/OCR/Camera/";


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
}
