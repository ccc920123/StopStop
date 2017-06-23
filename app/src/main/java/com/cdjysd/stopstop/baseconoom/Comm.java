package com.cdjysd.stopstop.baseconoom;

import android.os.Environment;

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

    public static final String PHONE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "jysd" + File.separator;
    public static final String SAVEFOLDER = PHONE_PATH + "/Error";
    public static final String RECOGNITIONPATH = PHONE_PATH
            + "/OCR/Camera/";
    public static final int PREMISSIONS_CAMERA = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 2;//写的权限

}
