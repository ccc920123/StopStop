package com.cdjysd.stopstop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.baseconoom.Comm;
import com.cdjysd.stopstop.bean.SetBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class SetingActivity extends BaseActivity implements AMapLocationListener {


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

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String Latitude;//纬度
    private String Longitude;//经度
    private String AddrStr;//地址

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dissLoadDialog();//关闭等待框
            if (msg.obj.toString() != null) {
                adderssText.setText(msg.obj.toString());
                adderssText.setEnabled(true);
            }
            super.handleMessage(msg);
        }
    };
    private String PDA_IMEI;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initInjector() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //注册监听函数
        initLocation();
        titleTv.setText("车场设置");
        setString = this.getResources().getStringArray(R.array.setradio);
        TelephonyManager TelephonyMgr = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        if (TelephonyMgr.getDeviceId() != null) {
            PDA_IMEI = TelephonyMgr.getDeviceId();
        } else {
            PDA_IMEI = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        SetBean set = new SetBean(this);
        if (!"".equals(set.getAdesstr())) {
            adderssText.setText(set.getAdesstr());
            seatText.setText(String.valueOf(set.getCarnumber()));
            pressText.setText(String.valueOf(set.getMoney()));
            switch (set.getWay()) {
                case "0":
                    radioHours.setChecked(true);
                    break;
                case "1":
                    radioNumber.setChecked(true);
                    break;
                case "2":
                    radioDays.setChecked(true);
                    break;
                default:
                    radioHours.setChecked(true);
                    break;


            }


        }
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
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

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位

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
                        .ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION);

                break;
            case R.id.sava_button://保存数据，先将数据上传到服务器，在保存到共享内存
                //判断数据是否为空
                if ("".equals(adderssText.getText().toString().trim()) ||
                        "".equals(seatText.getText().toString().trim()) ||
                        "".equals(pressText.getText().toString().trim())) {
                    ToastUtils.showToast(SetingActivity.this, "请填写完相应的数据");
                } else {
                    SetBean msetBean = new SetBean(SetingActivity.this);
                    msetBean.setAdesstr(adderssText.getText().toString());
                    msetBean.setLag(Latitude);
                    msetBean.setLog(Longitude);
                    msetBean.setCarnumber(Integer.parseInt(seatText.getText().toString()));
                    msetBean.setMoney(Float.parseFloat(pressText.getText().toString()));
                    msetBean.setWay(String.valueOf(radTag));
                    msetBean.setIMIE(PDA_IMEI);
                    onBackPressed();
                }


                break;
        }
    }

    @PermissionGrant(Comm.GPS)
    public void requestSdcardSuccess() {
        showLoading("定位中");
        //定位
        mlocationClient.startLocation();

    }

    @PermissionDenied(Comm.GPS)
    public void requestSdcardFailed() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("我们需要用到定位，请选择信任！").setPositiveButton("确定", new
                View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mlocationClient.stopLocation();
        Intent intent = new Intent(SetingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                Message message = new Message();
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                Latitude = String.valueOf(amapLocation.getLatitude());//获取纬度
                Longitude = String.valueOf(amapLocation.getLongitude());//获取经度
                AddrStr = amapLocation.getAddress();
                message.obj = AddrStr;
                handler.sendMessage(message);

//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

}
