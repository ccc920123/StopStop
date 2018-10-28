package com.cdjysd.stopstop.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/9/28
 * 修改人：
 * 修改时间：
 */


public class USER extends BmobObject {

    private String phone;
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
