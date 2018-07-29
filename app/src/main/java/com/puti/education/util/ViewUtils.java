package com.puti.education.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 跟View相关的工具类
 * 
 * 字库类
 * 设置Text的颜色
 * 有关尺寸的工具类
 * 分辨率处理工具类
 * 软键盘弹出关闭
 * 自动生成一个view的id
 * 标题和lable布局调整的工具方法
 */
public class ViewUtils {

    private static final String TAG = "ViewUtils";

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getYpix(Context context, int height) {
        int dp = px2dip(context, height);
        float scale = (float) ((context.getResources().getDisplayMetrics().density) * 1.5);
        return (int) (dp * scale - 2);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getSp2pxScale(Context context) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return scale;
    }


    /**
     * 分辨率处理
     */
    public  static RelativeLayout.LayoutParams getLayoutParams(Activity context, int int1, int int2) {

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        RelativeLayout.LayoutParams chart_layout = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        chart_layout.height = dm.heightPixels * int1 / int2;

        return chart_layout;
    }

    private static Typeface mTypeface = null;


    public static void getTypeface(Context context) {
        // Font path
         String fontPath = "font/Roboto-Light.ttf";
         mTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);

    }

    public static void setNumberFontStyle(TextView textView, Context context){

        if(mTypeface == null)
        {
            getTypeface(context);
        }


        if(mTypeface != null)
        {
            textView.setTypeface(mTypeface);
        }
        //else do noting
    }

    /**
     * 红张绿跌
     */
    public static void setFinanceDataColor(TextView textView,
            double decimalNum, int roseColorResId, int downColorResId) {
        if (textView == null || roseColorResId == 0 || downColorResId == 0)
            return;
        if (decimalNum < 0) {
            textView.setTextColor(downColorResId);
        } else if (decimalNum >= 0) {
            textView.setTextColor(roseColorResId);
        }
    }






    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(final Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = activity.getCurrentFocus();
        if (focusView == null)
            return;
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);

    }

    /**
     * 用于id的自增.
     */
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * 用于在代码中动态生成一个id.<br/>
     * 通过aapt自动生成的id呢, 其id的高字节是1;<br/>
     * 为了避免id的重复呢, 所以我们代码中自动生成的id的高位不能为1;<br/>
     * 为了避免代码自动生成的id重复了, 所以我们添加一个数据成员{@link #sNextGeneratedId}来使得每次调用{@link #generateViewId()}得到的id都是不同的.<br/>
     * 这个方法是从{@link View#generateViewId()}改造而来的, 因为{@link View#generateViewId()}是api17才添加到sdk中的.
     */
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < 17) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1;
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }

    }



    private static void changeNameAndLabelLayoutParams(boolean isFull,
                                                       TextView leftTextView,
                                                       TextView rightTextView,
                                                       int spaceBetweenTextViewsInDp) {
        if (isFull) {
            //右边TextView显示的字串有被省略, 执行如下代码进行调整.

            //lable部分
            RelativeLayout.LayoutParams rightLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rightLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            rightTextView.setLayoutParams(rightLp);
            rightTextView.setEllipsize(null);

            //name部分
            RelativeLayout.LayoutParams leftLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            leftLp.addRule(RelativeLayout.LEFT_OF, rightTextView.getId());
            leftLp.rightMargin = ViewUtils.dip2px(leftTextView.getContext(), spaceBetweenTextViewsInDp);
            leftLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            leftTextView.setLayoutParams(leftLp);
            leftTextView.setEllipsize(TextUtils.TruncateAt.END);

        } else {
            //让左边TextView全部显示, 这是默认的布局.

            //name部分
            RelativeLayout.LayoutParams leftLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            leftLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            leftTextView.setLayoutParams(leftLp);
            leftTextView.setEllipsize(null);

            //label部分
            RelativeLayout.LayoutParams rightLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            rightLp.addRule(RelativeLayout.RIGHT_OF, leftTextView.getId());
            rightLp.leftMargin = ViewUtils.dip2px(leftTextView.getContext(), 10);
            rightLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            rightTextView.setLayoutParams(rightLp);
            rightTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }


    public static void expandTouchArea(View view,
                                       int left,
                                       int top,
                                       int right,
                                       int bottom) {
        View parent = (View) view.getParent();
        if (parent == null){
            return;
        }

        Rect r = new Rect();
        view.getHitRect(r);

        r.top -= ViewUtils.dip2px(view.getContext(), top);
        r.left -= ViewUtils.dip2px(view.getContext(), left);
        r.bottom += ViewUtils.dip2px(view.getContext(), bottom);
        r.right += ViewUtils.dip2px(view.getContext(), right);

        parent.setTouchDelegate(new TouchDelegate( r ,  view));
    }

    public static void expandTouchArea(View view, int border){
        expandTouchArea(view, border, border, border, border);
    }

    /**
     * 将从服务器下发的int形颜色 转换成 android需要的int颜色.
     */
    public static int parseNetColor(int netColor) {
        if ((netColor & 0xff000000) == 0) {
            return netColor | 0xff000000;
        } else {
            return netColor;
        }
    }

    /**
     * 判断用户点击的区域是否是EditText.
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public static int getScreenWid(Context context){
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    public static int getScreeenHeight(Context context){
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

}
