package com.cdjysd.stopstop.mvp.presenter;

import android.content.Context;

import com.alibaba.idst.nls.internal.protocol.Content;
import com.cdjysd.stopstop.MemoryResultActivity;
import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.model.VehiceOutboundListModel;
import com.cdjysd.stopstop.mvp.model.VehiceOutboundListModelImp;
import com.cdjysd.stopstop.mvp.view.VehiceOutboundListView;
import com.cdjysd.stopstop.utils.NetUtils;

import java.util.List;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class VehiceOutboundListPresenter extends BasePresenter<VehiceOutboundListView> {

    VehiceOutboundListModelImp model = new VehiceOutboundListModel();

    public void selectDBDate(final Context context, final String hphm, final String phone) {

        mView.showLoadProgressDialog("正在查询数据...");

        model.getData(hphm, new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                if (bean == null || bean.size() <= 0) {
                    mView.showNOData(true, "");
                    if (NetUtils.isConnected(context)) {
                        selectNetData(phone, hphm);//通过数据再次查找数据
                    }
                } else {
                    mView.showNOData(false, "");
                }

                mView.setAdapter(bean);


            }

            @Override
            public void failed(String message) {
                mView.disDialog();
                mView.showNOData(true, message);
            }
        });


    }


    public void selectDBCollection(final Context context,final String phone) {
        mView.showLoadProgressDialog("正在查询数据...");
        model.getCollectionData(new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                if (bean == null || bean.size() <= 0) {
                    mView.showNOData(true, "");
                    if (NetUtils.isConnected(context)) {
                        selectNetData(phone);
                    }
                } else {
                    mView.showNOData(false, "");
                }
                mView.setAdapter(bean);
            }

            @Override
            public void failed(String message) {
                mView.disDialog();
            }
        });


    }

    /**
     * 查询网络上的数据
     *
     * @param phoen
     */
    public void selectNetData(String phoen) {
        mView.showLoadProgressDialog("正在查询数据...");
        model.selectNetData(phoen, new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                if (bean == null || bean.size() <= 0) {
                    mView.showNOData(true, "");

                } else {
                    mView.showNOData(false, "");
                }
                mView.setAdapter(bean);
            }

            @Override
            public void failed(String message) {
                mView.disDialog();
            }
        });


    }

    /**
     * 查询网络上的数据
     *
     * @param phoen
     */
    public void selectNetData(String phoen, String hphm) {
        mView.showLoadProgressDialog("正在查询数据...");
        model.SelectNetLikeData( hphm,phoen, new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                if (bean == null || bean.size() <= 0) {
                    mView.showNOData(true, "");
                } else {
                    mView.showNOData(false, "");
                }
                mView.setAdapter(bean);
            }

            @Override
            public void failed(String message) {
                mView.disDialog();
            }
        });


    }


}
