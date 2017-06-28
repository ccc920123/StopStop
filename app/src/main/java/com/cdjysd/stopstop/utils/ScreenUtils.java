package com.cdjysd.stopstop.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**类名: ScreenUtils
 * <br/>功能描述:
 * <br/>作者: 陈渝金
 * <br/>时间: 2017/6/28
 * <br/>最后修改者:
 * <br/>最后修改内容:
 */ 

public class ScreenUtils {
    private static ScreenUtils screen;

    private ScreenUtils() {
    }

    private static int width;
    private static int height;

    public static ScreenUtils getInstance(Context mContext) {
        if (screen == null) {
            synchronized (ScreenUtils.class) {
                screen = new ScreenUtils();
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                width = dm.widthPixels;
                height = dm.heightPixels;
            }
        }
        return screen;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
