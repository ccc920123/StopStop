package com.cdjysd.stopstop.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class InserCarBean extends DataSupport implements Serializable {

    private String carimage;
    private String hphm;
    private String hpys;
    private String inserttime;


    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getHpys() {
        return hpys;
    }

    public void setHpys(String hpys) {
        this.hpys = hpys;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

}
