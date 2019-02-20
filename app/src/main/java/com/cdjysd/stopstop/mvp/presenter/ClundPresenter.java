package com.cdjysd.stopstop.mvp.presenter;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;
import com.cdjysd.stopstop.mvp.model.ClundModel;
import com.cdjysd.stopstop.mvp.model.ClundModelImp;
import com.cdjysd.stopstop.mvp.view.ClundView;

import java.util.List;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/12/6
 * 修改人：
 * 修改时间：
 */


public class ClundPresenter extends BasePresenter<ClundView> {

  ClundModelImp modelImp=new ClundModel();

    public void selectData(String phone)
    {

        modelImp.selectData(phone, new MVPCallBack<List<HphmTable>>() {
            @Override
            public void succeed(List<HphmTable> bean) {
                mView.selecteClundData(bean);

            }

            @Override
            public void failed(String message) {

                mView.showToast(message);

            }
        });




    }


}
