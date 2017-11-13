package com.puti.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.puti.education.util.DisPlayUtil;

import java.util.ArrayList;

/**
 * 雷达图
 *
 */

public class RadarView extends View {

    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private int radius;

    private int count = 6;
    private float angle = (float) (Math.PI*2/count);//每个模块横跨的幅度

    private int[] pathColors = new int[]{0x71B2DC8B,0X71AED58B,0X71AFD989};
    private int lineColor = 0X80FFFFFF;

    private Context context;
    private String mTextArray[] = {"一","二","三","四","五","六"};
    private double mValue[];
    private String mCenterValue;

    private float mRadiusPercent = 0.6f;


    public RadarView(Context context) {
        this(context,null,0);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        radius = (int)(Math.min(h, w)/2 * mRadiusPercent);
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

        drawRegion(canvas);
        drawLine(canvas);

        //需要用户去设置
        drawRadarPointText(canvas);
        drawShdow(canvas);
        drawCenterText(canvas);

    }

    public void setmRadiusPercent(float mRadiusPercent) {
        this.mRadiusPercent = mRadiusPercent;
    }

    public void setTextContent(String []text){
        if (text != null && text.length == 6){
            mTextArray = text;
        }
        return;
    }

    public void setValueContent(double []value){
        if (value != null && value.length == 6){
            mValue = value;
        }
        return;
    }

    public void setCenterValue(String v){
        mCenterValue = v;
    }

    public void startDraw(){
        invalidate();
    }


    //绘制网格区域
    private void drawRegion(Canvas canvas){

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        float percent = 1;
        for (int j = 0 ;j < 3; j++){
            Path path = new Path();
            if (j == 1){
                percent = 0.75f;
            }else if (j == 2){
                percent = 0.5f;
            }
            for (int i = 0;i< 6;i++){
                Point point = getPoint(i,percent,0);
                if (i == 0){
                    path.moveTo(point.x,point.y);
                }else{
                    path.lineTo(point.x,point.y);
                }
            }
            path.close();
            paint.setColor(pathColors[j]);
            canvas.drawPath(path,paint);
        }
    }

    private void drawLine(Canvas canvas){

        Paint linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);

        Point point = null;
        for (int i = 0;i < 6; i++){
            point = getPoint(i,1,0);
            canvas.drawLine(centerX,centerY,point.x,point.y,linePaint);
        }
    }

    private void drawCenterText(Canvas canvas){

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(DisPlayUtil.dip2px(this.context,13));

        if (TextUtils.isEmpty(mCenterValue)){
            return;
        }
        Rect rect = new Rect();
        textPaint.getTextBounds(mCenterValue, 0, mCenterValue.length(), rect);
        int tWidth = rect.width();
        int tHeight = rect.height();

        canvas.drawText(mCenterValue,centerX - tWidth/2,centerY + tHeight/2,textPaint);
    }


    private void drawRadarPointText(Canvas canvas){

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#565656"));
        textPaint.setTextSize(DisPlayUtil.dip2px(this.context,11));

        if (mTextArray == null || mTextArray.length != 6){
            return;
        }

        int margin = DisPlayUtil.dip2px(context,10);
        for (int i = 0; i< 6;i++){
            String text = mTextArray[i];
            Point point = getPoint(i,1,0);
            if (i == 0){
                point.x = point.x - DisPlayUtil.getTextSize(mTextArray[0],textPaint).width()/2;
                point.y = point.y - margin;
            }else if (i == 1){
                point.x = point.x + margin;
            }else if (i == 2){
                point.y = point.y + DisPlayUtil.getTextSize(mTextArray[2],textPaint).height();
                point.x = point.x + margin;
            }else if (i == 3){
                point.x = point.x - DisPlayUtil.getTextSize(mTextArray[3],textPaint).width()/2;
                point.y = point.y + margin + DisPlayUtil.getTextSize(mTextArray[3],textPaint).height();
            }else if (i == 4){
                point.y = point.y + DisPlayUtil.getTextSize(mTextArray[4],textPaint).height();
                point.x = point.x - margin - DisPlayUtil.getTextSize(mTextArray[4],textPaint).width();
            }else if (i == 5){
                point.x = point.x - margin - DisPlayUtil.getTextSize(mTextArray[5],textPaint).width();
            }

            canvas.drawText(text,point.x,point.y,textPaint);
        }

    }

    /**
     * 绘制阴影部分
     */
    private void drawShdow(Canvas canvas){

        Path path = new Path();
        Paint paint = new Paint();
        paint.setColor(0x70429321);
        paint.setAntiAlias(true);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(DisPlayUtil.dip2px(this.context,10));

        if (mValue == null || mValue.length != 6){
            return;
        }
        ArrayList<Point> pointlist = new ArrayList<>();
        for (int i = 0; i< 6; i++){
            Point point = getPoint(i,mValue[i],0);
            if (i == 0){
                path.moveTo(point.x,point.y);
            }else{
                path.lineTo(point.x,point.y);
            }
            pointlist.add(point);
        }
        path.close();
        canvas.drawPath(path,paint);

        //开始对雷达上各点写数值
        for (int i=0; i<6; i++){
            Point point = pointlist.get(i);
            canvas.drawText(mValue[i] + "", point.x, point.y, textPaint);
        }
    }

    private Point getPoint(int position,double percent, int margin){

        int x = 0;
        int y = 0;

        if (position == 0) {

            x = centerX;
            y = (int)(centerY - radius * percent);

        } else if (position == 1) {

            x = (int)(centerX + Math.cos(angle / 2)*(radius * percent));
            y = (int)(centerY - Math.sin(angle/2)*(radius * percent));

        } else if (position == 2) {

            x = (int)(centerX + Math.cos(angle/2)*(radius * percent));
            y = (int)(centerY+Math.sin(angle/2)*(radius * percent));

        } else if (position == 3) {

            x = centerX;
            y = (int)(centerY + (radius* percent));

        } else if (position == 4) {

            x = (int)(centerX - Math.cos(angle/2) *(radius * percent));
            y = (int)(centerY +Math.sin(angle/2)*(radius * percent));

        }else if (position == 5){

            x = (int )(centerX - Math.cos(angle/2)*(radius * percent));
            y = (int)(centerY - Math.sin(angle/2)*(radius * percent));

        }

        return new Point(x, y);
    }

}
