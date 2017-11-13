package com.puti.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;

/**
 * Created by xjbin on 2017/4/26 0026.
 *
 * 事件详情 圆环统计图
 */

public class CircularRingView extends View{

    private Context mContext;

    private float mViewHeight;
    private float mViewWidth;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private int[] radianNum;

    private int[] colors = new int[]{0x99ff2600,0x99ff9300,0x9900caff,0x992b9313,0x9971df88,0x998d6314};

    public CircularRingView(Context context) {
        this(context,null,0);
    }

    public CircularRingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircularRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (radianNum != null && radianNum.length > 0){

            Rect rect = new Rect(0,0,(int)mViewWidth,(int)mViewHeight);
            canvas.clipRect(rect);
            canvas.drawColor(Color.WHITE);

            Paint ringBgPaint = new Paint();
            ringBgPaint.setAntiAlias(true);
            ringBgPaint.setStyle(Paint.Style.STROKE);
            ringBgPaint.setStrokeWidth(DisPlayUtil.dip2px(mContext,20));
            ringBgPaint.setColor(Color.parseColor("#f0f0f2"));
            ringBgPaint.setStrokeWidth(DisPlayUtil.dip2px(mContext,20));
            canvas.drawCircle(mCenterX,mCenterY,mRadius,ringBgPaint);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DisPlayUtil.dip2px(mContext,20));

            RectF rectF = new RectF(mCenterX - mRadius, mCenterX - mRadius, mCenterX + mRadius, mCenterX + mRadius);

            double startAngle = 0;
            double all =0;
            for (int i = 0 ; i < radianNum.length ; i++){
                double num = radianNum[i];
                all = all+ num;
                paint.setColor(colors[i]);

                if (i != 0) {
                    startAngle = startAngle + radianNum[i-1];
                }
                canvas.drawArc(rectF,(int) startAngle,(int) num,false,paint);
            }
            LogUtil.i("all ",all+"");

        }
    }


    /**
     *
     * @param/ value 数组元素值是  该数据值 与 数据总和 百分比
     */
    public void setDatas(double[] percentArray){

        if (percentArray != null){
            radianNum = new int[percentArray.length];
            for (int i = 0; i< percentArray.length;i++){
                radianNum[i] = (int)(percentArray[i] * 360);
            }
        }
    }

    /**
     *
     * @param height 单位px
     * @param width
     */
    public void setViewWidthAndHeight(int height,int width){

        this.mViewHeight = height;
        this.mViewWidth = width;
        mCenterX = this.mViewWidth/2;
        mCenterY = this.mViewHeight/2;
        mRadius = Math.min(this.mViewHeight,this.mViewWidth)/2 * 0.8f;
    }

    public void setViewInvalidate(){
        invalidate();
    }

}
