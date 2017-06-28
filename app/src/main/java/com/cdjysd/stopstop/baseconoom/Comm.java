package com.cdjysd.stopstop.baseconoom;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class Comm {
    private static LayoutInflater inflater;
    public static final String PHONE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "jysd" + File.separator;
    public static final String SAVEFOLDER = PHONE_PATH + "/Error";
    public static final String RECOGNITIONPATH = PHONE_PATH
            + "/OCR/Camera/";
    public static final int PREMISSIONS_CAMERA = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 2;//写的权限
    public static View inflate(Context context, ViewGroup parent, int res) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater.inflate(res, parent, false);

    }
    public static View inflate(Context context,int res){
        if(inflater==null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater.inflate(res,null);
    }
}
