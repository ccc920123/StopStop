package com.cdjysd.stopstop.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.cdjysd.stopstop.R;


/**
 * 开始界面圆形旋转，扩散
 * @author Administrator
 *
 */

public class SplshView extends View {
	/**
	 * 旋转动画大圆半径
	 */
	private float mRotationRadius = 60;
	/**
	 * 旋转小圆半径
	 */
	private float mCircleRadius = 12;
	/**
	 * 小圆颜色
	 */
	private int[] mCircleColors;
	/**
	 * 旋转时间
	 */
	private long mRotationDuration = 1200;
	/**
	 * 扩散动画时间
	 */
	@SuppressWarnings("unused")
	private long mSplshDuration = 1200;
	/**
	 * 背景颜色
	 */
	private int mSplshBackColor ;
	/**
	 * 空心圆半径
	 */
	private float mHoleRadius = 0f;
	/**
	 * 当前大圆旋转弧度
	 */
	private float mCurrentRotationAngle = 0f;
	/**
	 * 当前大圆半径
	 */
	private float mCurrentRotationRadius = mRotationRadius;
	/**
	 * 绘制圆的画笔
	 */
	private Paint mPaint = new Paint();
	/**
	 * 绘制背景的画笔
	 */
	private Paint mPaintBackground = new Paint();
	/**
	 * 屏幕正中心X坐标
	 */
	private float mCenterX;
	/**
	 * 屏幕正中心Y坐标
	 */
	private float mCenterY;
	/**
	 * 屏幕对角线的一半
	 */
	private float mDiagonalDist;
	/**
	 * 保存当前动画状态，共有三个动画
	 */
	private SplshState mState = null;

	/**
	 * 采用模板模式，不同状态执行不同的动画
	 *
	 * @author Administrator
	 *
	 */
	private abstract class SplshState {
		public abstract void drawState(Canvas canvas);
	}

	public SplshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public SplshView(Context context) {
		super(context);
		//
		init(context);
	}

	/**
	 * 初始化参数
	 */
	private void init(Context context) {
		// 颜色初始化
		mCircleColors = context.getResources().getIntArray(
				R.array.splsh_circle_color);
		// 画笔初始化
		mPaint.setAntiAlias(true);
		mPaintBackground.setStyle(Style.STROKE);// 粗线条
		mSplshBackColor=context.getResources().getColor(R.color.transparent);
		mPaintBackground.setColor( mSplshBackColor);

	}

	@Override
	// 图像画出后调用该方法 View显示出来
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCenterX = w / 2f;
		mCenterY = h / 2f;
		mDiagonalDist = (float) Math.sqrt(w * w + h * h);
	}

	/**
	 * 旋转动画消失
	 */
	public void splshDisapaer() {
		mState = new MergingState();
		//invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 判断当前是否是第一个动画
		if (mState == null) {
			mState = new RotationState();// 执行第一个动画
		}
		mState.drawState(canvas);
	}

	/**
	 *
	 * @author Administrator 旋转动画
	 */
	private class RotationState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		private RotationState() {
			// 如1200ms转动2π
			// 估值器
			animator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
			animator.setDuration(mRotationDuration);
			//animator.setInterpolator(new OvershootInterpolator(50f));
			animator.setInterpolator(new LinearInterpolator());
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// 得到某个时间点 转多少度
					mCurrentRotationAngle = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			animator.setRepeatCount(ValueAnimator.INFINITE);// 无限次数的旋转
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			// 执行3步骤
			// 1.清空画板 2.绘制小圆 3.旋转小圆
			drawBackGround(canvas);
			drawCircle(canvas);

		}

	}

	/**
	 * 扩散动画
	 *
	 * @author Administrator
	 *
	 */
	private class ExpandingState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		private ExpandingState() {
			// 空心圆扩散动画 ，半径为屏幕对角线的一半
			// 花多少秒 大圆半径从0—到对角线的一半
			animator = ValueAnimator.ofFloat(0, mDiagonalDist);
			animator.setDuration(mRotationDuration);
			// animator.setInterpolator(new OvershootInterpolator(10f));// 弹射效果
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// 得到某个时间点 大圆的半径
					mHoleRadius = (Float) animation.getAnimatedValue();
					invalidate();
				}
			});
			// animator.setRepeatCount(ValueAnimator.INFINITE);//无限次数的旋转
			// animator.addListener(new AnimatorListenerAdapter() {
			// @Override//监听该动画结束
			// public void onAnimationEnd(Animator animation) {
			// super.onAnimationEnd(animation);
			// mState=new ExpandingState();
			// invalidate();
			// }
			// });
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			// 绘制背景空心圆
			drawBackGround(canvas);
		}

	}

	/**
	 *
	 * @author Administrator 聚合动画
	 */
	private class MergingState extends SplshState {
		private ValueAnimator animator;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private MergingState() {
			// 花多少秒 半径R-到0
			animator = ValueAnimator.ofFloat(mRotationRadius, 0);
			animator.setDuration(mRotationDuration );
			animator.setInterpolator(new OvershootInterpolator(50f));// 弹射效果
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@SuppressLint("NewApi")
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// 得到某个时间点 大圆的半径
					mCurrentRotationRadius = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			// animator.setRepeatCount(ValueAnimator.INFINITE);//无限次数的旋转
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				// 监听该动画结束
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					mState = new ExpandingState();
					invalidate();
				}
			});
			animator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			drawBackGround(canvas);
			drawCircle(canvas);
		}

	}

	/**
	 * 清空画板
	 *
	 * @param canvas
	 */
	public void drawBackGround(Canvas canvas) {
		if (mHoleRadius > 0f) {
			/**
			 * 绘制背景空心圆
			 *
			 * @param canvas
			 */
			// 画笔宽度=对角线一半-空心圆的半径
			float strokewidth = mDiagonalDist / 2 - mHoleRadius;
			// 圆的半径=对角线的一半+画笔宽度的一半
			float radius =mHoleRadius + strokewidth / 2;
			mPaintBackground.setStrokeWidth(strokewidth);
			canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
		}else{
			canvas.drawColor(mSplshBackColor);
		}

	}



	/**
	 * 绘制小圆
	 *
	 * @param canvas
	 */
	public void drawCircle(Canvas canvas) {
		// 小圆相隔多少度
		float rotationAngle = (float) (Math.PI * 2 / mCircleColors.length);
		for (int i = 0; i < mCircleColors.length; i++) {
			/**
			 * x=R*cos(a)+mcx; y=R*sin(a)+mcy;
			 */
			// 角度=旋转之前的+旋转之后的，第几个还要乘以几
			double angle = i * rotationAngle + mCurrentRotationAngle;
			float cx = (float) (mCurrentRotationRadius * Math.cos(angle) + mCenterX);
			//
			// float cy= (float)
			// (mCurrentRotationAngle*Math.sin(angle)+mCenterY);
			float cy = (float) (mCurrentRotationRadius * Math.sin(angle) + mCenterY);
			mPaint.setColor(mCircleColors[i]);
			canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
		}
	}
}
