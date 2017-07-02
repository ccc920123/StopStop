package com.cdjysd.stopstop;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.baseconoom.Comm;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.Bind;
import butterknife.OnClick;

public class SetingActivity extends BaseActivity {


    @Bind(R.id.title_back)
    ImageView titleBack;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.adderss_text)
    EditText adderssText;
    @Bind(R.id.adess_onclick)
    TextView adessOnclick;
    @Bind(R.id.radio_hours)
    RadioButton radioHours;
    @Bind(R.id.radio_number)
    RadioButton radioNumber;
    @Bind(R.id.radio_days)
    RadioButton radioDays;
    @Bind(R.id.way_radiogroup)
    RadioGroup wayRadiogroup;
    @Bind(R.id.seat_text)
    EditText seatText;
    @Bind(R.id.press_text)
    EditText pressText;
    @Bind(R.id.set_text)
    TextView setText;
    @Bind(R.id.sava_button)
    Button savaButton;
    private String[] setString;
    private int radTag = 0;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();


    private String Latitude;//纬度
    private String Longitude;//经度
    private String AddrStr;//地址

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            adderssText.setText(msg.obj.toString());
            adderssText.setEnabled(true);
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initInjector() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        titleTv.setText("车场设置");
        setString = this.getResources().getStringArray(R.array.setradio);
    }

    @Override
    protected void initEventAndData() {
        wayRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_hours:
                        radTag = 0;
                        setText.setText(setString[radTag]);

                        break;

                    case R.id.radio_number:
                        radTag = 1;
                        setText.setText(setString[radTag]);
                        break;
                    case R.id.radio_days:
                        radTag = 2;
                        setText.setText(setString[radTag]);
                        break;
                }

            }
        });

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @OnClick({R.id.title_back, R.id.adess_onclick, R.id.sava_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.adess_onclick://地图搜索地址
                MPermissions.requestPermissions(this, Comm.GPS, Manifest.permission
                        .ACCESS_COARSE_LOCATION);

                break;
            case R.id.sava_button://保存数据，先将数据上传到服务器，在保存到共享内存

                break;
        }
    }

    @PermissionGrant(Comm.GPS)
    public void requestSdcardSuccess() {

        //定位
        mLocationClient.start();

    }

    @PermissionDenied(Comm.GPS)
    public void requestSdcardFailed() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("我们需要用到定位，请选择信任！").setPositiveButton("确定", new
                View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);

                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            Message message = new Message();
            Latitude = String.valueOf(location.getLatitude());    //获取纬度信息

            Longitude = String.valueOf(location.getLongitude());    //获取经度信息


            if (location.getLocType() == BDLocation.TypeGpsLocation) {


                AddrStr = location.getAddrStr();    //获取地址信息
                message.obj = AddrStr;
                handler.sendMessage(message);


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {


                AddrStr = location.getAddrStr();    //获取地址信息
                message.obj = AddrStr;
                handler.sendMessage(message);


            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
