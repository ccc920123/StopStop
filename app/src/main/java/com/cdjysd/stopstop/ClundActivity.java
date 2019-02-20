package com.cdjysd.stopstop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.bean.HphmTable;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.presenter.ClundPresenter;
import com.cdjysd.stopstop.mvp.presenter.MemoryResulPresenter;
import com.cdjysd.stopstop.mvp.view.ClundView;
import com.cdjysd.stopstop.utils.Date_U;
import com.cdjysd.stopstop.utils.NetUtils;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 同步云端数据
 */
public class ClundActivity extends BaseActivity implements ClundView {


    private String phone;
    private TextView clundText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clund;
    }

    @Override
    protected void initInjector() {


    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        phone = this.getIntent().getExtras().getString("PHONE");
        SharedPreferencesHelper.putString(this, "PHONE", phone);
        clundText = findViewById(R.id.clunndtext);

        clundText.setText("正在检查云端车辆...");
        ((ClundPresenter) mPresenter).selectData(phone);
    }

    @Override
    public BasePresenter getPresenter() {
        return new ClundPresenter();
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void disDialog() {

    }

    @Override
    public void showToast(String message) {
        if ("0".equals(message)) {
            ToastUtils.showToast(this, "云端无该用户数据，已为你自动跳转");
        } else {
            ToastUtils.showToast(this, "云端同步失败");

        }


    }

    @Override
    public void selecteClundData(final List<HphmTable> objList) {
        clundText.setText("下载成功，正在写入本机...");

        //开始写入本地sql数据库
        //先查询数据库中是否存在该车，
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                List<InserCarBean> carList=new ArrayList<>();
                //////
//                List<InserCarBean> data = DataSupport.where("hphm=?", hphm).find(InserCarBean.class);
//                if (data != null && data.size() > 0) {
//                    subscriber.onNext(data.size());
//                } else {
//                    subscriber.onNext(0);
//                }

                for (HphmTable beanTable: objList)
                {

                    InserCarBean carBean=new InserCarBean();
                    carBean.setHphm(beanTable.getHphm());
                    carBean.setInserttime(beanTable.getCreatetime());
                    carList.add(carBean);


                }
                DataSupport.saveAll(carList);
                subscriber.onNext(0);

                subscriber.onCompleted();
            }
        });
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                clundText.setText("数据写入本机完成");
                ToastUtils.showToast(ClundActivity.this, "数据写入本机完成");
                openActivity(MainActivity.class);
                finish();


            }

        });

    }
}
