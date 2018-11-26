package com.cdjysd.stopstop.mvp.presenter;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.USER;
import com.cdjysd.stopstop.mvp.model.UserRgisterModel;
import com.cdjysd.stopstop.mvp.model.UserRgisterModelImp;
import com.cdjysd.stopstop.mvp.view.UserRgisterView;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/26
 * 修改人：
 * 修改时间：
 */


public class UserRgisterPresenter  extends BasePresenter<UserRgisterView> {

    UserRgisterModelImp model=new UserRgisterModel();

    public void setUserRgister(USER user)
    {
        if (mView == null) {
            return;
        }
        mView.showLoadProgressDialog("请稍等...");
        model.setUserRgister(user, new MVPCallBack<String>() {
            @Override
            public void succeed(String phone) {
                mView.disDialog();
                mView.registUserRgister(phone);

            }

            @Override
            public void failed(String message) {
                mView.disDialog();
                mView.showToast(message);

            }
        });




    }
}
