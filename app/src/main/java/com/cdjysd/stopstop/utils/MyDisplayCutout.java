package com.cdjysd.stopstop.utils;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 描述：判断 华为，oppo，vivo 是否刘海屏  类
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/9/12
 * 修改人：
 * 修改时间：
 */


public class MyDisplayCutout {

    /**
     * 华为检查是否存在齐刘海
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**
     * oppo 检查是否存在齐刘海
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;//是否有凹槽

    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;//是否有圆角

    /**
     * Voiv 检查是否存在齐刘海
     *
     * @param context
     * @return
     */
    public static boolean hasNotchInScreenAtVoio(Context context) {
        boolean ret = false;//是否有凹槽
        boolean ret1 = false;//是否有圆角
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);
            ret1 = (boolean) get.invoke(FtFeature, ROUNDED_IN_SCREEN_VOIO);
            if (!ret && !ret1) {
                ret = false;
            } else {
                ret = true;
            }
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 判断小米手机中的齐刘海  为1表示是齐刘海
     *
     * @param context
     * @return
     */
    public static boolean hasNotchinScreenXiaoMi(Context context) {

        return SystemProperties.getInt("ro.miui.notch", 0) == 1;


    }
//    public static boolean hasNotchinScreenGoogle(Context context) {
//
//
//        DisplayCutout cutout = context.getDisplayCutout();
//        if(cutout != null){WindowManager.LayoutParams lp =getWindow().getAttributes();lp.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;getWindow().setAttributes(lp); }
//
//
//
//
//        return SystemProperties.getInt("ro.miui.notch", 0) == 1;
//
//
//    }


}
