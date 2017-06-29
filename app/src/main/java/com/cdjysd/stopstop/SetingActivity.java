package com.cdjysd.stopstop;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;

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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initInjector() {
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

                break;
            case R.id.sava_button://保存数据，先将数据上传到服务器，在保存到共享内存

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
