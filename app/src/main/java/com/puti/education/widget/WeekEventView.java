package com.puti.education.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.puti.education.bean.WeekEventItem;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.LogUtil;
import com.puti.education.R;
import com.puti.education.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjbin.
 *
 * 本周事件 处理和未处理事件统计图
 *
 */

public class WeekEventView extends View {

    private float lineSmoothness = 0.0f;

    private int mXscaleLableColor;
    private int undealEventLineColor;
    private int dealEventLineColor ;

    private String[] mxscaleLableArray ;
    private String[] yLables ;

    private int mMaxValue;
    /** 光滑因子 */
    private float smoothness;

    private List<Point> mHasDealEventDataList = new ArrayList<>();
    private List<Point> mUnDealEventDataList = new ArrayList<>();

    private List<Point> mHasDealPointList = new ArrayList<>();
    private List<Point> mUnDealPointList = new ArrayList<>();

    private int mChartLineRangeHeight;//绘制曲线的整体高度，排除标题
    private int mChartTitleRangeHeight = 80;//标题栏高度
    private  int  mChartXscaleRangeHeigth;// x轴刻度的高度
    private int mYlableWidth ;//Y轴水平宽度

    private int chartWidth;
    private int chartHeight;

    private Paint mDealPaint;
    private Paint mUnDealPaint;
    private Paint mXScalePaint;
    private Paint mYscaleLablePaint;
    private Paint mTitleTextPaint;
    private Paint mCircleSignPaint;

    private Paint mHintPaint;
    private Point mMinPoint;
    private double mDisplayValue = 0;

    private Path mDealPath;
    private Path mUnDealPath;

    private int partWidth;
    private int fontSize;
    private int titleFontSize;
    private int mCircleSignMargin;
    private int mXscaleMarginTop;

    private Context mContext;



    public WeekEventView(Context context) {
        this(context,null,0);
    }

    public WeekEventView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeekEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.week_event);
        undealEventLineColor = typedArray.getColor(R.styleable.week_event_undealinecolor, Color.parseColor("#67CDDC"));
        dealEventLineColor = typedArray.getColor(R.styleable.week_event_hasdealinecolor,Color.parseColor("#ffd93c"));
        mXscaleLableColor = typedArray.getColor(R.styleable.week_event_xlablecolor,Color.parseColor("#B7B7B7"));
        typedArray.recycle();

        init();
    }


    private void init(){
        fontSize = DisPlayUtil.dip2px(mContext,10);
        titleFontSize= DisPlayUtil.dip2px(mContext,10);
        mCircleSignMargin = DisPlayUtil.dip2px(mContext,70);
        mXscaleMarginTop = DisPlayUtil.dip2px(mContext,10);
        mChartXscaleRangeHeigth = DisPlayUtil.dip2px(mContext,24);

        mYlableWidth = DisPlayUtil.dip2px(mContext,26);

        smoothness = 0.33f;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        drawTwoLine(canvas);
    }

    public void drawTwoLine(Canvas canvas){

        Rect rect = new Rect();
        getDrawingRect(rect);

        chartWidth = rect.width();
        chartHeight = rect.height();
        partWidth = (chartWidth-mYlableWidth)/6;
        mChartLineRangeHeight = chartHeight  - mChartXscaleRangeHeigth;

        canvas.drawColor(Color.WHITE);

        //Rect rect = new Rect(0,0,chartWidth,chartHeight);
        //canvas.clipRect(rect);


        mDealPaint = new Paint();
        mDealPaint.setColor(dealEventLineColor);
        mDealPaint.setAntiAlias(true);
        mDealPaint.setStyle(Paint.Style.FILL);
        mDealPath = new Path();

        mUnDealPaint = new Paint();
        mUnDealPaint.setColor(undealEventLineColor);
        mUnDealPaint.setAntiAlias(true);
        mUnDealPaint.setStyle(Paint.Style.FILL);
        mUnDealPath = new Path();

        mXScalePaint = new Paint();
        mXScalePaint.setColor(mXscaleLableColor);
        mXScalePaint.setAntiAlias(true);
        mXScalePaint.setTextSize(fontSize);

        mYscaleLablePaint = new Paint();
        mYscaleLablePaint.setColor(mXscaleLableColor);
        mYscaleLablePaint.setTextSize(fontSize);
        mYscaleLablePaint.setTextAlign(Paint.Align.RIGHT);
        mYscaleLablePaint.setAntiAlias(true);

        mTitleTextPaint = new Paint();
        mTitleTextPaint.setColor(mXscaleLableColor);
        mTitleTextPaint.setTextSize(titleFontSize);
        mTitleTextPaint.setAntiAlias(true);

        mHintPaint = new Paint();
        mHintPaint.setColor(Color.YELLOW);
        mHintPaint.setStrokeJoin(Paint.Join.ROUND);
        mHintPaint.setStrokeCap(Paint.Cap.ROUND);
        mHintPaint.setStrokeWidth(3);

        drawCircleSign(canvas);
        drawXscales(canvas);
        drawYLables(canvas);
        //drawHasDealEventLine(canvas);
        //drawUnDealEventLine(canvas);

        //buildPath(canvas,mUnDealPath,mUnDealEventDataList,mUnDealPaint);
        //buildPath(canvas,mDealPath,mHasDealEventDataList,mDealPaint);

        drawCurveAndPoints(canvas,mDealPath,mHasDealPointList,mDealPaint);
        drawCurveAndPoints(canvas,mUnDealPath,mUnDealPointList,mUnDealPaint);

        drawValueHint(canvas);
    }

    private void drawCircleSign(Canvas canvas){

         mCircleSignPaint = new Paint();
         mCircleSignPaint.setAntiAlias(true);
         mCircleSignPaint.setStyle(Paint.Style.FILL);
         mCircleSignPaint.setColor(undealEventLineColor);

         String text = "未解决";
         Rect rect = new Rect();
         mTitleTextPaint.getTextBounds(text, 0, text.length(), rect);
         int height = DisPlayUtil.getFontSize(mTitleTextPaint);

         int margin = DisPlayUtil.dip2px(mContext,10);

         //圆的位置和倒数第二个点的x坐标相似
         float circleX1 = chartWidth - mCircleSignMargin;
         float circleY1 = mChartTitleRangeHeight/6;
         float circleX2 = chartWidth - mCircleSignMargin;
         float circleY2 = circleY1+ DisPlayUtil.dip2px(mContext,12);
         float ridus = DisPlayUtil.dip2px(mContext,4);

         canvas.drawCircle(circleX1,circleY1,ridus,mCircleSignPaint);
         canvas.drawText("未解决",circleX1+margin,circleY1+ height/3,mTitleTextPaint);

         mCircleSignPaint.setColor(dealEventLineColor);
         canvas.drawCircle(circleX2,circleY2,ridus,mCircleSignPaint);
         canvas.drawText("已解决",circleX2+ margin ,circleY2+ height/3,mTitleTextPaint);

    }

    private void drawXscales(Canvas canvas){

        if (mxscaleLableArray != null && mxscaleLableArray.length > 0
                && mHasDealEventDataList != null && mHasDealEventDataList.size() > 0){

            int length = mxscaleLableArray.length;
            Point point = null;
            for (int i = 0;i< length;i++){
                float textWidth = mXScalePaint.measureText(mxscaleLableArray[i]);
                float halfTextWidth = textWidth/2;
                point = mHasDealEventDataList.get(i);
                String weekName = TimeUtils.dateToWeek(mHasDealDataList.get(length - i -1).data);
                if (i == 0){
                    canvas.drawText(weekName,point.x,chartHeight - 10,mXScalePaint);
                }else if (i == length - 1){
                    canvas.drawText(weekName,point.x - textWidth,chartHeight - 10,mXScalePaint);
                }else{
                    canvas.drawText(weekName,point.x-halfTextWidth,chartHeight - 10,mXScalePaint);
                }
            }
        }
    }


    /**
     * 5个刻度，分成4份
     */
    private void drawYLables(Canvas canvas){
        if (yLables == null){
            return;
        }
        int parHeight = (chartHeight - mChartXscaleRangeHeigth - mXscaleMarginTop)/4;
        int x;
        int y;
        for (int i =0;i< 5;i++){
            x = mYlableWidth - DisPlayUtil.dip2px(mContext,10);
            y = chartHeight - (parHeight * i + mChartXscaleRangeHeigth);
            canvas.drawText(yLables[i],x,y,mYscaleLablePaint);
        }
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

        path.rLineTo(0,0);
        path.lineTo(adjustedPoints.get(pointSize-1).x,mChartLineRangeHeight);
        path.lineTo(adjustedPoints.get(0).x,mChartLineRangeHeight);
        path.close();

        canvas.drawPath(path,paint);
    }

    double dealMaxValue = 100;
    double unDealMaxVlaue = 100;
    List<WeekEventItem> mHasDealDataList;
    List<WeekEventItem> mUnDealDataList;

    public void initData(final List<WeekEventItem> hasDealDataList, final List<WeekEventItem> unDealDataList, final String[] mXscaleLableArray){

        new Thread(){
            @Override
            public void run() {

                if (hasDealDataList != null && unDealDataList != null && mXscaleLableArray != null){
                    mxscaleLableArray = mXscaleLableArray;

                    mUnDealDataList  = unDealDataList;
                    mHasDealDataList = hasDealDataList;

                    dealMaxValue = getMaxValue(hasDealDataList);
                    unDealMaxVlaue = getMaxValue(unDealDataList);
                    dealMaxValue = unDealMaxVlaue = Math.max(dealMaxValue,unDealMaxVlaue) * 1.2;
                    if (dealMaxValue < 0){
                        return;
                    }
                    dealMaxValue = makeYMaxValue(dealMaxValue);

                    int size = hasDealDataList.size();

                    WeekEventItem weekEventItem = null;
                    Point point = null;
                    mHasDealEventDataList = new ArrayList<>();
                    mUnDealEventDataList = new ArrayList<>();

                    double ypart = 0;
                    if (dealMaxValue != 0){
                        //ypart = (mChartLineRangeHeight - mChartTitleRangeHeight)/dealMaxValue;
                        ypart = (mChartLineRangeHeight)/dealMaxValue;
                    }

                    for (int i=0;i< size;i++){
                        weekEventItem = hasDealDataList.get(size - i -1);
                        point = new Point(i*partWidth+mYlableWidth,(int)(chartHeight-mChartXscaleRangeHeigth - ypart * weekEventItem.count));
                        mHasDealEventDataList.add(point);
                    }
                    mHasDealPointList.clear();
                    mHasDealPointList.addAll(mHasDealEventDataList);

                    for (int i=0;i< size;i++){
                        weekEventItem = unDealDataList.get(size - i -1);
                        point = new Point(i*partWidth+mYlableWidth,(int)(chartHeight-mChartXscaleRangeHeigth - ypart * weekEventItem.count));
                        mUnDealEventDataList.add(point);
                    }
                    mUnDealPointList.clear();
                    mUnDealPointList.addAll(mUnDealEventDataList);

                    computeBesselPoints(mUnDealPointList);
                    computeBesselPoints(mHasDealPointList);

                    postInvalidate();
                }

            }
        }.start();




    }

    private int getMaxValue(List<WeekEventItem> list){

        if (list== null || list.size() == 0){
            return 0;
        }

        int size = list.size();
        WeekEventItem weekEventItem = null;
        int maxValue = 0;
        for (int i =0 ;i< size;i++){
            weekEventItem = list.get(i);
            if (weekEventItem.count > maxValue){
                maxValue = weekEventItem.count;
            }
        }
        return maxValue;
    }

    //构造y轴最大值
    private double makeYMaxValue(double realMaxValue){

        double tempMaxValue = realMaxValue;
        int intMaxValue = (int)realMaxValue;
        yLables = new String[5];
        if (tempMaxValue == 0){
            for (int i = 0;i<5;i++){
                yLables[i] = i+"";
            }
            tempMaxValue = 4;
            return tempMaxValue;
        }


        while (true){
            ++tempMaxValue;

            if (tempMaxValue > 0 && tempMaxValue > realMaxValue && ((int)(tempMaxValue%4)) == 0){

                int part = (int)(tempMaxValue/4);
                for (int i = 0;i<5;i++){
                    yLables[i] = i*part+"";
                }
                return  tempMaxValue;
            }
        }
    }


    int mPosIndex = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();

        mPosIndex = 0;
        mMinPoint = null;
        double distance1=0, distance2=0, tempDistance=0;
        int lenght = mHasDealEventDataList.size();
        int tempIndex = lenght-1;
        if (lenght > 0){
            for (Point point: mHasDealEventDataList){
                distance1 = Math.sqrt(Math.sqrt(Math.abs(point.x-x))+Math.sqrt(Math.abs(point.y-y)));

                if (tempDistance <= 0){
                    mMinPoint = point;
                    tempDistance = distance1;
                    mDisplayValue = mHasDealDataList.get(tempIndex).count;
                    mPosIndex = tempIndex;
                }
                if (distance1 < tempDistance){
                    mMinPoint = point;
                    tempDistance = distance1;
                    mDisplayValue = mHasDealDataList.get(tempIndex).count;
                    mPosIndex = tempIndex;
                }
                tempIndex--;
            }

            tempIndex = lenght-1;
            for (Point point: mUnDealEventDataList){
                distance2 = Math.sqrt(Math.sqrt(Math.abs(point.x-x))+Math.sqrt(Math.abs(point.y-y)));
                if (tempDistance <= 0){
                    tempDistance = distance2;
                }
                if (distance2 < tempDistance){
                    mMinPoint = point;
                    tempDistance = distance2;
                    mDisplayValue = mUnDealDataList.get(tempIndex).count;
                    mPosIndex = tempIndex;
                }
                tempIndex--;
            }

            invalidate();
        }

        return super.onTouchEvent(event);
    }

    public void drawValueHint(Canvas canvas){
        if (mMinPoint != null){
            //canvas.drawCircle(mMinPoint.x,mMinPoint.y, 16, mHintPaint);
            //canvas.drawRect(mMinPoint.x, mMinPoint.y-22,mMinPoint.x+45,  mMinPoint.y, mHintPaint);
            if (mPosIndex == 0){
                canvas.drawText("" + mDisplayValue, mMinPoint.x-45,mMinPoint.y, mXScalePaint);
            }else if (mPosIndex == 6){
                canvas.drawText("" + mDisplayValue, mMinPoint.x,mMinPoint.y, mXScalePaint);
            }else{
                canvas.drawText("" + mDisplayValue, mMinPoint.x,mMinPoint.y, mXScalePaint);
            }

        }

    }



    /** 计算贝塞尔结点 */
    private void computeBesselPoints(List<Point> list) {
        List<Point> besselPoints = list;
        List<Point> points = new ArrayList<Point>();


        for (Point point : list) {
            points.add(point);
        }
        int count = points.size();

        besselPoints.clear();
        for (int i = 0; i < count; i++) {
            if (i == 0 || i == count - 1) {
                computeUnMonotonePoints(i, points, besselPoints);
            } else {
                Point p0 = points.get(i - 1);
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
                    computeUnMonotonePoints(i, points, besselPoints);
                } else {
                    computeMonotonePoints(i, points, besselPoints);
                }
            }
        }

    }

    /** 计算非单调情况的贝塞尔结点 */
    private void computeUnMonotonePoints(int i, List<Point> points, List<Point> besselPoints) {
        if (i == 0) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            besselPoints.add(p1);
            besselPoints.add(new Point((int)(p1.x + (p2.x - p1.x) * smoothness), p1.y));
        } else if (i == points.size() - 1) {
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            besselPoints.add(new Point((int)(p1.x - (p1.x - p0.x) * smoothness), p1.y));
            besselPoints.add(p1);
        } else {
            Point p0 = points.get(i - 1);
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            besselPoints.add(new Point((int)(p1.x - (p1.x - p0.x) * smoothness), p1.y));
            besselPoints.add(p1);
            besselPoints.add(new Point((int)(p1.x + (p2.x - p1.x) * smoothness), p1.y));
        }
    }

    /**
     * 计算单调情况的贝塞尔结点
     *
     * @param i
     * @param points
     * @param besselPoints
     */
    private void computeMonotonePoints(int i, List<Point> points, List<Point> besselPoints) {
        Point p0 = points.get(i - 1);
        Point p1 = points.get(i);
        Point p2 = points.get(i + 1);
        float k = (float)(p2.y - p0.y) / (float)(p2.x - p0.x);
        float b = p1.y - k * p1.x;
        Point p01 = new Point();
        p01.x = (int)(p1.x - (p1.x - (p0.y - b) / k) * smoothness);
        p01.y = (int)(k * p01.x + b);
        besselPoints.add(p01);
        besselPoints.add(p1);
        Point p11 = new Point();
        p11.x = (int)(p1.x + (p2.x - p1.x) * smoothness);
        p11.y = (int)(k * p11.x + b);
        besselPoints.add(p11);
    }


    /** 绘制曲线和结点 */
    private void drawCurveAndPoints(Canvas canvas, Path path, List<Point> points, Paint paint) {

        path.reset();

        List<Point> list = points;
        for (int i = 0; i < list.size(); i = i + 3) {
            if (i == 0) {
                path.moveTo(list.get(i).x, list.get(i).y);
            } else {
                path.cubicTo(list.get(i - 2).x, list.get(i - 2).y, list.get(i - 1).x, list.get(i - 1).y, list.get(i).x, list.get(i).y);
            }
        }


        int pointSize = list.size();
        if (pointSize > 0){
            path.rLineTo(0,0);
            path.lineTo(list.get(pointSize-1).x,mChartLineRangeHeight);
            path.lineTo(list.get(0).x,mChartLineRangeHeight);
            path.close();
        }

        //paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);// 绘制光滑曲线
        //paint.setStyle(Paint.Style.FILL);





//        for (Point point : series.getPoints()) {
//            if (point.willDrawing) {
//                canvas.drawCircle(point.x, point.y, 5, paint);
//                paint.setAlpha(80);
//                canvas.drawCircle(point.x, point.y, 10, paint);
//                paint.setAlpha(255);
//            }
//        }// 绘制结点
//
//
//        for (Point point : list) {
//            if (!series.getPoints().contains(point)) {
//                paint.setColor(Color.BLUE);
//                paint.setAlpha(255);
//                canvas.drawCircle(point.x, point.y, 5, paint);
//            }
//        }// 绘制贝塞尔控制结点


    }
}
