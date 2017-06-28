package com.cdjysd.stopstop.mvp.view;

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


public interface VehiceOutboundListView extends  BaseView {
    void setAdapter(List<InserCarBean> data);
    void showNOData(boolean isShow,String string);
}
