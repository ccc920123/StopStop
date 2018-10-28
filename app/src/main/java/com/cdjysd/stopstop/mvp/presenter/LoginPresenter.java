package com.cdjysd.stopstop.mvp.presenter;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.mvp.model.LoginModelImp;
import com.cdjysd.stopstop.mvp.model.LoginModle;
import com.cdjysd.stopstop.mvp.view.LoginView;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/9/27
 * 修改人：
 * 修改时间：
 */


public class LoginPresenter extends BasePresenter<LoginView> {

    LoginModelImp model = new LoginModle();

    public void login(String phone, String password) {
        if (mView == null) {
            return;
        }
        mView.showLoadProgressDialog("正在登录...");
        model.logService(phone, password, new MVPCallBack<String>() {
            @Override
            public void succeed(String bean) {

                mView.disDialog();

                mView.loginsueed(bean);


            }

            @Override
            public void failed(String message) {
             mView.disDialog();
             mView.showToast(message);
            }
        });


    }


}
