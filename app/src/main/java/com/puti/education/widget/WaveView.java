package com.puti.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.puti.education.util.DisPlayUtil;

/**
 * 声波图
 *
 */

public class WaveView extends View {

    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private int radius;

    private int count = 6;
    private float angle = (float) (Math.PI*2/count);//每个模块横跨的幅度

    private int outColor = 0x20FF0000;//最外层颜色
    private int inSideColor = 0X40FF0000;//最外第二层颜色
    private int thirdSideColor = 0X80FF0000;//最三层颜色
    private int fourthSideColor = 0XA0FF0000;//最三层颜色
    private int fifthSideColor = 0XFFFF0000;//最三层颜色
    private int lineColor = 0X80FFFFFF;

    private Context context;
    private String mTextArray[] = {"一","二","三","四","五","六"};
    private double mValue[];
    private String mCenterValue;


    public WaveView(Context context) {
        this(context,null,0);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        radius = (int)(Math.min(h, w)/2 * 0.6f);
        //中心坐标
        centerX = w/2;
        centerY = h/2;
        postInvalidate();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        drawCircle(canvas);

    }


    private void drawCircle(Canvas canvas){
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(outColor);
        int step = 15;
        int stepLast = 0;

        RectF rectF;
        Path mAnimPath;

        for (int i = 1;i<6; i++){
            stepLast = i*step;
            rectF = new RectF(centerX - radius+stepLast, centerY - radius+stepLast, centerX + radius-stepLast, centerY + radius-stepLast);   //画矩形
            mAnimPath = new Path();
            mAnimPath.addArc(rectF, 50, 359);//第一个参数是你弧形的画的矩形范围，就是你的圆弧画满后是内切的//第二个参数是起始的角度//第三个参数是移动的度数 ，所以终点度数是第二个参数加上第三个参数
            mAnimPath.close();
            canvas.drawPath(mAnimPath, mPaint);
        }



    }



}
