package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;

import java.util.List;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/12/6
 * 修改人：
 * 修改时间：
 */


public interface ClundModelImp {
    void selectData(String phone,MVPCallBack<List<HphmTable>> callBack);
}
