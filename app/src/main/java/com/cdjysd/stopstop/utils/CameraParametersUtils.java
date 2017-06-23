package com.cdjysd.stopstop.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

/**
 * 识别时用到的相机帮助类，寻找适合识别的分辨路
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class CameraParametersUtils {
	Camera.Parameters parameters;
	public int srcWidth, srcHeight;
	public int preWidth, preHeight;
	public int picWidth, picHeight;
	public int surfaceWidth, surfaceHeight;
	List<Camera.Size> list;
	private boolean isShowBorder = false;

	public CameraParametersUtils(Context context) {

		setScreenSize(context);
	}
	/**
	 * 获取设备的预览分辨率的宽和高
	 *
	 * @param camera
	 */
	public void getCameraPreParameters(Camera camera)

	{
		isShowBorder = false;
		// 荣耀七设备
		if ("PLK-TL01H".equals(Build.MODEL)) {
			preWidth = 1920;
			preHeight = 1080;
			return;
		}
		// 其他设备
		parameters = camera.getParameters();
		list = parameters.getSupportedPreviewSizes();
		float ratioScreen = (float) srcWidth / srcHeight;

		for (int i = 0; i < list.size(); i++) {
			float ratioPreview = (float) list.get(i).width / list.get(i).height;
			if (ratioScreen == ratioPreview) {// 判断屏幕宽高比是否与预览宽高比一样，如果一样执行如下代码
				if (list.get(i).width >= 1280 || list.get(i).height >= 720) {// 默认预览以1280*720为标准
					if (preWidth == 0 && preHeight == 0) {// 初始值
						preWidth = list.get(i).width;
						preHeight = list.get(i).height;
					}
					if (list.get(0).width > list.get(list.size() - 1).width) {
						// 如果第一个值大于最后一个值
						if (preWidth > list.get(i).width
								|| preHeight > list.get(i).height) {
							// 当有大于1280*720的分辨率但是小于之前记载的分辨率，我们取中间的分辨率
							preWidth = list.get(i).width;
							preHeight = list.get(i).height;
						}
					} else {
						// 如果第一个值小于最后一个值
						if (preWidth < list.get(i).width
								|| preHeight <list.get(i).height) {
							// 如果之前的宽度和高度大于等于1280*720，就不需要再筛选了
							if (preWidth >= 1280 || preHeight >= 720) {

							} else {
								// 为了找到合适的分辨率，如果preWidth和preHeight没有比1280*720大的就继续过滤
								preWidth = list.get(i).width;
								preHeight = list.get(i).height;
							}
						}
					}
				}
			}
		}
		// 说明没有找到程序想要的分辨率
		if (preWidth == 0 || preHeight == 0) {
			isShowBorder = true;
			preWidth = list.get(0).width;
			preHeight = list.get(0).height;
			for (int i = 0; i < list.size(); i++) {

				if (list.get(0).width > list.get(list.size() - 1).width) {
					// 如果第一个值大于最后一个值
					if (preWidth >=list.get(i).width
							|| preHeight >=list.get(i).height) {
						// 当上一个选择的预览分辨率宽或者高度大于本次的宽度和高度时，执行如下代码:
						if (list.get(i).width >= 1280) {
							// 当本次的预览宽度和高度大于1280*720时执行如下代码
							preWidth = list.get(i).width;
							preHeight = list.get(i).height;

						}
					}
				} else {
					if (preWidth <= list.get(i).width
							|| preHeight <= list.get(i).height) {
						if (preWidth >= 1280 || preHeight >= 720) {

						} else {
							// 当上一个选择的预览分辨率宽或者高度大于本次的宽度和高度时，执行如下代码:
							if (list.get(i).width >= 1280) {
								// 当本次的预览宽度和高度大于1280*720时执行如下代码
								preWidth = list.get(i).width;
								preHeight = list.get(i).height;

							}
						}

					}
				}
			}
		}
		// 如果没有找到大于1280*720的分辨率的话，取集合中的最大值进行匹配
		if (preWidth == 0 || preHeight == 0) {
			isShowBorder = true;
			if (list.get(0).width > list.get(list.size() - 1).width) {
				preWidth = list.get(0).width;
				preHeight = list.get(0).height;
			} else {
				preWidth = list.get(list.size() - 1).width;
				preHeight = list.get(list.size() - 1).height;
			}
		}
		if (isShowBorder) {
			if (ratioScreen > (float) preWidth / preHeight) {
				surfaceWidth = (int) (((float) preWidth / preHeight) * srcHeight);
				surfaceHeight = srcHeight;
			} else {
				surfaceWidth = srcWidth;
				surfaceHeight = (int) (((float) preHeight / preWidth) * srcHeight);
			}
		}else{
			surfaceWidth = srcWidth;
			surfaceHeight=srcHeight;
		}
		System.out.println("surfaceWidth1:"+surfaceWidth+"--surfaceHeight1:"+surfaceHeight);
	}

	@SuppressLint("NewApi")
	private void setScreenSize(Context context) {
		int x, y;
		WindowManager wm = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE));
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point screenSize = new Point();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				display.getRealSize(screenSize);
				x = screenSize.x;
				y = screenSize.y;
			} else {
				display.getSize(screenSize);
				x = screenSize.x;
				y = screenSize.y;
			}
		} else {
			x = display.getWidth();
			y = display.getHeight();
		}

		srcWidth = x;
		srcHeight = y;
	}
	/**
	 * @param mDecorView{tags} 设定文件
	 * @return ${return_type}    返回类型
	 * @throws
	 * @Title: 沉寂模式
	 * @Description: 隐藏虚拟按键
	 */
	@TargetApi(19)
	public void hiddenVirtualButtons(View mDecorView) {
		if (Build.VERSION.SDK_INT >= 19) {
			mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_FULLSCREEN);
//					| View.SYSTEM_UI_FLAG_IMMERSIVE);
		}
	}
}
