package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/27
 * 修改人：
 * 修改时间：
 */


public interface MemoryResulModelImp {
    void insetNet(InserCarBean carBean,String phone, MVPCallBack<String> callBack);
}
