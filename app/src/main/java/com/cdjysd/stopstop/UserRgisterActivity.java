package com.cdjysd.stopstop;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.bean.USER;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.presenter.UserRgisterPresenter;
import com.cdjysd.stopstop.mvp.view.UserRgisterView;
import com.cdjysd.stopstop.utils.NetUtils;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 注册界面/忘记密码
 */
public class UserRgisterActivity extends BaseActivity implements UserRgisterView {


    @BindView(R.id.title_back_imguser)
    ImageView titleBackImguser;
    @BindView(R.id.title_titleuser)
    TextView titleTitleuser;
    @BindView(R.id.title_layout)
    Toolbar titleLayout;
    @BindView(R.id.regist_phone_text)
    TextView registPhoneText;
    @BindView(R.id.regist_send_code)
    TextView registSendCode;
    @BindView(R.id.regist_phone_no)
    EditText registPhoneNo;
    @BindView(R.id.placeholder_phone)
    TextView placeholderPhone;
    @BindView(R.id.item_phone)
    RelativeLayout itemPhone;
    @BindView(R.id.regist_checkcode_text)
    TextView registCheckcodeText;
    @BindView(R.id.placeholder_phone_code)
    TextView placeholderPhoneCode;
    @BindView(R.id.regist_checkcode)
    EditText registCheckcode;
    @BindView(R.id.item_code)
    RelativeLayout itemCode;
    @BindView(R.id.regist_password_text)
    TextView registPasswordText;
    @BindView(R.id.placeholder_phone_pw1)
    TextView placeholderPhonePw1;
    @BindView(R.id.placeholder_phone_pw2)
    TextView placeholderPhonePw2;
    @BindView(R.id.regist_password)
    EditText registPassword;
    @BindView(R.id.item_password)
    RelativeLayout itemPassword;
    @BindView(R.id.regist_password_text_confirm)
    TextView registPasswordTextConfirm;
    @BindView(R.id.regist_password_confirm)
    EditText registPasswordConfirm;
    @BindView(R.id.item_password2)
    RelativeLayout itemPassword2;
    @BindView(R.id.regist_agreement_check)
    CheckBox registAgreementCheck;
    @BindView(R.id.regist_agreement_re)
    TextView registAgreementRe;
    @BindView(R.id.regist_agreement)
    TextView registAgreement;
    @BindView(R.id.item_aggreement)
    RelativeLayout itemAggreement;
    @BindView(R.id.regist_submit)
    Button registSubmit;
    private SendCodeCountDownTimer timer;
    private String phoneNo;
    int tag;
    protected CountDownTimer countDownTimer;
    /**
     * 手机号码匹配((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}
     */
    public static final String PHONE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(17[0,0-9])|(18[0,0-9]))\\d{8}$";
    /**
     * 密码匹配
     */
    public static final String PASSWORD_PATTERN = "^[0-9A-Za-z]{6,18}$";
    /**
     * 验证码格式检查
     */
    public static final String CHECK_CODE_PARTTEN = "^[0-9]{6}$";
    private String checkCode;
    private String passWord;
    private String passwordConfirm;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_rgister;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        init();
    }


    /**
     * 开始处理数据，判断是注册还是忘记密码进入
     */
    private void init() {

        titleBackImguser = findViewById(R.id.title_back_imguser);
        titleTitleuser = findViewById(R.id.title_titleuser);
        titleLayout = findViewById(R.id.title_layout);
        registPhoneText = findViewById(R.id.regist_phone_text);
        registSendCode = findViewById(R.id.regist_send_code);
        registPhoneNo = findViewById(R.id.regist_phone_no);
        placeholderPhone = findViewById(R.id.placeholder_phone);
        itemPhone = findViewById(R.id.item_phone);
        registCheckcodeText = findViewById(R.id.regist_checkcode_text);
        placeholderPhoneCode = findViewById(R.id.placeholder_phone_code);
        registCheckcode = findViewById(R.id.regist_checkcode);
        itemCode = findViewById(R.id.item_code);
        registPasswordText = findViewById(R.id.regist_password_text);
        placeholderPhonePw1 = findViewById(R.id.placeholder_phone_pw1);
        placeholderPhonePw2 = findViewById(R.id.placeholder_phone_pw2);
        registPassword = findViewById(R.id.regist_password);
        itemPassword = findViewById(R.id.item_password);
        registPasswordTextConfirm = findViewById(R.id.regist_password_text_confirm);
        registPasswordConfirm = findViewById(R.id.regist_password_confirm);
        itemPassword2 = findViewById(R.id.item_password2);
        registAgreementCheck = findViewById(R.id.regist_agreement_check);
        registAgreementRe = findViewById(R.id.regist_agreement_re);
        registAgreement = findViewById(R.id.regist_agreement);
        itemAggreement = findViewById(R.id.item_aggreement);
        registSubmit = findViewById(R.id.regist_submit);
        tag = getIntent().getExtras().getInt("TAG");
        if (tag == 0) {
            titleTitleuser.setText("用户注册");


        } else {
            titleTitleuser.setText("密码修改");
            itemAggreement.setVisibility(View.GONE);
            registSubmit.setText("确认修改");
        }
        titleBackImguser.setOnClickListener(click);
        registSendCode.setOnClickListener(initClick);
        registSubmit.setOnClickListener(initClick);
        registAgreement.setOnClickListener(initClick);

    }

    private View.OnClickListener click = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (tag == 3) {
                finish();
            } else {
                onBackPressed();
            }

        }
    };
    private View.OnClickListener initClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == registSendCode) {
                if (!phoneNoCheck()) {// 检查手机号码是否输入正确
                    return;
                }
                if (!NetUtils.isConnected(UserRgisterActivity.this)) {
                    ToastUtils.showToast(UserRgisterActivity.this, "无网络，请检查网络连接");
                    return;
                }

                cn.bmob.v3.BmobSMS.requestSMSCode(phoneNo, "注册验证模块", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, cn.bmob.v3.exception.BmobException e) {
                        if (e == null) {
                            sendCodeAction();
                            Log.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                        } else {
                            ToastUtils.showToast(UserRgisterActivity.this,"发送验证码失败");
                        }
                    }
                });

            } else if (v == registSubmit) {
                if (!inputCheck()) {
                    return;
                }
                //提交注册信息

                BmobSMS.verifySmsCode(phoneNo, checkCode, new UpdateListener() {
                    @Override
                    public void done(BmobException ex) {
                        if (ex == null) {//短信验证码已验证成功
                            uploadingData();
                            Log.i("bmob", "验证通过");
                        } else {
                            Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                            ToastUtils.showToast(UserRgisterActivity.this, "请输入正确的验证码");
                            uploadingData();
                        }
                    }
                });


            } else if (v == registAgreement) {
                openActivity(AgreementActivity.class);
            }

        }
    };

    /**
     * 调用接口上传注册数据
     */
    private void uploadingData() {






        final USER user=new USER();
        user.setPhone(phoneNo);
        user.setPassword(passWord);
        ((UserRgisterPresenter)mPresenter).setUserRgister(user);







    }

    /**
     * @Description 开启倒计时
     */
    private void sendCodeAction() {
        // 开启重新发送验证码倒计时，按钮不可点击
        registSendCode.setClickable(false);
        registSendCode.setBackground(UserRgisterActivity.this.getResources().getDrawable(
                R.drawable.btn_organge));
        timer = new SendCodeCountDownTimer(99000, 1000);
        timer.start();
    }

    /**
     * @return
     * @Description 检查手机号码格式
     */
    private boolean phoneNoCheck() {
        phoneNo = registPhoneNo.getText().toString().trim();
        if (phoneNo == null || "".equals(phoneNo)) {
            ToastUtils.showToast(UserRgisterActivity.this, "手机号不能为空，请输入手机号");
            return false;
        }
        if (!phoneNo.matches(PHONE_PATTERN)) {
            ToastUtils.showToast(UserRgisterActivity.this, "您输入的手机号格式不正确，请输入正确的手机号");
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (tag == 3) {
                finish();
            } else {
                onBackPressed();
            }

        }
        return false;
    }

    private void setSendCodeStyle() {
        registSendCode.setBackground(UserRgisterActivity.this.getResources().getDrawable(
                R.drawable.btn_organgepre));
        registSendCode.setText("重新发送");
        registSendCode.setClickable(true);
    }

    /**
     * @return
     * @Description 检查用户输入数据
     */
    private boolean inputCheck() {
        checkCode = registCheckcode.getText().toString().trim();
        passWord = registPassword.getText().toString().trim();
        passwordConfirm = registPasswordConfirm.getText().toString().trim();
        if (!phoneNoCheck()) {
            return false;
        }
        if (checkCode == null || "".equals(checkCode)) {
            ToastUtils.showToast(UserRgisterActivity.this, "验证码不能为空，请输入短息验证码");
            return false;
        }
        if (!checkCode.matches(CHECK_CODE_PARTTEN)) {
            ToastUtils.showToast(UserRgisterActivity.this, "验证码格式不正确，请输入正确的验证码");
            return false;
        }
        if (passWord == null || "".equals(passWord)) {
            ToastUtils.showToast(UserRgisterActivity.this, "密码不能为空，请输入密码");
            return false;
        }
        if (!passWord.matches(PASSWORD_PATTERN)) {
            ToastUtils.showToast(UserRgisterActivity.this, "您输入的密码格式不正确，请输入6~18位字母或数字组合");
            return false;
        }
        if (passwordConfirm == null || "".equals(passwordConfirm)) {
            ToastUtils.showToast(UserRgisterActivity.this, "确认密码不能为空，请再次输入密码");
            return false;
        }
        if (!passWord.equals(passwordConfirm)) {
            ToastUtils.showToast(UserRgisterActivity.this, "两次输入密码不一致，请重新输入");
            return false;
        }
        if (!registAgreementCheck.isChecked()) {
            ToastUtils.showToast(UserRgisterActivity.this, "请先阅读《用户服务协议》");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return new UserRgisterPresenter();
    }

    @Override
    public void showLoadProgressDialog(String str) {
        showLoading(str);
    }

    @Override
    public void disDialog() {dissLoadDialog();

    }

    @Override
    public void showToast(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void registUserRgister(String phoneNo) {
        SharedPreferencesHelper.putString(UserRgisterActivity.this,"PHONE",phoneNo);
        openActivity(MainActivity.class);
    }


    private class SendCodeCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture
         * @param countDownInterval
         */
        public SendCodeCountDownTimer(long millisInFuture,
                                      long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /*
         * <p>Title onTick</p> <p>Description </p>
         *
         * @param millisUntilFinished
         *
         * @see android.os.CountDownTimer#onTick(long)
         */
        @Override
        public void onTick(long millisUntilFinished) {
            // 已秒为单位倒计时
            registSendCode.setText(String.valueOf(millisUntilFinished / 1000));
        }

        /*
         * <p>Title onFinish</p> <p>Description </p>
         *
         * @see android.os.CountDownTimer#onFinish()
         */
        @Override
        public void onFinish() {
            // 倒计时结束，Button变为重新发送，并且可点击
            setSendCodeStyle();
        }

    }


//    /**
//     * bomb短信回调
//     */
//    class MySMSCodeListener implements SMSCodeListener {
//
//        @Override
//        public void onReceive(String content) {
//            if (registCheckcode != null) {
//                registCheckcode.setText(content);
//            }
//        }
//
//    }
}