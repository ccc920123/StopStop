package com.parkingwang.vehiclekeyboard.support;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;

/**
 陈渝金
 * 645503254@qq.com
 * 四川星盾科技股份有限公司
 */
public class DrawableTint {

    public static Drawable tint(Drawable drawable, ColorStateList tint) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableTint.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);
        drawable = drawable.mutate();
        DrawableCompat.setTintList(drawable, tint);
        return drawable;
    }

    // 这里使用的是TintAwareDrawable的setTintMode方法
    @SuppressLint("RestrictedApi")
    private static void setTintMode(Drawable drawable, PorterDuff.Mode mode) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintMode(mode);
        } else {
            DrawableCompat.setTintMode(drawable, mode);
        }
    }
}
