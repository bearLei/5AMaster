package com.puti.education.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.puti.education.bean.YearEventLineData;
import com.puti.education.bean.YearEventsItem;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 年度事件统计图
 */

public class YearEventView extends View {

    private float lineSmoothness = 0.0f;

    private int mXscaleLableColor;
    private int mLineColor;

    private String[] mxscaleLableArray;

    private List<Point> mPointList;
    private List<Point> mOtherPointList;

    private int mChartLineRangeHeight;//绘制曲线的整体高度，排除标题
    private int mChartXscaleRangeHeigth;// x轴刻度的高度

    private int chartWidth;
    private int chartHeight;

    private Paint scalePaint;
    private Paint yScalePaint;
    private Path mPath;
    private Paint linePaint;

    private Path otherPath;
    private Paint otherPaint;
    private int mLableTextSize = 22;

    private int partWidth;
    private float RATIO;
    private final int partCount = 11;
    private int yLableWidth = 50;//纵坐标的宽度
    private int mYlableWithXlableOffset;//y轴与x轴的间距

    private Context mContext;

    private String[] mYscaleLableArray;

    public YearEventView(Context context) {
        this(context, null, 0);
    }

    public YearEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        RATIO = DisPlayUtil.getPaintTextSizeRadio((Activity) context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.week_event);
        mLineColor = typedArray.getColor(R.styleable.week_event_undealinecolor, Color.parseColor("#67CDDC"));
        mXscaleLableColor = typedArray.getColor(R.styleable.week_event_xlablecolor, Color.parseColor("#B7B7B7"));
        typedArray.recycle();

        init();
    }

    private void init(){
        yLableWidth = DisPlayUtil.dip2px(mContext,24);
        mChartXscaleRangeHeigth = DisPlayUtil.dip2px(mContext,50);
        mLableTextSize = DisPlayUtil.dip2px(mContext,8);
        mYlableWithXlableOffset = DisPlayUtil.dip2px(mContext,15);
    }


    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        chartWidth = w;
        chartHeight = h;
        partWidth = (chartWidth - yLableWidth) / partCount;
        mChartLineRangeHeight = chartHeight - mChartXscaleRangeHeigth ;

        super.onSizeChanged(w, h, oldw, oldh);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        drawView(canvas);
    }

    public void drawView(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        Rect rect = new Rect();
        getDrawingRect(rect);
        chartWidth = rect.width();
        chartHeight = rect.height();

        partWidth = (chartWidth - yLableWidth) / partCount;
        mChartLineRangeHeight = chartHeight - mChartXscaleRangeHeigth ;

        mPath = new Path();
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        linePaint.setColor(Color.parseColor("#67cddc"));

        otherPath = new Path();
        otherPaint = new Paint();
        otherPaint.setAntiAlias(true);
        otherPaint.setStyle(Paint.Style.STROKE);
        otherPaint.setStrokeWidth(5);
        otherPaint.setColor(Color.parseColor("#f18c17"));

        scalePaint = new Paint();
        scalePaint.setColor(mXscaleLableColor);
        scalePaint.setAntiAlias(true);
        scalePaint.setTextSize(mLableTextSize);

        yScalePaint = new Paint();
        yScalePaint.setColor(mXscaleLableColor);
        yScalePaint.setAntiAlias(true);
        yScalePaint.setTextSize(mLableTextSize);
        yScalePaint.setTextAlign(Paint.Align.RIGHT);

        drawXscales(canvas);
        drawYscales(canvas);
        drawLine(canvas);
    }


    private void drawLine(Canvas canvas) {

        if (mPointList != null && mPointList.size() > 0) {
            buildPath(canvas,mPath,mPointList,linePaint);
        }

        if (mOtherPointList != null && mOtherPointList.size() > 0) {
            buildPath(canvas,otherPath,mOtherPointList,otherPaint);
        }

    }


    private void drawXscales(Canvas canvas) {

        mxscaleLableArray =new String[] {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        int length = mxscaleLableArray.length;

        for (int i = 0; i < length; i++) {
            float textWidth = scalePaint.measureText(mxscaleLableArray[i]);
            float halfTextWidth = textWidth / 2;
            int y = chartHeight - mChartXscaleRangeHeigth + DisPlayUtil.dip2px(mContext,20);
            if (i == 0) {
                canvas.drawText(mxscaleLableArray[i], i * partWidth + yLableWidth,y , scalePaint);
            } else if (i == length - 1) {
                canvas.drawText(mxscaleLableArray[i], i * partWidth + yLableWidth - textWidth, y, scalePaint);
            } else {
                canvas.drawText(mxscaleLableArray[i], i * partWidth + yLableWidth - halfTextWidth, y, scalePaint);
            }
        }

    }

    private void drawYscales(Canvas canvas) {
        if (mYscaleLableArray == null){
            return;
        }
        float yPart = (mChartLineRangeHeight - mYlableWithXlableOffset) / (mYscaleLableArray.length-1) ;
        if (mYscaleLableArray != null && mYscaleLableArray.length > 0 && mPointList != null && mPointList.size() > 0) {
            int length = mYscaleLableArray.length;
            for (int i = 0; i < length; i++) {
                canvas.drawText(mYscaleLableArray[i], yLableWidth - DisPlayUtil.dip2px(mContext,10),
                        chartHeight - mChartXscaleRangeHeigth - yPart * i , yScalePaint);
            }
        }
    }


    double mMaxValue = 100;

    public void initData(final YearEventLineData yearEventLineData, final YearEventLineData otherYearEventLineData,final String[] mXscaleLableArray) {

        new Thread(){
            @Override
            public void run() {
                List<YearEventsItem> dataList = yearEventLineData.yearEventItems;
                List<YearEventsItem> otherDataList = otherYearEventLineData.yearEventItems;

                if (dataList != null && otherDataList != null && mXscaleLableArray != null) {

                    mxscaleLableArray = mXscaleLableArray;

                    mMaxValue = Math.max(getMaxValue(dataList),getMaxValue(otherDataList));//获取线条的最大值

                    mMaxValue = makeYMaxValue(mMaxValue);

                    if (mMaxValue < 0)
                        return;

                    YearEventsItem yearEventsItem = null;
                    Point point = null;

                    //第一条线
                    int size = dataList.size();
                    mPointList = new ArrayList<>();
                    double part = 0;
                    if (mMaxValue != 0){
                        part = (mChartLineRangeHeight-mYlableWithXlableOffset) / mMaxValue;
                    }

                    for (int i = 0; i < size; i++) {
                        yearEventsItem = dataList.get(i);
                        point = new Point(i * partWidth + yLableWidth, chartHeight  - mChartXscaleRangeHeigth- (int)(part *  yearEventsItem.count));
                        mPointList.add(point);
                    }

                    //第二条线
                    mOtherPointList = new ArrayList<>();
                    double otherpart = (mChartLineRangeHeight-mYlableWithXlableOffset) / mMaxValue;
                    for (int i = 0; i < size; i++) {
                        yearEventsItem = otherDataList.get(i);
                        point = new Point(i * partWidth + yLableWidth, chartHeight  - mChartXscaleRangeHeigth- (int)(otherpart *  yearEventsItem.count));
                        mOtherPointList.add(point);
                    }

                    postInvalidate();
                }
            }
        }.start();

    }

    private int getMaxValue(List<YearEventsItem> list){

        if (list== null || list.size() == 0){
            return 0;
        }

        int size = list.size();
        YearEventsItem yearEventsItem = null;
        int maxValue = 0;
        for (int i =0 ;i< size;i++){
            yearEventsItem = list.get(i);
            if (yearEventsItem.count > maxValue){
                maxValue = yearEventsItem.count;
            }
        }

        return maxValue;
    }

    private void buildPath(Canvas canvas,Path path,List<Point> adjustedPoints,Paint paint) {

        if (adjustedPoints == null || adjustedPoints.size() == 0){
            return;
        }

        path.reset();

        path.moveTo(adjustedPoints.get(0).x, adjustedPoints.get(0).y);
        int pointSize = adjustedPoints.size();

        for (int i = 0; i < adjustedPoints.size() - 1; i++) {
            float pointX = (adjustedPoints.get(i).x + adjustedPoints.get(i+1).x) / 2;
            float pointY = (adjustedPoints.get(i).y + adjustedPoints.get(i+1).y) / 2;

            float controlX = adjustedPoints.get(i).x;
            float controlY = adjustedPoints.get(i).y;

            path.quadTo(controlX, controlY, pointX, pointY);
        }
        path.quadTo(adjustedPoints.get(pointSize -1).x, adjustedPoints.get(pointSize -1).y, adjustedPoints.get(pointSize -1).x,
                adjustedPoints.get(pointSize -1).y);

        /*path.rLineTo(0,0);
        path.lineTo(adjustedPoints.get(pointSize-1).x,mChartLineRangeHeight);
        path.lineTo(adjustedPoints.get(0).x,mChartLineRangeHeight);
        path.close();*/

        canvas.drawPath(path,paint);
    }

    /*private void measurePath(List<Point> pointList, Canvas canvas, Path path, Paint paint) {

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
    }*/

    //构造y轴最大值
    private double makeYMaxValue(double realMaxValue){

        double tempMaxValue = realMaxValue;
        mYscaleLableArray = new String[5];
        if (tempMaxValue == 0){
            for (int i = 0;i<5;i++){
                mYscaleLableArray[i] = i+"";
            }
            tempMaxValue = 4;
            return tempMaxValue;
        }

        while (true){
            ++tempMaxValue;
            if (tempMaxValue > 0 && tempMaxValue > realMaxValue && tempMaxValue%4 == 0){

                int part = (int)(tempMaxValue/4);
                for (int i = 0;i<5;i++){
                    mYscaleLableArray[i] = i*part+"";
                }
                return  tempMaxValue;
            }
        }
    }

}
