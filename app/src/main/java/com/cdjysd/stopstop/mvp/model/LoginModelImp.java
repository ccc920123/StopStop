package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;

/**
 * 描述：
 * 时间： 2018/9/27
 * 修改人：
 * 修改时间：
 */


public interface LoginModelImp {

    void logService(String phone, String pwd, MVPCallBack<String> callBack);
}
