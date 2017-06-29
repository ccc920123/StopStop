package com.cdjysd.stopstop.mvp.presenter;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.model.VehiceOutboundListModel;
import com.cdjysd.stopstop.mvp.model.VehiceOutboundListModelImp;
import com.cdjysd.stopstop.mvp.view.VehiceOutboundListView;

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

    public void selectDBDate(String hphm) {

        mView.showLoadProgressDialog("正在查询数据...");

        model.getData(hphm, new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                mView.showNOData(false, "");
                mView.setAdapter(bean);


            }

            @Override
            public void failed(String message) {
                mView.disDialog();
                mView.showNOData(true, message);
            }
        });


    }


    public void selectDBCollection() {
        mView.showLoadProgressDialog("正在查询数据...");
        model.getCollectionData(new MVPCallBack<List<InserCarBean>>() {
            @Override
            public void succeed(List<InserCarBean> bean) {
                mView.disDialog();
                mView.showNOData(false, "");
                mView.setAdapter(bean);
            }

            @Override
            public void failed(String message) {
                mView.disDialog();
            }
        });


    }

}
