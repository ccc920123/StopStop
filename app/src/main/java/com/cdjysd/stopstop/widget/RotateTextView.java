package com.cdjysd.stopstop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class RotateTextView extends TextView {
    private int mDegrees=0;
    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(mDegrees, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
    public void setDegrees(int degrees) {
        mDegrees = degrees;
    }
}

