package com.cdjysd.stopstop.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author chenyujin
 * @className SDCardStatu
 * @function SDCard状态
 */
public class SDCardStatu {

    public static boolean isSDCardAvailable() {
        // 获得sd卡的状态
        String sdState = Environment.getExternalStorageState();
        // 判断SD卡是否存在
        return sdState.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 得到手机内存路径
     *
     * @return
     */
    public static String get_SdCard_Categories() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

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

}