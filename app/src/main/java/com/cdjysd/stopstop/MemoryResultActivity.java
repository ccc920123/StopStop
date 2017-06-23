package com.cdjysd.stopstop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.widget.dialog.SimpleListDialog;
import com.cdjysd.stopstop.widget.dialog.SimpleListDialogAdapter;

import static android.view.View.GONE;

public class MemoryResultActivity extends BaseActivity {
    private TextView color, titleTv;
    private EditText number;
    private Button confirm;
    private int width, height;
    private ImageView image, titleBack;
    private String bitmapPath;
    private Bitmap bitmap = null;
    private boolean recogType;// 记录进入此界面时是拍照识别界面还是视频识别界面   	 true:视频识别 		false:拍照识别

    private boolean isatuo;

    private static final String PATH = Environment
            .getExternalStorageDirectory().toString() + "/DCIM/Camera/";
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
    protected void initEventAndData() {

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
        color = (TextView) findViewById(R.id.plate_color);
        confirm = (Button) findViewById(R.id.confirm);
        image = (ImageView) findViewById(R.id.plate_image);
        titleBack = (ImageView) findViewById(R.id.title_back);
        titleTv = (TextView) findViewById(R.id.title_tv);
        if (isatuo) {
            bitmapPath = getIntent().getStringExtra("path");
            int left = getIntent().getIntExtra("left", -1);
            int top = getIntent().getIntExtra("top", -1);
            int w = getIntent().getIntExtra("width", -1);
            int h = getIntent().getIntExtra("height", -1);
            System.out.println("视频流图片路径" + bitmapPath);
            if (bitmapPath != null && !bitmapPath.equals("")) {
                bitmap = BitmapFactory.decodeFile(bitmapPath);
                //在使用图片路径识别模式跳入本界面时   请将下面这行代码注释
                bitmap = Bitmap.createBitmap(bitmap, left, top, w, h);
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                }
            }

            number.setText(getIntent().getCharSequenceExtra("number"));
            color.setText(getIntent().getCharSequenceExtra("color"));

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
                // TODO Auto-generated method stub

                finish();
            }
        });
        color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorType();
            }
        });
    }

    private void selectColorType() {

        SimpleListDialog mSimpleListDialog = new SimpleListDialog(this);
        mSimpleListDialog.setTitle("选择颜色");
        mSimpleListDialog.setTitleLineVisibility(GONE);

        mSimpleListDialog.setAdapter(new SimpleListDialogAdapter(this, carColorString));
        mSimpleListDialog.setCanceledOnTouchOutside(true);
        mSimpleListDialog.setOnSimpleListItemClickListener(new SimpleListDialog.onSimpleListItemClickListener() {
            @Override
            public void onItemClick(int position) {

                switch (position) {
                    case 0:
                        color.setText(carColorString[0]);
                        break;
                    case 1:
                        color.setText(carColorString[1]);
                        break;
                    case 2:
                        color.setText(carColorString[2]);
                        break;
                    case 3:
                        color.setText(carColorString[3]);
                        break;
                    case 4:
                        color.setText(carColorString[4]);
                        break;

                }
            }
        });
        mSimpleListDialog.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (bitmap != null) {
            bitmap = null;
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
