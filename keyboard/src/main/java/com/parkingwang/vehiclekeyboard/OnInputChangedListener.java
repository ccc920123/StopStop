package com.parkingwang.vehiclekeyboard;

/**
 * 陈渝金
 * 645503254@qq.com
 * 四川星盾科技股份有限公司
 */
public interface OnInputChangedListener {

    void onChanged(String number, boolean isCompleted);

    void onCompleted(String number, boolean isAutoCompleted);
}
