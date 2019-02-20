//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kernal.plateid;

import android.telephony.TelephonyManager;

import com.kernal.lisence.DeviceFP;

public class PlateIDAPI {
    static {
        System.loadLibrary("THPlateID");
    }

    public PlateIDAPI() {
    }

    public native int TH_InitPlateIDSDKTF(TH_PlateIDCfg var1, DeviceFP var2);

    public native int TH_InitPlateIDSDK(TH_PlateIDCfg var1, TelephonyManager var2, DeviceFP var3);

    public native int TH_UninitPlateIDSDK();

    public native TH_PlateIDResult[] TH_RecogImage(String var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10);

    public native TH_PlateIDResult[] TH_RecogImageByte(byte[] var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10);

    public native TH_PlateIDResult[] TH_RecogImageByte(byte[] var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10, int var11);

    public native TH_PlateIDResult[] TH_RecogImageByte(byte[] var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10, TH_PlateIDCfg var11);

    public native int TH_SetImageFormat(int var1, int var2, int var3);

    public native int TH_SetDayNightMode(int var1);

    public native int TH_SetEnlargeMode(int var1);

    public native int TH_SetEnabledPlateFormat(int var1);

    public native int TH_SetProvinceOrder(String var1);

    public native int TH_SetRecogThreshold(int var1, int var2);

    public native String TH_GetVersion();

    public native int TH_SetContrast(int var1);

    public native int TH_SetAutoSlopeRectifyMode(int var1, int var2);
}
