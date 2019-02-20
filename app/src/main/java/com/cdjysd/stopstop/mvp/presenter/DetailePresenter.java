package com.cdjysd.stopstop.mvp.presenter;

import android.content.Context;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.mvp.model.DetaileModel;
import com.cdjysd.stopstop.mvp.model.DetaileModwlImp;
import com.cdjysd.stopstop.mvp.view.DetailedView;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/28
 * 修改人：
 * 修改时间：
 */


public class DetailePresenter extends BasePresenter<DetailedView> {

    DetaileModwlImp mModel = new DetaileModel();

    public void delectData(String phone, String hphm) {
        if (mView == null) {
            return;
        }
        mView.showLoadProgressDialog("正在数据...");
        mModel.delectData(phone, hphm, new MVPCallBack<String>() {
            @Override
            public void succeed(String bean) {
                mView.disDialog();
                mView.delectSucceed();
            }

            @Override
            public void failed(String message) {
                mView.disDialog();
                mView.delectSucceed();
            }
        });


    }

}
