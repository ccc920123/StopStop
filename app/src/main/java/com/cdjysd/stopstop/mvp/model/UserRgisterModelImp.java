package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.USER;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/26
 * 修改人：
 * 修改时间：
 */


public interface UserRgisterModelImp {
    void setUserRgister(USER user, MVPCallBack<String> callBack );
}
