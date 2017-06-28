package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;

import java.util.List;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public interface VehiceOutboundListModelImp extends BaseModel {

    public void getData(String hphm, MVPCallBack<List<InserCarBean>> callBack);

}
