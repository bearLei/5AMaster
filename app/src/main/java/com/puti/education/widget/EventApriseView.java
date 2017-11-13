package com.puti.education.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.puti.education.R;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;

import java.util.List;

/**
 * 事件详情---评价曲线图
 */

public class EventApriseView extends View{

    private int[] colors = {Color.parseColor("#36A2E9"),Color.parseColor("#BDD9F1")};

    private float lineSmoothness = 0.2f;

    private List<List<Point>> mDatas;
    private String[] mYScales;
    private String[] mXScales;
    private String[] mSigns;

    private int mViewWidth;
    private int mViewHeight;

    private int mSignTilesHeight;//线条标记占据高度
    private int mYScaleWidth;//y轴的高度
    private int mXScaleHeight;//x轴刻度占据高度
    private int mCircleRadius;//圆点半径
    private int mLineViewHeight;//绘制曲线区域的高度
    private int mLineViewWidth;//宽度

    private Context mContext;


    public EventApriseView(Context context) {
        this(context,null,0);
    }

    public EventApriseView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EventApriseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mSignTilesHeight = DisPlayUtil.dip2px(context,60);
        mXScaleHeight = DisPlayUtil.dip2px(context,60);
        mYScaleWidth = DisPlayUtil.dip2px(context,35);
        mCircleRadius = DisPlayUtil.dip2px(context,5);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Rect rect = new Rect(0,0,(int)mViewWidth,(int)mViewHeight);
        canvas.clipRect(rect);
        canvas.drawColor(Color.WHITE);

        drawSignTitle(canvas,mSigns);
        drawYscalesAndXLine(canvas);
        drawXscales(canvas);
        setBeierLine(canvas);
    }

    private  void drawSignTitle(Canvas canvas,String[] signTitleArray){

        if (signTitleArray != null && signTitleArray.length > 0){
            Paint cpaint = new Paint();
            cpaint.setTextSize(DisPlayUtil.dip2px(mContext,14));
            cpaint.setColor(Color.parseColor("#36A2E9"));
            cpaint.setAntiAlias(true);

            Paint textPaint = new Paint();
            textPaint.setTextSize(DisPlayUtil.dip2px(mContext,10));
            textPaint.setColor(Color.parseColor("#565656"));
            textPaint.setAntiAlias(true);

            int centerY = mSignTilesHeight/2;
            int start = DisPlayUtil.dip2px(mContext,6);
            int textMargin = DisPlayUtil.dip2px(mContext,6);
            int betweenMargin = DisPlayUtil.dip2px(mContext,17);

            Rect rect = new Rect();
            cpaint.getTextBounds(signTitleArray[1],0,signTitleArray[1].length(),rect);
            int textWidth = rect.width();
            int textHeight = rect.height();

            Point c1 = new Point(start,centerY);
            canvas.drawCircle(c1.x,c1.y,mCircleRadius,cpaint);//第一个标记
            Point tp1 = new Point(start+mCircleRadius*2+textMargin,centerY);
            canvas.drawText(signTitleArray[0],tp1.x,tp1.y+ textHeight/4,textPaint);

            cpaint.setColor(Color.parseColor("#BDD9F1"));
            Point c2 = new Point(tp1.x + betweenMargin+textWidth,centerY);
            canvas.drawCircle(c2.x,centerY,mCircleRadius,cpaint);
            Point tp2 = new Point(c2.x + textMargin,centerY);
            canvas.drawText(signTitleArray[1],tp2.x + textMargin,tp2.y + textHeight/4,textPaint);
        }

    }

    //y坐标轴 ，坐标线
    private void drawYscalesAndXLine(Canvas canvas){

        int average = mLineViewHeight/2;

        Paint ypaint = new Paint();
        ypaint.setColor(Color.parseColor("#cacaca"));
        ypaint.setTextSize(DisPlayUtil.sp2px(mContext,10));
        ypaint.setAntiAlias(true);

        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.window_bg));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setAntiAlias(true);

        int height = DisPlayUtil.getFontSize(ypaint);

        for (int i = 0;i<3;i++){
            //y轴刻度
            Point point = new Point(0,mViewHeight - mXScaleHeight- i* average);
            canvas.drawText(mYScales[i],point.x,point.y+height/4,ypaint);

            //坐标线
            Point lp1 = new Point(point.x + mYScaleWidth,point.y);
            canvas.drawLine(lp1.x,lp1.y,lp1.x + mLineViewWidth,lp1.y,linePaint);
        }

    }

    //绘制x轴坐标
    private void drawXscales(Canvas canvas){

        Paint textPaint = new Paint();
        textPaint.setTextSize(DisPlayUtil.sp2px(mContext,10));
        textPaint.setColor(Color.parseColor("#cacaca"));
        textPaint.setAntiAlias(true);

        int average = (mLineViewWidth - DisPlayUtil.dip2px(mContext,30)) / 5;

        for (int i = 0 ;i < mXScales.length;i++){
            canvas.drawText(mXScales[i],mYScaleWidth + i*average, mViewHeight - mXScaleHeight/2,textPaint);
        }

    }

    //绘制曲线
    private void setBeierLine(Canvas canvas){

        if (mDatas != null && mDatas.size() > 0){

            for (int i = 0;i< mDatas.size();i++){
                Path path = new Path();
                Paint linePaint = new Paint();
                linePaint.setStyle(Paint.Style.STROKE);
                linePaint.setStrokeWidth(5);
                linePaint.setColor(colors[i]);
                linePaint.setAntiAlias(true);
                measurePath(mDatas.get(i),canvas,path,linePaint);
            }
        }
    }

    public void setDatas(List<List<Point>> pointList){
        if (pointList != null && pointList.size() > 0){
            this.mDatas = pointList;
        }
        invalidate();
    }

    public void setAxisScaleArrayAndSign(String[] yScales,String[] xScales,String[] sign){
        this.mYScales = yScales;
        this.mXScales = xScales;
        this.mSigns = sign;
    }

    public void setViewWidthAndHeight(int height,int width){
        this.mViewHeight = height;
        this.mViewWidth = width;
        this.mLineViewHeight = mViewHeight - mSignTilesHeight - mXScaleHeight;
        this.mLineViewWidth = mViewWidth - mYScaleWidth;
        LogUtil.i("view value",""+ mViewHeight +"  "+ mSignTilesHeight +"  "+ mXScaleHeight);
    }

    public int getLineViewHeight(){
        return mLineViewHeight;
    }

    public int getLineViewWidth(){
        return mLineViewWidth;
    }

    public int getYScaleWidth(){
        return mYScaleWidth;
    }

    public int getXScaleHeight(){
        return mXScaleHeight;
    }

    public int getmSignTilesHeight() {
        return mSignTilesHeight;
    }

    private void measurePath(List<Point> pointList, Canvas canvas, Path path, Paint paint) {

        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = pointList.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = pointList.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = pointList.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = pointList.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = pointList.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                path.moveTo(currentPointX, currentPointY);
                //mAssistPath.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY, currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }

        canvas.drawPath(path, paint);
    }

    public void setViewInvalidate(){
        invalidate();
    }
}
