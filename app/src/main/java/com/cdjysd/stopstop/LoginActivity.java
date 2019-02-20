package com.cdjysd.stopstop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.presenter.LoginPresenter;
import com.cdjysd.stopstop.mvp.view.LoginView;
import com.cdjysd.stopstop.utils.NetUtils;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.DeletableEditText;
import com.cdjysd.stopstop.widget.dialog.LoadProgressDialog;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements LoginView {

    /**
     * 手机号
     */

    private DeletableEditText loginUserPhoneNo;
    /**
     * 密码
     */

    private DeletableEditText loginUserPassword;
    /**
     * 注册
     */

    private TextView loginRegist;
    /**
     * 忘记密码
     */

    private TextView loginForgetPassword;
    /**
     * 登陆
     */

    private Button loginSubmit;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        findView();
        init();
//        JPushInterface.stopPush(getApplicationContext());
    }

    private void init() {
        loginSubmit.setOnClickListener(click);
        loginForgetPassword.setOnClickListener(click);
        loginRegist.setOnClickListener(click);

    }

    private void findView() {

        loginSubmit = (Button) findViewById(R.id.login_submit);
        loginForgetPassword = (TextView) findViewById(R.id.login_forget_password);
        loginRegist = (TextView) findViewById(R.id.login_regist);
        loginUserPassword = (DeletableEditText) findViewById(R.id.login_user_password);
        loginUserPhoneNo = (DeletableEditText) findViewById(R.id.login_user_phone_no);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Bundle bundle = new Bundle();

            switch (v.getId()) {
                case R.id.login_regist:
                    bundle.putInt("TAG", 0);//0注册
                    openActivity(UserRgisterActivity.class,bundle);

                    break;
                case R.id.login_forget_password:
                    bundle.putInt("TAG", 1);//1忘记密码
                    openActivity(UserRgisterActivity.class, bundle);
                    break;
                case R.id.login_submit:
                    if (NetUtils.isConnected(LoginActivity.this)) {

                        ((LoginPresenter) mPresenter).login(loginUserPhoneNo.getText().toString(), loginUserPassword.getText().toString());


                    } else {
                        ToastUtils.showToast(LoginActivity.this, "没有网络，请连接网络");
                    }


                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {

//        AppContext.getQueues().cancelAll("login");
        System.exit(0);
    }

    @Override
    public BasePresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
    }

    @Override
    public void disDialog() {
        dissLoadDialog();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void loginsueed(String userPhone) {
        //判断登录成功后内存储存的手机号是否和登录的手机号一至
        if(SharedPreferencesHelper.getString(this,"PHONE","").equals(userPhone))
        {
            SharedPreferencesHelper.putString(this,"PHONE",userPhone);
            openActivity(MainActivity.class);
        }else{

            //跳转到云端界面更新云端数据

            Bundle bundle=new Bundle();
                    bundle.putString("PHONE",userPhone);
            openActivity(ClundActivity.class,bundle);

        }


        finish();

    }
}
