package com.cdjysd.stopstop.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cdjysd.stopstop.R;
import com.cdjysd.stopstop.baseconoom.AppManager;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.view.BaseView;
import com.cdjysd.stopstop.utils.StatusBarUtil;
import com.cdjysd.stopstop.widget.dialog.LoadProgressDialog;

import butterknife.ButterKnife;
import me.naturs.library.statusbar.StatusBarHelper;



public abstract class BaseActivity<T extends BasePresenter<BaseView>> extends AppCompatActivity implements IBase {
    public BasePresenter mPresenter;
    // public RxManager mRxManager;
    protected StatusBarHelper mStatusBarHelper;
    public Context mContext;
    //判断是否为登录界面或者欢迎界面
    /**
     * 传入标示符
     */
    public String fromTag;
    private LoadProgressDialog progressDialog;
    public static boolean isNewCar = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseConfig();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        onTintStatusBar();
        initInjector();
        onSaveState(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.attach((BaseView) this);
        }
        //注册一个监听连接状态的listener
        initEventAndData();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(BaseActivity.this);
//        }
    }

    public void onSaveState(Bundle savedInstanceState) {
    }


    public void setBaseConfig() {
        initTheme();
        AppManager.getAppManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //  SetStatusBarColor();
    }

    public void onTintStatusBar() {
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT, StatusBarHelper
                    .LEVEL_21_VIEW);
        }
        mStatusBarHelper.setColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /**
     * 获取布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initInjector();

    /**
     * 设置监听
     */
    protected abstract void initEventAndData();

    private void initTheme() {
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    public void SetStatusBarColor(int color) {
        StatusBarUtil.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarUtil.translucentStatusBar(this, false);
    }


    @Override
    protected void onDestroy() {

        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.detachView();
            mPresenter = null;
        }
        //mRxManager.clear();
        ButterKnife.unbind(this);
         AppManager.getAppManager().finishActivity(this);

        super.onDestroy();
    }

    private void initToolBarConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    public void showLoading(String string) {
        progressDialog = LoadProgressDialog.getInstance(this);
        progressDialog.setMessage(string);
        progressDialog.show();
    }

    public void dissLoadDialog() {
        if (progressDialog != null) {
            progressDialog.dismissHUD();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
