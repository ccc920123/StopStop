package com.cdjysd.stopstop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AgreementActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    ImageView titleBackImg;
    @BindView(R.id.title_title)
    TextView titleTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        titleTitle.setText("用户协议");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
