package com.cdjysd.stopstop.mvp.presenter;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.model.MemoryResulModel;
import com.cdjysd.stopstop.mvp.model.MemoryResulModelImp;
import com.cdjysd.stopstop.mvp.view.MemoryResultView;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/27
 * 修改人：
 * 修改时间：
 */


public class MemoryResulPresenter extends BasePresenter<MemoryResultView> {

    MemoryResulModelImp  modelImp=new MemoryResulModel();



    public void insertNet(InserCarBean bean,String phone)
    {

        if (mView == null) {
            return;
        }
        mView.showLoadProgressDialog("正在同步数据");
        modelImp.insetNet(bean, phone,new MVPCallBack<String>() {
            @Override
            public void succeed(String bean) {

                mView.disDialog();
                mView.inserNetSucced();

            }

            @Override
            public void failed(String message) {
                mView.disDialog();
                mView.showToast("同步失败，不影响你的使用");
                mView.inserNetSucced();

            }
        });

    }

}
