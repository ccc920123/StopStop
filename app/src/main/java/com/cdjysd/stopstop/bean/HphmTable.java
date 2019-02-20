package com.cdjysd.stopstop.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/27
 * 修改人：
 * 修改时间：
 */


public class HphmTable extends BmobObject {


    private String phone;
    private String hphm;
    private String createtime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
