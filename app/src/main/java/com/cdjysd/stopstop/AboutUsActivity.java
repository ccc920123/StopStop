package com.cdjysd.stopstop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;

public class AboutUsActivity extends BaseActivity {


    ImageView titleBack;
    TextView titleTv;
    TextView version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initInjector() {

        titleBack=findViewById(R.id.title_back);
        titleTv=findViewById(R.id.title_tv);
        version=findViewById(R.id.version);

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        titleTv.setText("关于我们");
        version.setText("版本号：V" + getAppVersionName(this));
        titleBack.setOnClickListener(click);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


   private View.OnClickListener click=new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           onBackPressed();

       }
   };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
