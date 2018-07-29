package com.puti.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.puti.education.bean.ChildRelativeDes;
import com.puti.education.util.DisPlayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjbin on 2017/5/22 0022.
 *
 * 亲子关系图
 */

public class RelationDesBarView extends View{

    private Context mContext;

    private int mCharViewWidth;
    private int mCharViewHeight;
    private int mLeftRightSpace;// y轴刻度的宽度
    private int mXTitleSpace; //x轴坐标的高度
    private int topSpace;//
    private int mHalfBarWidth;
    private float viewAverageWidth;

    private Paint mLinePait;
    private Paint mBarPaint;
    private Paint mTextPaint;
    private Paint mYTextPaint;

    private List<Point> mPointList = null;

    private final static int[] BAR_COLORS = new int[]{0XFF00C2FF,0XFFFFCA00,0XFF4FBC00,0XFFF32A2A,0XFF00C2FF,0XFF3FBC00};
    private final static int[] Y_TEXT_COLORS = new int[]{0XFFF44D4D,0XFFFED129,0XFF29CBFE,0XFF6BC629};
    private final static String[] Y_TITLES = new String[]{"差","中","良","优"};
    private final static String[] mXTitles = new String[]{"过于关注学习","社交干预","过分关爱","感情疏远","干预孩子行为","沟通"};

    public RelationDesBarView(Context context) {
        super(context,null,0);
    }

    public RelationDesBarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RelationDesBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init(){
        mLeftRightSpace = DisPlayUtil.dip2px(mContext,50);
        mXTitleSpace = DisPlayUtil.dip2px(mContext,25);
        topSpace = DisPlayUtil.dip2px(mContext,5);
        mPointList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCharViewHeight = getMeasuredHeight() - topSpace;
        mCharViewWidth = getMeasuredWidth();
        mHalfBarWidth = mCharViewWidth/7*2/4/2;
        viewAverageWidth = mCharViewWidth / 7;
        Log.i("===viewAverageWidth",""+viewAverageWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect(0,0,mCharViewWidth,mCharViewHeight);
        canvas.clipRect(rect);
        canvas.drawColor(Color.WHITE);

        initPaint();
        drawLineAndYTitles(canvas);
        drawXTilteAndXscaleMark(canvas);
        drawBar(canvas);
    }

    private void initPaint(){
        mLinePait = new Paint();
        mLinePait.setColor(Color.parseColor("#EEEEEE"));
        mLinePait.setStyle(Paint.Style.STROKE);
        mLinePait.setStrokeWidth(DisPlayUtil.dip2px(mContext,1));
        mLinePait.setAntiAlias(true);

        mBarPaint = new Paint();
        mBarPaint.setColor(Color.parseColor("#EEEEEE"));
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#212121"));
        mTextPaint.setTextSize(DisPlayUtil.sp2px(mContext,7));
        mTextPaint.setAntiAlias(true);

        mYTextPaint = new Paint();
        mYTextPaint.setColor(Color.parseColor("#212121"));
        mYTextPaint.setTextSize(DisPlayUtil.sp2px(mContext,10));
        mYTextPaint.setAntiAlias(true);
    }

    public void setInitData(List<ChildRelativeDes> childRelativeList){

        if (childRelativeList == null || childRelativeList.size() == 0){
            return;
        }

        int max = 100;
        float x,y;
        int part = (mCharViewHeight - mXTitleSpace)/max;

        ChildRelativeDes childRelativeDes = null;
        Point point = null;
        mPointList.clear();
        for (int i= 0;i< childRelativeList.size();i++){
            childRelativeDes = childRelativeList.get(i);
            x = i*viewAverageWidth + viewAverageWidth - mHalfBarWidth;
            y = mCharViewHeight - mXTitleSpace - childRelativeDes.value * part;
            Log.i("=xy",x+" "+y);
            point = new Point((int)x,(int)y);
            mPointList.add(point);
        }
        invalidate();
    }

    private void drawLineAndYTitles(Canvas canvas){

        int y = 0;
        int partHeight = (mCharViewHeight - mXTitleSpace)/4;

        for (int i=0; i < 5;i++){
            y = mCharViewHeight - mXTitleSpace - partHeight*i;
            canvas.drawLine(0,y,mCharViewWidth,y,mLinePait);
        }

        for (int i =0;i<4;i++){
            y = mCharViewHeight - mXTitleSpace - partHeight*i - partHeight*2/3;
            mYTextPaint.setColor(Y_TEXT_COLORS[i]);
            canvas.drawText(Y_TITLES[i],0,y,mYTextPaint);
        }
    }

    private void drawBar(Canvas canvas){

        if (mPointList == null || mPointList.size() == 0){
            return;
        }

        Point point = null;
        Rect rect= null;
        int tempIndex = 0;
        for (int i=0;i < mPointList.size();i++){
            tempIndex = i;
            if (i > 5){
                tempIndex = i % 6;
            }
            mBarPaint.setColor(BAR_COLORS[tempIndex]);
            point = mPointList.get(i);
            Log.i("==mHalfBarWidth",""+mHalfBarWidth);
            Log.i("==px,py",""+point.x+"  "+point.y);
            rect = new Rect(point.x,point.y,point.x + mHalfBarWidth*2,mCharViewHeight - mXTitleSpace);
            canvas.drawRect(rect,mBarPaint);
        }
    }

    //X坐标
    private void drawXTilteAndXscaleMark(Canvas canvas){

        if (mXTitles == null || mXTitles.length == 0){
            return;
        }

        float x1,y1,x2,y2,tx,ty;

        float part = mCharViewWidth/7;
        for (int i =0;i < 6;i++){
            x1 = i*part + part;
            y1 = mCharViewHeight - mXTitleSpace;
            x2 = x1;
            y2 = y1 + DisPlayUtil.dip2px(mContext,5);
            canvas.drawLine(x1,y1,x2,y2,mLinePait);

            int width = DisPlayUtil.getTextWidth(mTextPaint,mXTitles[i]);
            Log.i("text width",width+"");
            tx = x1 - width/2;
            ty = y2  + DisPlayUtil.dip2px(mContext,10);
            canvas.drawText(mXTitles[i],tx,ty,mTextPaint);
        }

    }
}
