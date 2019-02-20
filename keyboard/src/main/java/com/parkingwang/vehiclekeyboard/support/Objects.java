package com.parkingwang.vehiclekeyboard.support;

/**
 陈渝金
 * 645503254@qq.com
 * 四川星盾科技股份有限公司
 */
public class Objects {

    private Objects() {
    }

    public static <T> T notNull(T val) {
        if (null == val) {
            throw new NullPointerException("Null pointer");
        }
        return val;
    }
}
