package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/28
 * 修改人：
 * 修改时间：
 */


public interface DetaileModwlImp {

    void delectData(String phone, String hphm, MVPCallBack<String> callBack);
}
