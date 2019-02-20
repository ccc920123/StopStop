package com.cdjysd.stopstop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.presenter.MemoryResulPresenter;
import com.cdjysd.stopstop.mvp.view.MemoryResultView;
import com.cdjysd.stopstop.utils.Date_U;
import com.cdjysd.stopstop.utils.NetUtils;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.parkingwang.vehiclekeyboard.AsyscHphm;
import com.parkingwang.vehiclekeyboard.PopupKeyboard;
import com.parkingwang.vehiclekeyboard.ScenClick;
import com.parkingwang.vehiclekeyboard.view.InputView;

import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MemoryResultActivity extends BaseActivity implements ScenClick,AsyscHphm,MemoryResultView {
    private TextView titleTv, imageNum;
    private Button confirm;
    private ImageView titleBack;
    private final  int HPHM_SCAN_REQUEST_CODE=200;

//    private boolean isatuo;

    //    private static final String PATH = Environment
//            .getExternalStorageDirectory().toString() + "/DCIM/Camera/";
    private PopupKeyboard mPopupKeyboard;
    InputView carnuber;

    @Override
    protected int getLayoutId() {
        return R.layout.memoryresult_activity;
    }

    @Override
    protected void initInjector() {
        findView();
        titleTv.setText("车辆信息");
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(carnuber, this);
        //直接设置新能源车牌
//        mPopupKeyboard.getController().setLockCarKeyBoard(true);
    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: findView
     * @Description:
     */
    private void findView() {

//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);

        confirm = (Button) findViewById(R.id.confirm);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleTv = (TextView) findViewById(R.id.title_tv);
        imageNum = (TextView) findViewById(R.id.imagenum);
        carnuber = findViewById(R.id.carnuber);
        titleBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mPopupKeyboard.isShown()){
                    mPopupKeyboard.dismiss(MemoryResultActivity.this);
                } else {
                    Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String hphm = carnuber.getNumber();

                if("".equals(hphm)){
                    ToastUtils.showToast(MemoryResultActivity.this, "无号牌？请定义一个号牌吧！");
                    return;
                }
                //先查询数据库中是否存在该车，
                Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        List<InserCarBean> data = DataSupport.where("hphm=?", hphm).find(InserCarBean.class);
                        if (data != null && data.size() > 0) {
                            subscriber.onNext(data.size());
                        } else {
                            subscriber.onNext(0);
                        }

                        subscriber.onCompleted();
                    }
                });

                observable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                        if (integer > 0) {
                            new AlertDialog(MemoryResultActivity.this).builder().setTitle("提示：").setMsg("你的车库已经存在该车辆，不能继续添加。")
                                    .setNegativeButton("进行出库", new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //TODO
                                            Intent intent = new Intent(MemoryResultActivity.this, VehicleOutboundListActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }).setPositiveButton("检查车辆", new OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                }
                            }).show();

                        } else {

                            // 将数据插入到本地数据库。同时将车辆信息同步到网络上
                            final InserCarBean bean = new InserCarBean();
                            bean.setHphm(hphm);
                            bean.setHpys("");
                            bean.setInserttime(Date_U.getCurrentTime2());

                            if (bean.save()) {
                                if (NetUtils.isConnected(MemoryResultActivity.this)) {//无网络直接不请求数据

                                    ((MemoryResulPresenter) mPresenter).insertNet(bean, SharedPreferencesHelper.getString(MemoryResultActivity.this, "PHONE", ""));
                                }else{
                                    Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            } else {
                                ToastUtils.showToast(MemoryResultActivity.this, "保存失败，你的手机系统内存可能不足");
                            }
                        }


                    }

                });


            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPopupKeyboard.isShown()){
            mPopupKeyboard.dismiss(MemoryResultActivity.this);
            return;
        }
        Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public BasePresenter getPresenter() {
        return new MemoryResulPresenter();
    }


    @Override
    public void scenClick(Context mContext) {

        Intent intent = new Intent(MemoryResultActivity.this,
                MemoryCameraActivity.class);

        startActivityForResult(intent, HPHM_SCAN_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HPHM_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String hphm = data.getCharSequenceExtra("number").toString();
            carnuber.updateNumber(hphm);    //号码返回结果
            imageNum.setText(hphm);
        }
    }

    @Override
    public void gethphm() {

        imageNum.setText(carnuber.getNumber());
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
    public void inserNetSucced() {


        Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
