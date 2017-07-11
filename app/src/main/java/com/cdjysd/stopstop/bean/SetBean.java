package com.cdjysd.stopstop.bean;

import android.content.Context;

import com.cdjysd.stopstop.utils.SharedPreferencesHelper;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class SetBean {

    private String name;
    private String adesstr;
    private String log;//经度
    private String lag;//纬度
    private String way;//收费方式
    private int carnumber;//车位
    private float money;//价格
    private String IMIE;


    private Context context;

    public SetBean(Context context) {
        super();
        this.context = context;
    }

    public SetBean(Context context, String name, String adesstr, String log, String lag, String way, int carnumber, float money,String IMIE) {
        this.context = context;
        this.name = name;
        this.adesstr = adesstr;
        this.log = log;
        this.lag = lag;
        this.way = way;
        this.carnumber = carnumber;
        this.money = money;
        this.IMIE=IMIE;
    }

    public String getIMIE() {
        return IMIE;
    }

    public void setIMIE(String IMIE) {
        this.IMIE = IMIE;
    }

    public String getName() {
        name=SharedPreferencesHelper.getString(context,"name","");
        return name;
    }

    public void setName(String name) {
        SharedPreferencesHelper.putString(context,"name",name);
        this.name = name;
    }

    public String getAdesstr() {
        adesstr=SharedPreferencesHelper.getString(context,"adesstr","");
        return adesstr;
    }

    public void setAdesstr(String adesstr) {
        SharedPreferencesHelper.putString(context,"adesstr",adesstr);
        this.adesstr = adesstr;
    }

    public String getLog() {
        log=SharedPreferencesHelper.getString(context,"log","");
        return log;
    }

    public void setLog(String log) {
        SharedPreferencesHelper.putString(context,"log",log);
        this.log = log;
    }

    public String getLag() {
        lag=SharedPreferencesHelper.getString(context,"lag","");
        return lag;
    }

    public void setLag(String lag) {
        SharedPreferencesHelper.putString(context,"lag",lag);
        this.lag = lag;
    }

    public String getWay() {
        way=SharedPreferencesHelper.getString(context,"way","0");
        return way;
    }

    public void setWay(String way) {
        SharedPreferencesHelper.putString(context,"way",way);
        this.way = way;
    }

    public int getCarnumber() {
        carnumber=SharedPreferencesHelper.getInt(context,"carnumber",0);
        return carnumber;
    }

    public void setCarnumber(int carnumber) {
        SharedPreferencesHelper.putInt(context,"carnumber",carnumber);
        this.carnumber = carnumber;
    }

    public float getMoney() {
        money=SharedPreferencesHelper.getFloat(context,"money",0);
        return money;
    }

    public void setMoney(float money) {
        SharedPreferencesHelper.putFloat(context,"money",money);
        this.money = money;
    }
}
