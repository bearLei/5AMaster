package com.puti.education.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.puti.education.bean.TeacherExperience;
import com.puti.education.bean.TeacherPower;
import com.puti.education.util.DisPlayUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *  条形统计图
 */
public class MultiBarChartView extends View{

    private Paint mLinePaint;
    private Paint mXScaleLablePaint;
    private Paint mYscaleLablePaint;
    private Paint mYellowBarPaint;
    private Paint mBlueBarPaint;
    private Paint mTitleTextPaint;
    private Paint mCircleSignPaint;

    private Point mValuePoint;
    private int mBarWidth;

    private int lineColor = Color.parseColor("#f1f1f1");
    private int scaleLableColor = Color.parseColor("#999999");
    private int yellowBarColor = Color.parseColor("#FFD93C");
    private int blueBarColor = Color.parseColor("#67CDDC");
    private int lineWidth = 5;

    private int scaleLableSize;
    private final int mBarMargin = 15;//黄蓝色bar水平间隔

    private int mYlableWidth ;//Y轴水平宽度
    private int mXlableHeight;// X轴水平高度

    private int xCoordiLineHieght;//x轴刻度线的高度

    private int mChartTitleRangeHeight ;//标题栏高度
    private int mCircleSignMargin;

    private int mCharViewWidth;
    private int mCharViewHeight;

    private double maxValue = 100;// 先假设400
    private int mDediveOffsset;//主要是为了正常显示y刻度而设置

    private List<Point> mTeacherPowerPointList = new ArrayList<>(4);
    private List<Point> mTeacherExpirencePointList  = new ArrayList<>(4);
    private List<String> monthList = new ArrayList<>(4);

    private List<TeacherExperience> mTeacherExperienceList = new ArrayList<>(4);
    private List<TeacherPower> mTeacherPowerList = new ArrayList<>(4);
    private String[] lables ;

    private Context mContext;

    public MultiBarChartView(Context context) {
        this(context,null,0);
    }

    public MultiBarChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MultiBarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        mYlableWidth = DisPlayUtil.dip2px(context,40);
        scaleLableSize = DisPlayUtil.sp2px(context,10);
        xCoordiLineHieght = DisPlayUtil.dip2px(context,7);
        lineWidth = DisPlayUtil.dip2px(context,1);
        mXlableHeight = DisPlayUtil.dip2px(context,30);
        mDediveOffsset = DisPlayUtil.dip2px(context,10);

        mChartTitleRangeHeight = DisPlayUtil.dip2px(mContext,30);
        mCircleSignMargin = DisPlayUtil.dip2px(mContext,70);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCharViewHeight = getMeasuredHeight();
        mCharViewWidth = getMeasuredWidth();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        drawView(canvas);
    }

    private void initPaint(){
        mLinePaint = new Paint();
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth);
        mLinePaint.setAntiAlias(true);

        mXScaleLablePaint = new Paint();
        mXScaleLablePaint.setColor(scaleLableColor);
        mXScaleLablePaint.setTextSize(scaleLableSize);
        mXScaleLablePaint.setAntiAlias(true);

        mYscaleLablePaint = new Paint();
        mYscaleLablePaint.setColor(scaleLableColor);
        mYscaleLablePaint.setTextSize(scaleLableSize);
        mYscaleLablePaint.setTextAlign(Paint.Align.RIGHT);
        mYscaleLablePaint.setAntiAlias(true);

        mYellowBarPaint = new Paint();
        mYellowBarPaint.setStyle(Paint.Style.FILL);
        mYellowBarPaint.setColor(yellowBarColor);
        mYellowBarPaint.setAntiAlias(true);

        mTitleTextPaint = new Paint();
        mTitleTextPaint.setColor(scaleLableColor);
        mTitleTextPaint.setTextSize(scaleLableSize);
        mTitleTextPaint.setAntiAlias(true);

        mBlueBarPaint = new Paint();
        mBlueBarPaint.setStyle(Paint.Style.FILL);
        mBlueBarPaint.setColor(blueBarColor);
        mBlueBarPaint.setAntiAlias(true);
    }

    private void drawView(Canvas canvas){

        Rect rect = new Rect(0,0,mCharViewWidth,mCharViewHeight);
        canvas.clipRect(rect);
        canvas.drawColor(Color.WHITE);

        drawCircleSign(canvas);
        drawXLables(canvas);
        drawYLables(canvas);
        drawBar(canvas);
        drawValueHint(canvas);
    }


    public void initData(final List<TeacherExperience> teacherExperienceList, final List<TeacherPower> teacherPowerList){

        new Thread(){
            @Override
            public void run() {
                monthList.clear();
                for (int i = 0;i<teacherExperienceList.size();i++){
                    monthList.add(teacherExperienceList.get(i).data);
                }
                mTeacherExperienceList = teacherExperienceList;
                mTeacherPowerList = teacherPowerList;

                maxValue = Math.max(getExperienceMaxValue(mTeacherExperienceList),getTeacherPowerMaxValue(mTeacherPowerList));
                if (maxValue < 0){
                    return;
                }
                maxValue = makeYMaxValue(maxValue);
                int part = (mCharViewWidth - mYlableWidth) /4;
                int barWidth = (part - mBarMargin)/4 ;//每个bar的宽度
                double  partValue = 0;
                if (maxValue != 0){
                      partValue = (mCharViewHeight - mXlableHeight - mDediveOffsset-mChartTitleRangeHeight) / maxValue ;
                }

                int x,y;

                TeacherExperience teacherExperience = null;
                TeacherPower teacherPower = null;
                Point point = null;

                //教师经验
                int len = teacherExperienceList.size();
                if (teacherExperienceList != null && len >= 4){
                    mTeacherExpirencePointList.clear();
                    for (int i =0;i< 4;i++){
                        teacherExperience = teacherExperienceList.get(len-4+i);
                        x = mYlableWidth + part * i + barWidth;
                        y = mCharViewHeight - (mXlableHeight +(int)(teacherExperience.value * partValue));
                        point = new Point(x,y);
                        mTeacherExpirencePointList.add(point);
                    }
                }

                //教师能力
                len = teacherPowerList.size();
                if (teacherPowerList != null && len > 4){
                    mTeacherPowerPointList.clear();
                    for (int i =0;i< 4;i++){
                        teacherPower = teacherPowerList.get(len-4+i);
                        x = mYlableWidth + part * i + (barWidth*2 + mBarMargin);
                        y = mCharViewHeight - (mXlableHeight+(int)(teacherPower.value * partValue));
                        point = new Point(x,y);
                        mTeacherPowerPointList.add(point);
                    }
                }

                postInvalidate();

            }
        }.start();


    }

    private void drawCircleSign(Canvas canvas){

        mCircleSignPaint = new Paint();
        mCircleSignPaint.setAntiAlias(true);
        mCircleSignPaint.setStyle(Paint.Style.FILL);
        mCircleSignPaint.setColor(yellowBarColor);

        String text = "经验分";
        Rect rect = new Rect();
        mTitleTextPaint.getTextBounds(text, 0, text.length(), rect);
        int height = DisPlayUtil.getFontSize(mTitleTextPaint);

        int margin = DisPlayUtil.dip2px(mContext,10);

        //圆的位置和倒数第二个点的x坐标相似
        float circleX1 = mCharViewWidth - (mCircleSignMargin*2);
        float circleY1 = mChartTitleRangeHeight/3;
        float circleX2 = mCharViewWidth - mCircleSignMargin;
        float circleY2 = circleY1;
        float ridus = DisPlayUtil.dip2px(mContext,4);

        canvas.drawCircle(circleX1,circleY1,ridus,mCircleSignPaint);
        canvas.drawText("经验分",circleX1+margin,circleY1+ height/3,mTitleTextPaint);

        mCircleSignPaint.setColor(blueBarColor);
        canvas.drawCircle(circleX2,circleY2,ridus,mCircleSignPaint);
        canvas.drawText("能力分",circleX2+ margin ,circleY2+ height/3,mTitleTextPaint);

    }
    /**
     * 5个刻度，分成4份
     */
    private void drawYLables(Canvas canvas){
        if (lables == null){
            return;
        }
        int parHeight = (mCharViewHeight - mXlableHeight - mDediveOffsset - mChartTitleRangeHeight)/4;
        int x;
        int y;
        for (int i =0;i< 5;i++){
            x = mYlableWidth - DisPlayUtil.dip2px(mContext,10);
            y = mCharViewHeight - (parHeight * i + mXlableHeight);
            canvas.drawText(lables[i],x,y,mYscaleLablePaint);
        }
    }

    /**
     * x坐标线 + 刻度线
     */
    private void drawXLables(Canvas canvas){
        if (monthList == null || monthList.size() == 0)
            return;

        canvas.drawLine(mYlableWidth,mCharViewHeight - mXlableHeight,mCharViewWidth,mCharViewHeight - mXlableHeight,mLinePaint);
        int xPartValue = (mCharViewWidth - mYlableWidth)/4;
        int x1,y1,x2,y2,x,y;

        Rect rect = new Rect();
        mYscaleLablePaint.getTextBounds(monthList.get(0), 0, monthList.get(0).length(), rect);
        int offset = rect.width()/2;

        int len = monthList.size();
        if (len >= 4) {
            for (int i = 0; i < 4; i++) {
                x = mYlableWidth + xPartValue / 2 + i * xPartValue - offset;
                y = xCoordiLineHieght + (mCharViewHeight - mXlableHeight) + DisPlayUtil.dip2px(mContext, 10);
                canvas.drawText(monthList.get(len - 4 + i), x, y, mXScaleLablePaint);
            }
        }
        for (int i = 0 ; i < 5;i++){
            x1 = mYlableWidth + i*xPartValue;
            y1 = mCharViewHeight - mXlableHeight - lineWidth;
            x2 = x1;
            y2 = xCoordiLineHieght + (mCharViewHeight - mXlableHeight);//设置x轴刻度数显高度为10px
            canvas.drawLine(x1,y1,x2,y2,mLinePaint);
        }
    }

    /**
     * 根据效果图初步计算bar的宽度为每个刻度的 四分之一
     */
    private void drawBar(Canvas canvas){
         if (mTeacherExpirencePointList != null && mTeacherExpirencePointList.size() >0 ){

             mBarWidth = ((mCharViewWidth - mYlableWidth)/4 - mBarMargin)/4 ;//每个bar的宽度

             for (int i =0;i<4;i++){
                 //黄色bar
                 Point yPoint = mTeacherExpirencePointList.get(i);
                 canvas.drawRect(yPoint.x,yPoint.y,yPoint.x + mBarWidth,mCharViewHeight - mXlableHeight,mYellowBarPaint);
             }
         }

        if (mTeacherPowerPointList != null && mTeacherPowerPointList.size() > 0 ){

            mBarWidth = ((mCharViewWidth - mYlableWidth)/4 - mBarMargin)/4 ;//每个bar的宽度

            for (int i =0;i<4;i++){
                //蓝色bar
                Point yPoint2 = mTeacherPowerPointList.get(i);
                canvas.drawRect(yPoint2.x,yPoint2.y,yPoint2.x + mBarWidth,mCharViewHeight - mXlableHeight,mBlueBarPaint);
            }
        }
    }

    private int getExperienceMaxValue(List<TeacherExperience> list){

        if (list== null || list.size() == 0){
            return 0;
        }

        int size = list.size();
        TeacherExperience teacherExperience = null;
        int maxValue = 0;
        for (int i =0 ;i< 4 && (size >= 4);i++){
            teacherExperience = list.get(size - 4 + i);
            if (teacherExperience.value > maxValue){
                maxValue = teacherExperience.value;
            }
        }
        return maxValue;
    }

    private float getTeacherPowerMaxValue(List<TeacherPower> list){

        if (list== null || list.size() == 0){
            return 0;
        }

        int size = list.size();
        TeacherPower teacherPower = null;
        float maxValue = 0;
        for (int i =0 ;i< 4  && (size >= 4);i++){
            teacherPower = list.get(size - 4 + i);
            if (teacherPower.value > maxValue){
                maxValue = teacherPower.value;
            }
        }
        return maxValue;
    }

    //构造y轴最大值
    private double makeYMaxValue(double realMaxValue){

        double tempMaxValue = realMaxValue;
        lables = new String[5];
        if (tempMaxValue == 0){
            for (int i = 0;i<5;i++){
                lables[i] = i+"";
            }
            tempMaxValue = 4;
            return tempMaxValue;
        }

        while (true){
            ++tempMaxValue;
            if (tempMaxValue > 0 && tempMaxValue > realMaxValue && tempMaxValue%4 == 0){

                int part = (int)(tempMaxValue/4);
                for (int i = 0;i<5;i++){
                    lables[i] = i*part+"";
                }
                return  tempMaxValue;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();

        int len = mTeacherExpirencePointList.size();
        int index = 0, datalen = 0;
        if (len > 0){
            for (Point point: mTeacherExpirencePointList){
                if (x >= point.x && x<= (point.x + mBarWidth)){
                    mValuePoint = point;
                    datalen =  mTeacherExperienceList.size();
                    mDisplayValue = mTeacherExperienceList.get(datalen-4+index).value;
                    break;
                }
                index++;
            }
        }

        index = 0;
        len = mTeacherPowerPointList.size();
        if (len > 0){
            for (Point point: mTeacherPowerPointList){
                if (x >= point.x && x<= (point.x + mBarWidth)){
                    mValuePoint = point;
                    datalen =  mTeacherPowerList.size();
                    mDisplayValue = mTeacherPowerList.get(datalen-4+index).value;
                    break;
                }
                index++;
            }
        }

        invalidate();
        return super.onTouchEvent(event);
    }

    private double mDisplayValue = 0;
    public void drawValueHint(Canvas canvas){
        if (mValuePoint != null){
            //canvas.drawCircle(mMinPoint.x,mMinPoint.y, 16, mHintPaint);
            //canvas.drawRect(mMinPoint.x, mMinPoint.y-22,mMinPoint.x+45,  mMinPoint.y, mHintPaint);
            canvas.drawText("" + mDisplayValue, mValuePoint.x,mValuePoint.y, mTitleTextPaint);
        }

    }
}
