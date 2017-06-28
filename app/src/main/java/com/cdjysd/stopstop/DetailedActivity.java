package com.cdjysd.stopstop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.utils.Date_U;
import com.cdjysd.stopstop.utils.ImageUtility;
import com.cdjysd.stopstop.utils.RxSchedulers;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import static com.cdjysd.stopstop.R.id.title_tv;

public class DetailedActivity extends BaseActivity {


    @Bind(R.id.title_back)
    ImageView titleBack;
    @Bind(title_tv)
    TextView titleTv;
    @Bind(R.id.plate_image)
    ImageView plateImage;
    @Bind(R.id.plate_number)
    TextView plateNumber;
    @Bind(R.id.plate_color)
    TextView plateColor;
    @Bind(R.id.inset_time)
    TextView insetTime;
    @Bind(R.id.delete_time)
    TextView deleteTime;
    @Bind(R.id.all_time)
    TextView allTime;
    @Bind(R.id.money)
    TextView moneyText;
    @Bind(R.id.telphone_number)
    EditText telphoneNumber;
    @Bind(R.id.delect_confirm)
    Button delectConfirm;

    private InserCarBean carBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed;
    }

    @Override
    protected void initInjector() {

        carBean = (InserCarBean) getIntent().getSerializableExtra("BEAN");
        titleTv.setText("出库详情");

    }

    @Override
    protected void initEventAndData() {

        if (carBean.getCarimage() != null && !"".equals(carBean.getCarimage())) {

            Observable.create(new Observable.OnSubscribe<Bitmap>() {
                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    Bitmap bitmap = ImageUtility.base64ToBitmap(carBean.getCarimage());
                    subscriber.onNext(bitmap);
                }
            }).compose(RxSchedulers.schedulersTransformer).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap beans) {

                    plateImage.setImageBitmap(beans);
                }
            });


        }

        plateNumber.setText(carBean.getHphm());
        plateColor.setText(carBean.getHpys());
        insetTime.setText(carBean.getInserttime());
        deleteTime.setText(Date_U.getCurrentTime2());
//        allTime.setText();
//        moneyText.setText();


    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @OnClick({R.id.title_back, R.id.delect_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.delect_confirm:

                DataSupport.deleteAll(InserCarBean.class, "hphm = ? and hpys = ?", carBean.getHphm(), carBean.getHpys());

                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailedActivity.this, VehicleOutboundListActivity.class);
        startActivity(intent);
        finish();


    }
}
