package com.cdjysd.stopstop;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.baseconoom.Comm;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.bean.SetBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.utils.Date_U;
import com.cdjysd.stopstop.utils.ImageUtility;
import com.cdjysd.stopstop.utils.RxSchedulers;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import static com.cdjysd.stopstop.R.id.title_tv;

public class DetailedActivity extends BaseActivity {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(title_tv)
    TextView titleTv;
    @BindView(R.id.plate_image)
    ImageView plateImage;
    @BindView(R.id.plate_number)
    TextView plateNumber;
    @BindView(R.id.plate_color)
    TextView plateColor;
    @BindView(R.id.inset_time)
    TextView insetTime;
    @BindView(R.id.delete_time)
    TextView deleteTime;
    @BindView(R.id.all_time)
    TextView allTime;
    @BindView(R.id.money)
    TextView moneyText;
    @BindView(R.id.telphone_number)
    EditText telphoneNumber;
    @BindView(R.id.delect_confirm)
    Button delectConfirm;

    private InserCarBean carBean;


    SetBean set;
    //处理返回的发送状态
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    PendingIntent sentPI;

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
    protected void initEventAndData(Bundle savedInstanceState) {

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
        Long oldTime = Date_U.datayyyMMdd(carBean.getInserttime());
        Long newTime = Date_U.datayyyMMdd(Date_U.getCurrentTime2());
        long timeall = newTime - oldTime;
        allTime.setText(getAllTime(timeall));

        set = new SetBean(this);
        String way = set.getWay();

        switch (way) {
            case "0":
                moneyText.setText(getMoneyh(timeall));
                break;

            case "1":
                moneyText.setText(getMoneyday(timeall));
                break;

            case "2":
                moneyText.setText(String.valueOf(set.getMoney()) + "元");
                break;

        }
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                0);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        DataSupport.deleteAll(InserCarBean.class, "hphm = ? and hpys = ?", carBean.getHphm(), carBean.getHpys());
                        DetailedActivity.this.dissLoadDialog();
                        Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        ToastUtils.showToast(DetailedActivity.this,"发送失败");
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        ToastUtils.showToast(DetailedActivity.this,"发送失败");
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        ToastUtils.showToast(DetailedActivity.this,"发送失败");
//                        break;
                    default:
                        ToastUtils.showToast(DetailedActivity.this, "发送失败");
                        DataSupport.deleteAll(InserCarBean.class, "hphm = ? and hpys = ?", carBean.getHphm(), carBean.getHpys());
                        DetailedActivity.this.dissLoadDialog();
                        Intent intents = new Intent(DetailedActivity.this, MainActivity.class);
                        startActivity(intents);
                        finish();
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

    }

    /**
     * 金额(h)
     *
     * @param time
     * @return
     */
    private String getMoneyh(long time) {
        double hour = Math.ceil(time / (1000 * 60 * 60.0f));
        float prees = set.getMoney();

        return String.valueOf(hour * prees) + "元";
    }

    /**
     * 金额(day)
     *
     * @param time
     * @return
     */
    private String getMoneyday(long time) {
        double day = Math.ceil(time / (1000 * 24 * 60 * 60.0f));
        float prees = set.getMoney();
        return String.valueOf(day * prees) + "元";
    }


    private String getAllTime(long time) {
        StringBuffer sb = new StringBuffer();

        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
//        if (!sb.toString().equals("刚刚")) {
//            sb.append("前");
//        }
        return sb.toString();


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
                if ("".equals(telphoneNumber.getText().toString().trim())) {
                    DataSupport.deleteAll(InserCarBean.class, "hphm = ? and hpys = ?", carBean.getHphm(), carBean.getHpys());
                    Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    if (isMobileNO(telphoneNumber.getText().toString().trim())) {
                        MPermissions.requestPermissions(this, Comm.SEND_SMS, Manifest.permission.SEND_SMS);//申请发送短信权限
                    } else {
                        ToastUtils.showToast(DetailedActivity.this, "请输入正确的手机号才能发送停车详情");
                    }
                }

                break;
        }
    }

    @PermissionGrant(Comm.SEND_SMS)
    public void requestCameraSucceed() {
        DetailedActivity.this.showLoading("发送详情中...");
        SetBean bean = new SetBean(DetailedActivity.this);
//        String message = plateNumber.getText() + "车主,您在[" + bean.getAdesstr() + "]," +
//                "停车时长:" + allTime.getText() + ",金额:" + moneyText.getText() +
//                ",停车时间:" + insetTime.getText() + "至" + deleteTime.getText() +
//                "。欢迎再次光临[成都金盈时代]";
      String message=plateNumber.getText() + "车主您好.您在[" + bean.getAdesstr() + "]处停车消费如下:\n"+
              "入库时间:"+insetTime.getText()+"\n出库时间:"+deleteTime.getText()+"\n时长:"+allTime.getText()+
              "金额:" + moneyText.getText() +"欢迎再次光临[成都金盈时代]";

        sendSMS(telphoneNumber.getText().toString().trim(), message);


    }

    @PermissionDenied(Comm.SEND_SMS)
    public void requestCameraFailed() {
        new AlertDialog(this).builder().setTitle("提示")
                .setMsg("会使用到短信权限，请授权！")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);


        return m.matches();

    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber, String message) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, null);
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
