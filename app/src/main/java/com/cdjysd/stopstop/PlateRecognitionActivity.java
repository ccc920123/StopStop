package com.cdjysd.stopstop;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdjysd.stopstop.utils.CameraParametersUtils;
import com.cdjysd.stopstop.utils.CameraSetting;
import com.cdjysd.stopstop.utils.FrameCapture;
import com.cdjysd.stopstop.utils.SDUtils;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.VinInformation;
import com.cdjysd.stopstop.widget.PlateViewfinderView;
import com.kernal.plateid.PlateCfgParameter;
import com.kernal.plateid.PlateRecogService;
import com.kernal.plateid.PlateRecognitionParameter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.hardware.Camera.getCameraInfo;

public class PlateRecognitionActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PreviewCallback {
    /**
     * 提示
     */
    private TextView tip;
    /**
     * 相机参数工具
     */
    private CameraParametersUtils cameraParametersUtils;
    /**
     * 屏幕尺寸
     */
    private int screenWidth, screenHeight;
    /**
     * vin界面参数
     */
    private VinInformation wlci;
    /**
     * 中间那个green框框
     */
    public static PlateViewfinderView myViewfinderView;
    /**
     * 定时对焦
     */
    private Timer time = new Timer();
    private TimerTask timer;
    /**
     * 界面相机所需的三个对象
     */
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    /**
     * 拍照分辨率集合
     */
    private List<Integer> srcList = new ArrayList<Integer>();// 拍照分辨率集合
    /**
     * 屏幕相关参数
     */
    private DisplayMetrics dm = new DisplayMetrics();
    private Message msg;
    /**
     * 动态设置界面布局的 重要参数属性
     */
    private RelativeLayout.LayoutParams layoutParams;
    /**
     * 根布局 ,右侧布局
     */
    public static RelativeLayout re, bg_template_listView;
    /**
     *
     */
    public static int nMainIDX;
    /**
     * 敏感区域,裁剪时候用到的参数
     */
    private int[] regionPos = new int[4];// 敏感区域
    /**
     * 第一次运行
     */
    private boolean isFirstProgram = true;
    /**
     * 相机的参数尺寸
     */
    private Camera.Size size;
    /**
     * 拍照获取的数据
     */
    private byte[] data;
    /**
     * 点击拍照标记
     */
    private boolean isTakePic = false;
    /**
     * 拍照按钮
     */
    private ImageButton imbtn_takepic;
    /**
     * 返回键和闪光灯
     */
    private ImageView iv_camera_back, iv_camera_flash;
    /**
     * 判断SDK是否初始化
     */
    private int iInitPlateIDSDK = -1;
    private int nRet = -1;

    private boolean isCamera = true;// 判断是预览识别还是视频识别 true:视频识别 false:预览识别
    private boolean recogType = true;// 记录进入此界面时是拍照识别还是视频识别 true:视频识别 false:拍照识别

    private boolean cameraRecogUtill = false; // cameraRecogUtill
    // true:拍照识别采用拍摄照片（整图）根据路径识别，不受扫描框限制
    // false:采用视频流 单帧识别模式 识别扫描框内的车牌
    private String path;// 圖片保存的路徑
    private boolean isAutoFocus = true; // 是否开启自动对焦 true:开启，定时对焦 false:不开起
    // ，只在图片模糊时对焦
    private boolean sameProportion = false;   //是否在1280*960预览分辨率以下找到与屏幕比相同比例的 预览分辨率组
    private boolean isFirstIn = true;
    private int initPreWidth = 1280; //
    private int initPreHeight = 960;//预览分辨率筛选上限，即在筛选合适的分辨率时  在这两个值以下筛选
    //图片格式
    private int imageformat = 6;// NV21 -->6
    public PlateRecogService.MyBinder recogBinder;
    /**
     * 屏幕真实分辨率宽高
     */
    private int width, height;
    /**
     * 预览大小
     */
    private int preWidth = 0;
    private int preHeight = 0;
    /**
     *
     */
    private int bVertFlip = 0;
    private int bDwordAligned = 1;
    //缓存数据
    private byte[] tempData;
    // 照片数据
    private byte[] picData;

    private String[] fieldvalue = new String[14];
    private int rotation = 0;
    private static int tempUiRot = 0;
    private Bitmap bitmap, bitmap1;
    private Vibrator mVibrator;
    //返回数据
    private String number = "", color = "";
    private PlateRecognitionParameter prp = new PlateRecognitionParameter();
    private boolean setRecogArgs = true;// 刚进入此界面后对识别车牌函数进行参数设置

    /**
     * NV21数据
     */
    private byte[] intentNV21data;
    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (PlateRecogService.MyBinder) service;
            iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();

            if (iInitPlateIDSDK != 0) {
                nRet = iInitPlateIDSDK;
                String[] str = {"" + iInitPlateIDSDK};
                getResult(str);
            }
            // recogBinder.setRecogArgu(recogPicPath, imageformat,
            // bGetVersion, bVertFlip, bDwordAligned);
            PlateCfgParameter cfgparameter = new PlateCfgParameter();
            cfgparameter.armpolice = 4;
            cfgparameter.armpolice2 = 16;
            cfgparameter.embassy = 12;
            cfgparameter.individual = 0;
            // cfgparameter.nContrast = 9;
            cfgparameter.nOCR_Th = 0;
            cfgparameter.nPlateLocate_Th = 5;
            cfgparameter.onlylocation = 15;
            cfgparameter.tworowyellow = 2;
            cfgparameter.tworowarmy = 6;
            cfgparameter.szProvince = "";
            cfgparameter.onlytworowyellow = 11;
            cfgparameter.tractor = 8;
            cfgparameter.bIsNight = 1;
            if (cameraRecogUtill) {
                imageformat = 0;
            }
            recogBinder.setRecogArgu(cfgparameter, imageformat, bVertFlip,
                    bDwordAligned);

            // fieldvalue = recogBinder.doRecog(recogPicPath, width,
            // height);

        }
    };

    /*  private Handler handler = new Handler() {
          @Override
          public void handleMessage(Message msg) {
              getScreenSize();
              if(msg.what==5){
                  getPreToChangView(preWidth, preHeight);
              }else{
                  re.removeView(myview);
                  setRotationAndView(msg.what);
                  getPreToChangView(preWidth, preHeight);
                  if (rotation == 90 || rotation == 270) {
                      myview = new PlateViewfinderView(PlateRecognitionActivity.this,false,preWidth,preHeight);
                  } else {
                      myview = new PlateViewfinderView(PlateRecognitionActivity.this, true,preWidth,preHeight);
                  }
                  re.addView(myview);
                  if(camera!=null){
                      camera.setDisplayOrientation(rotation);
                  }
              }
              super.handleMessage(msg);
          }
      };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        //如果是视屏识别，改变识别模式
//        isCamera = getIntent().getBooleanExtra("camera", false);
//        recogType = getIntent().getBooleanExtra("camera", false);
//           if (isCamera) {
//        if (cameraRecogUtill) {
//            cameraRecogUtill = false;
//        }
//    }
        cameraParametersUtils = new CameraParametersUtils(this);
        width = cameraParametersUtils.srcWidth;
        height = cameraParametersUtils.srcHeight;
        PlateRecogService.initializeType = recogType;

        findView();
        //这里我们让他一直竖屏
//        setRotationAndView(uiRot);
        setVerticalButton();
        getScreenSize();
        tempUiRot = 0;
    }

    // 设置横屏屏方向按钮布局
    private void setVerticalButton() {
        if (width == surfaceView.getWidth()
                || surfaceView.getWidth() == 0) {

            layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT, height);
            surfaceView.setLayoutParams(layoutParams);

            // 右侧菜单栏的背景布局
            layoutParams = new RelativeLayout.LayoutParams(
                  width , (int) (0.28 * height));
            layoutParams.topMargin = (int) (0.82 * height);
//            layoutParams.topMargin = 0;
            bg_template_listView.setLayoutParams(layoutParams);
            //提示
            layoutParams = new RelativeLayout.LayoutParams(
                     width, (int) (0.20 * height));
            layoutParams.topMargin = (int) (0.75 * height);
            layoutParams.leftMargin = (int) (width * 0.025);


            tip.setLayoutParams(layoutParams);
            // 闪光灯按钮UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.1), (int) (width * 0.1));
            layoutParams.leftMargin = (int) (width * 0.85);
            layoutParams.topMargin = (int) (height * 0.05);
            iv_camera_flash.setLayoutParams(layoutParams);

        } else if (width > surfaceView.getWidth()) {

            // 如果将虚拟硬件弹出则执行如下布局代码，相机预览分辨率不变压缩屏幕的高度
            int surfaceViewHeight = (surfaceView.getWidth() * height)
                    / width;
            layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    surfaceViewHeight);
            layoutParams.topMargin = (height - surfaceViewHeight) / 2;
            surfaceView.setLayoutParams(layoutParams);

            // 右侧菜单栏的背景布局
            layoutParams = new RelativeLayout.LayoutParams(
                     width, (int) (0.20 *height));
            layoutParams.topMargin = (int) (0.82 * height)
                    - (height - surfaceView.getHeight());
            bg_template_listView.setLayoutParams(layoutParams);
            //提示
            layoutParams = new RelativeLayout.LayoutParams(
                    width, (int) (0.20 * height));
            layoutParams.leftMargin = (int) (width * 0.025);
            layoutParams.topMargin = (int) (height * 0.75);
            tip.setLayoutParams(layoutParams);
            // 闪光灯按钮UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (width * 0.1), (int) (width * 0.1));
            layoutParams.leftMargin = (int) (width * 0.85);
            layoutParams.topMargin = (int) (height * 0.05);
            iv_camera_flash.setLayoutParams(layoutParams);

        }
        layoutParams = new RelativeLayout.LayoutParams(
                (int) (width * 0.1), (int) (width * 0.1));
        layoutParams.leftMargin = (int) (width * 0.05);
        layoutParams.topMargin = (int) (height * 0.05);
        iv_camera_back.setLayoutParams(layoutParams);
    }

    /**
     * 获取控件
     */
    private void findView() {
//        if (myViewfinderView != null) {
//            myViewfinderView.destroyDrawingCache();
//            re.removeView(myViewfinderView);
//            myViewfinderView = null;
//        }
        tip = (TextView) findViewById(R.id.tip);
        surfaceView = (SurfaceView) this
                .findViewById(R.id.surfaceview_camera);
        bg_template_listView = (RelativeLayout) findViewById(R.id.bg_template_listView);
        //
        re = (RelativeLayout) this.findViewById(R.id.re);
        imbtn_takepic = (ImageButton) this.findViewById(R.id.imbtn_takepic);
        // 扫描框的UI布局 myViewfinderView = new PlateViewfinderView(this, true, preWidth, preHeight);
//        layoutParams = new RelativeLayout.LayoutParams(width, height);
//        layoutParams.leftMargin = 0;
//        layoutParams.topMargin = 0;
//        myViewfinderView.setLayoutParams(layoutParams);
//        re.addView(myViewfinderView);
        iv_camera_back = (ImageView) this.findViewById(R.id.iv_camera_back);
        iv_camera_flash = (ImageView) this
                .findViewById(R.id.iv_camera_flash);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        iv_camera_back.setOnClickListener(this);
        iv_camera_flash.setOnClickListener(this);
        imbtn_takepic.setOnClickListener(this);
    }

    /* (non-Javadoc)
        * @see android.app.Activity#onResume()
        */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        OpenCameraAndSetParameters();
    }

    /**
     * @param @param fieldvalue 调用识别接口返回的数据
     * @return void 返回类型
     * @Title: getResult
     * @Description: TODO(获取结果)
     * @throwsbyte[]picdata
     */

    private void getResult(String[] fieldvalue) {

        if (nRet != 0)
        // 未通过验证 将对应错误码返回
        {
            Toast.makeText(this, "验证失败111", Toast.LENGTH_SHORT).show();
            //  feedbackWrongCode();
        } else {
            // 通过验证 获取识别结果
            String result = "";
            String[] resultString;
            String timeString = "";
            String boolString = "";
            boolString = fieldvalue[0];

            if (boolString != null && !boolString.equals(""))
            // 检测到车牌后执行下列代码
            {

                resultString = boolString.split(";");
                int lenght = resultString.length;
                // Log.e("DEBUG", "nConfidence:" +
                // fieldvalue[4]);
                if (lenght > 0) {

                    String[] strarray = fieldvalue[4].split(";");

                    // 静态识别下 判断图像清晰度是否大于75

                    if (recogType ? true : Integer.valueOf(strarray[0]) > 75) {

                        tempData = recogBinder.getRecogData();

                        if (tempData != null) {

                            if (lenght == 1) {

                                if (fieldvalue[11] != null
                                        && !fieldvalue[11].equals("")) {
                                    int time = Integer.parseInt(fieldvalue[11]);
                                    time = time / 1000;
                                    timeString = "" + time;
                                } else {
                                    timeString = "null";
                                }

                                // if (null != fieldname) {

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                options.inPurgeable = true;
                                options.inInputShareable = true;
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                int Height = 0, Width = 0;
                                if (rotation == 90 || rotation == 270) {
                                    Height = preWidth;
                                    Width = preHeight;
                                } else if (rotation == 180 || rotation == 0) {
                                    Height = preHeight;
                                    Width = preWidth;
                                }
                                YuvImage yuvimage = new YuvImage(tempData,
                                        ImageFormat.NV21, Width, Height,
                                        null);
                                yuvimage.compressToJpeg(new Rect(0, 0,
                                        Width, Height), 100, baos);

                                bitmap = BitmapFactory.decodeByteArray(
                                        baos.toByteArray(), 0, baos.size(),
                                        options);

                                bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
                                        bitmap.getWidth(),
                                        bitmap.getHeight(), null, true);
                                path = savePicture(bitmap1);

                                mVibrator = (Vibrator) getApplication()
                                        .getSystemService(
                                                Service.VIBRATOR_SERVICE);
                                mVibrator.vibrate(100);
                                closeCamera();
                                Intent intent = new Intent(
                                        PlateRecognitionActivity.this,
                                        MainActivity.class);
                                number = fieldvalue[0];
                                color = fieldvalue[1];

                                int left = Integer.valueOf(fieldvalue[7]);
                                int top = Integer.valueOf(fieldvalue[8]);
                                int w = Integer.valueOf(fieldvalue[9])
                                        - Integer.valueOf(fieldvalue[7]);
                                int h = Integer.valueOf(fieldvalue[10])
                                        - Integer.valueOf(fieldvalue[8]);
                                Toast.makeText(PlateRecognitionActivity.this, "" + number + "," + color, Toast.LENGTH_SHORT).show();
                                intent.putExtra("number", number);
                                intent.putExtra("color", color);
                                intent.putExtra("path", path);
                                intent.putExtra("left", left);
                                intent.putExtra("top", top);
                                intent.putExtra("width", w);
                                intent.putExtra("height", h);
                                intent.putExtra("time", fieldvalue[11]);
                                intent.putExtra("recogType", recogType);
                                new FrameCapture(intentNV21data, preWidth,
                                        preHeight, "10");
                                startActivity(intent);
                                PlateRecognitionActivity.this.finish();

                                //   }

                            } else {
                                String itemString = "";

                                mVibrator = (Vibrator) getApplication()
                                        .getSystemService(
                                                Service.VIBRATOR_SERVICE);
                                mVibrator.vibrate(100);
                                closeCamera();
                                Intent intent = new Intent(
                                        PlateRecognitionActivity.this,
                                        MainActivity.class);
                                for (int i = 0; i < lenght; i++) {

                                    itemString = fieldvalue[0];
                                    resultString = itemString.split(";");
                                    number += resultString[i] + ";\n";

                                    itemString = fieldvalue[1];
                                    // resultString
                                    // =
                                    // itemString.split(";");
                                    color += resultString[i] + ";\n";
                                    itemString = fieldvalue[11];
                                    resultString = itemString.split(";");
                                    //

                                }

                                intent.putExtra("number", number);
                                intent.putExtra("color", color);
                                intent.putExtra("time", resultString);
                                intent.putExtra("recogType", recogType);
                                PlateRecognitionActivity.this.finish();
                                startActivity(intent);


                            }
                        }
                    }

                }

            } else
            // 未检测到车牌时执行下列代码
            {
                if (!recogType)
                // 预览识别执行下列代码 不是预览识别 不做处理等待下一帧
                {
                    ;
                    if (picData != null) {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inPurgeable = true;
                        options.inInputShareable = true;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        YuvImage yuvimage = new YuvImage(picData,
                                ImageFormat.NV21, preWidth, preHeight, null);
                        yuvimage.compressToJpeg(new Rect(0, 0, preWidth,
                                preHeight), 100, baos);
                        bitmap = BitmapFactory.decodeByteArray(
                                baos.toByteArray(), 0, baos.size(), options);

                        Matrix matrix = new Matrix();
                        matrix.reset();
                        if (rotation == 90) {
                            matrix.setRotate(90);
                        } else if (rotation == 180) {
                            matrix.setRotate(180);
                        } else if (rotation == 270) {
                            matrix.setRotate(270);
                            //
                        }
                        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
                                bitmap.getWidth(), bitmap.getHeight(), matrix,
                                true);
                        path = savePicture(bitmap1);

                        if (fieldvalue[11] != null
                                && !fieldvalue[11].equals("")) {
                            int time = Integer.parseInt(fieldvalue[11]);
                            time = time / 1000;
                            timeString = "" + time;
                        } else {
                            timeString = "null";
                        }

                        //   if (null != fieldname) {
                        mVibrator = (Vibrator) getApplication()
                                .getSystemService(Service.VIBRATOR_SERVICE);
                        mVibrator.vibrate(100);
                        closeCamera();
                        Intent intent = new Intent(
                                PlateRecognitionActivity.this,
                                MainActivity.class);
                        number = fieldvalue[0];
                        color = fieldvalue[1];
                        if (fieldvalue[0] == null) {
                            number = "null";
                        }
                        if (fieldvalue[1] == null) {
                            color = "null";
                        }
                        int left = prp.plateIDCfg.left;
                        int top = prp.plateIDCfg.top;
                        int w = prp.plateIDCfg.right - prp.plateIDCfg.left;
                        int h = prp.plateIDCfg.bottom - prp.plateIDCfg.top;

                        intent.putExtra("number", number);
                        intent.putExtra("color", color);
                        intent.putExtra("path", path);
                        intent.putExtra("left", left);
                        intent.putExtra("top", top);
                        intent.putExtra("width", w);
                        intent.putExtra("height", h);
                        intent.putExtra("time", fieldvalue[11]);
                        intent.putExtra("recogType", recogType);
                        PlateRecognitionActivity.this.finish();
                        startActivity(intent);

                        //  }
                    }
                }
            }
        }

        nRet = -1;
        fieldvalue = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera != null) {

            initCamera(holder, initPreWidth, initPreHeight);
            //扫描框的UI布局
            myViewfinderView = new PlateViewfinderView(this, true, preWidth, preHeight);
            layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;
            myViewfinderView.setLayoutParams(layoutParams);
            re.addView(myViewfinderView);
            if (SharedPreferencesHelper.getBoolean(PlateRecognitionActivity.this,
                    "isOpenFlash_plate", false)) {
                iv_camera_flash.setImageResource(R.drawable.flash_off);
                CameraSetting.getInstance(this).openCameraFlash(camera);
            } else {
                iv_camera_flash.setImageResource(R.drawable.flash_on);
                CameraSetting.getInstance(this).closedCameraFlash(camera);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initCamera(SurfaceHolder holder, int setPreWidth, int setPreHeight) {
        determineDisplayOrientation();
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        Camera.Size size;
        int length = list.size();
        int previewWidth = 480;
        int previewheight = 640;
        int second_previewWidth = 0;
        int second_previewheight = 0;

        if (length == 1) {
            //设备只有一组预览分辨率
            size = list.get(0);
            previewWidth = size.width;
            previewheight = size.height;
        } else {
            for (int i = 0; i < length; i++) {
                size = list.get(i);
                // System.out.println("宽   "+size.width+"   高"+size.height);

                if (size.height <= setPreHeight || size.width <= setPreWidth) {

                    second_previewWidth = size.width;
                    second_previewheight = size.height;

                    if (previewWidth <= second_previewWidth) {
                        //横屏下
//                        if (width > height) {
//                            if (second_previewWidth * surfaceView.getHeight() == second_previewheight * surfaceView.getWidth()) {
//                                previewWidth = second_previewWidth;
//                                previewheight = second_previewheight;
//                                sameProportion = true;
//                            }
//                        }
                        //竖屏下
                        if (height > width) {

                            if (second_previewWidth * surfaceView.getWidth() == second_previewheight * surfaceView.getHeight()) {
                                previewWidth = second_previewWidth;
                                previewheight = second_previewheight;
                                sameProportion = true;

                            }
                        }
                    }
                }
            }
            if (!sameProportion) {
                for (int i = 0; i < length; i++) {
                    size = list.get(i);
                    if (size.height <= setPreHeight || size.width <= setPreWidth) {

                        second_previewWidth = size.width;
                        second_previewheight = size.height;

                        if (previewWidth <= second_previewWidth) {
                            previewWidth = second_previewWidth;
                            previewheight = second_previewheight;
                        }
                    }
                }
            }
        }
        preWidth = previewWidth;
        preHeight = previewheight;
        System.out.println("预览分辨率：" + preWidth + "    " + preHeight);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPreviewSize(preWidth, preHeight);
        if (parameters.getSupportedFocusModes().contains(
                parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                && !isAutoFocus) {
            isAutoFocus = false;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            parameters
                    .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (parameters.getSupportedFocusModes().contains(
                parameters.FOCUS_MODE_AUTO)) {
            isAutoFocus = true;
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        camera.setParameters(parameters);
        camera.setDisplayOrientation(rotation);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setPreviewCallback(PlateRecognitionActivity.this);
        camera.startPreview();

    }
    /**
     * 设置旋转角度
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    private void determineDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);

        int rotation = this.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;

                break;
            }
        }

        int displayOrientation;

        // Camera direction
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(displayOrientation);

    }
    /**
     * @return void 返回类型
     * @throws
     * @Title: closeCamera
     * @Description: TODO(这里用一句话描述这个方法的作用) 关闭相机
     */
    private void closeCamera() {
        // TODO Auto-generated method stub
        System.out.println("关闭相机 ");
        synchronized (this) {
            try {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 闪光灯触发事件
            case R.id.iv_camera_flash:
                if (SharedPreferencesHelper.getBoolean(this,
                        "isOpenFlash_plate", false)) {
                    iv_camera_flash.setImageResource(R.drawable.flash_on);
                    SharedPreferencesHelper.putBoolean(this,
                            "isOpenFlash_plate", false);
                    CameraSetting.getInstance(this).closedCameraFlash(camera);
                } else {
                    SharedPreferencesHelper.putBoolean(this,
                            "isOpenFlash_plate", true);
                    iv_camera_flash.setImageResource(R.drawable.flash_off);
                    CameraSetting.getInstance(this).openCameraFlash(camera);
                }
                break;
            // 返回按钮触发事件
            case R.id.iv_camera_back:
                closeCamera();
                finish();

                break;

            // 拍照按钮触发事件
            case R.id.imbtn_takepic:
          /*  sd    isTakePic = true;
                msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);*/

                break;
        }
    }

    //获取屏幕尺寸
    public void getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public String savePicture(Bitmap bitmap) {
        String strCaptureFilePath = SDUtils.PATH + "plateID_" + SDUtils.pictureName() + ".jpg";
        File dir = new File(SDUtils.PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCaptureFilePath;
    }

    /**
     * 设置自动对焦
     */
    public void OpenCameraAndSetParameters() {
        try {
            if (null == camera) {
                camera = Camera.open();
            }
            if (timer == null) {
                timer = new TimerTask() {
                    public void run() {
                        // isSuccess=false;
                        if (camera != null) {
                            try {
                                camera.autoFocus(new Camera.AutoFocusCallback() {
                                    public void onAutoFocus(boolean success,
                                                            Camera camera) {
                                        // isSuccess=success;

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    ;
                };
            }
            time = new Timer();
            time.schedule(timer, 500, 2500);
            if (!isFirstIn) {
                initCamera(surfaceHolder, initPreWidth, initPreHeight);
            }
            isFirstIn = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // 实时监听屏幕旋转角度
//        int uiRot = getWindowManager().getDefaultDisplay().getRotation();// 获取屏幕旋转的角度
//        if (uiRot != tempUiRot) {
//            Message mesg = new Message();
//            mesg.what = uiRot;
//            handler.sendMessage(mesg);
//            tempUiRot = uiRot;
//        }
        if (setRecogArgs) {
            Intent authIntent = new Intent(PlateRecognitionActivity.this,
                    PlateRecogService.class);
            bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
            setRecogArgs = false;
        }
        if (iInitPlateIDSDK == 0) {
            prp.height = preHeight;//
            prp.width = preWidth;//
            // 开发码
            prp.devCode = "5YYX5LQS5PIT5RO";

            if (cameraRecogUtill) {
                // 拍照识别 在使用根据图片路径识别时 执行下列代码
                if (isCamera) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21,
                            preWidth, preHeight, null);
                    yuvimage.compressToJpeg(
                            new Rect(0, 0, preWidth, preHeight), 100, baos);
                    bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(),
                            0, baos.size(), options);
                    Matrix matrix = new Matrix();
                    matrix.reset();
                    if (rotation == 90) {
                        matrix.setRotate(90);
                    } else if (rotation == 180) {
                        matrix.setRotate(180);
                    } else if (rotation == 270) {
                        matrix.setRotate(270);
                        //
                    }
                    bitmap1 = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                    bitmap.getHeight(), matrix, true);
                    path = savePicture(bitmap1);
                    prp.pic = path;
                    fieldvalue = recogBinder.doRecogDetail(prp);
                    nRet = recogBinder.getnRet();
                    if (nRet != 0) {
                        Toast.makeText(PlateRecognitionActivity.this, "验证失败2222", Toast.LENGTH_SHORT).show();
                        //  feedbackWrongCode();
                    } else {

                        number = fieldvalue[0];
                        color = fieldvalue[1];
                        mVibrator = (Vibrator) getApplication()
                                .getSystemService(Service.VIBRATOR_SERVICE);
                        mVibrator.vibrate(100);
                        closeCamera();
                        // 此模式下跳转 请到MemoryResultActivity 更改下代码 有注释注意查看
                        Intent intent = new Intent(PlateRecognitionActivity.this,
                                MainActivity.class);
                        intent.putExtra("number", number);
                        intent.putExtra("color", color);
                        intent.putExtra("path", path);
                        // intent.putExtra("time", fieldvalue[11]);
                        intent.putExtra("recogType", false);
                        startActivity(intent);
                        PlateRecognitionActivity.this.finish();
                    }
                }
            } else {
                // System.out.println("视频流识别模式");

                prp.picByte = data;
                picData = data;
                //   if (rotation == 0) {
                // 通知识别核心,识别前图像应先旋转的角度
                prp.plateIDCfg.bRotate = 0;
                setHorizontalRegion();
//                } else if (rotation == 90) {
//
//                    prp.plateIDCfg.bRotate = 1;
//                    setLinearRegion();
//
//                } else if (rotation == 180) {
//                    prp.plateIDCfg.bRotate = 2;
//                    setHorizontalRegion();
//                } else if (rotation == 270) {
//                    prp.plateIDCfg.bRotate = 3;
//                    setLinearRegion();
//                }
                if (isCamera) {
                    // 进行授权验证 并开始识别

                    fieldvalue = recogBinder.doRecogDetail(prp);

                    nRet = recogBinder.getnRet();

                    if (nRet != 0) {
                        String[] str = {"" + nRet};
                        getResult(str);
                    } else {
                        getResult(fieldvalue);
                        intentNV21data = data;
                    }

                }
            }
        }
    }

    // 设置横屏时的识别区域
    //+ 150
    //+ 50
    private void setHorizontalRegion() {

        prp.plateIDCfg.left = preWidth / 2 - (myViewfinderView.length ) * preHeight / surfaceView.getHeight();

        prp.plateIDCfg.right = preWidth / 2 + (myViewfinderView.length ) * preHeight
                / surfaceView.getHeight();
        prp.plateIDCfg.top = preHeight / 2 - myViewfinderView.length * preHeight / surfaceView.getHeight();
        prp.plateIDCfg.bottom = preHeight / 2 + myViewfinderView.length * preHeight
                / surfaceView.getHeight();
//        prp.plateIDCfg.left = preWidth / 2 - (myViewfinderView.framelength) * preWidth / surfaceView.getWidth();
//
//        prp.plateIDCfg.right = preWidth / 2 +( myViewfinderView.framelength) * preWidth
//                / surfaceView.getWidth();
//        prp.plateIDCfg.top = preHeight / 2 - (myViewfinderView.frameHight/2) * preHeight / surfaceView.getHeight();
//        prp.plateIDCfg.bottom = preHeight / 2 +(myViewfinderView.frameHight/2) * preHeight
//                / surfaceView.getHeight();
//		System.out.println("左  ："+prp.plateIDCfg.left+"   右  ："+prp.plateIDCfg.right+"     高："+prp.plateIDCfg.top+"    底："+prp.plateIDCfg.bottom);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeCamera();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
