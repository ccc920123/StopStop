package com.cdjysd.stopstop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.utils.Date_U;
import com.cdjysd.stopstop.utils.FileUtil;
import com.cdjysd.stopstop.utils.ImageUtility;
import com.cdjysd.stopstop.utils.RxSchedulers;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.cdjysd.stopstop.widget.dialog.SimpleListDialog;
import com.cdjysd.stopstop.widget.dialog.SimpleListDialogAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.view.View.GONE;

public class MemoryResultActivity extends BaseActivity {
    private TextView  titleTv,imageNum;
    private EditText number;
    private Button confirm;
    private int width, height;
    private ImageView titleBack;
    private boolean recogType;// 记录进入此界面时是拍照识别界面还是视频识别界面   	 true:视频识别 		false:拍照识别

    private boolean isatuo;

    //    private static final String PATH = Environment
//            .getExternalStorageDirectory().toString() + "/DCIM/Camera/";
    private String[] carColorString;
    ;

    @Override
    protected int getLayoutId() {
        return R.layout.memoryresult_activity;
    }

    @Override
    protected void initInjector() {

        recogType = getIntent().getBooleanExtra("recogType", false);
        isatuo = getIntent().getBooleanExtra("isatuo", false);
        carColorString = this.getResources().getStringArray(R.array.carColor);
        findView();
        titleTv.setText("车辆信息");
        System.out.println("识别时间：" + getIntent().getStringExtra("time"));
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {

    }

    /**
     * @return void    返回类型
     * @throws
     * @Title: findView
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void findView() {
        // TODO Auto-generated method stub

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
        number = (EditText) findViewById(R.id.plate_number);
        confirm = (Button) findViewById(R.id.confirm);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleTv = (TextView) findViewById(R.id.title_tv);
        imageNum = (TextView) findViewById(R.id.imagenum);
        if (isatuo) {
            int left = getIntent().getIntExtra("left", -1);
            int top = getIntent().getIntExtra("top", -1);
            int w = getIntent().getIntExtra("width", -1);
            int h = getIntent().getIntExtra("height", -1);

            number.setText(getIntent().getCharSequenceExtra("number"));

        }
        titleBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String hphm = number.getText().toString();
                //先查询数据库中是否存在该车，
                Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        List<InserCarBean> data = DataSupport.where("hphm=? and hpys=?", hphm, "").find(InserCarBean.class);
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

                            // 将数据插入到本地数据库。
                            final InserCarBean bean = new InserCarBean();
                            bean.setHphm(hphm);
                            bean.setHpys("");
                            bean.setInserttime(Date_U.getCurrentTime2());

                            if (bean.save()) {
                                Intent intent = new Intent(MemoryResultActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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
    public BasePresenter getPresenter() {
        return null;
    }
}
