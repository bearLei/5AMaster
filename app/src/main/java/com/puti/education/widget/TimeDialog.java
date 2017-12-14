package com.puti.education.widget;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.widget.timeWidget.wheeladapter.NumericWheelAdapter;
import com.puti.education.widget.timeWidget.wheelview.OnWheelScrollListener;
import com.puti.education.widget.timeWidget.wheelview.WheelView;

import java.util.Calendar;

public class TimeDialog extends Dialog {

    String chooseTime = null;

    WheelView yearWheel;
    WheelView monthWheel;
    WheelView dayWheelView;
    WheelView hourWheelView;
    WheelView minWheelView;

    TextView showTimeView;

    Context mCtx;

    private boolean hide;//是否隐藏时间和分钟
    public interface OnTimeItemClickListener {
        void onItemClick(String timeStr);
    }

    OnTimeItemClickListener myOnItemClickListener = null;

    public void setMyOnItemClickListener(OnTimeItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    public TimeDialog(Context context, int theme,boolean hideHour) {
        super(context, theme);
        this.hide = hideHour;
        mCtx = context;

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int n_year = yearWheel.getCurrentItem() + 1950;//年
                int n_month = monthWheel.getCurrentItem() + 1;//月

                int o_monday = dayWheelView.getCurrentItem() + 1;

                int tempday = getDay(n_year, n_month);
                NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mCtx, 1, tempday, "%02d");
                numericWheelAdapter.setLabel("日");
                dayWheelView.setViewAdapter(numericWheelAdapter);

                if (o_monday <= tempday){
                    dayWheelView.setCurrentItem(o_monday-1);
                }else{
                    dayWheelView.setCurrentItem(0);
                }

                int n_monday = dayWheelView.getCurrentItem() + 1;

                int hour = hourWheelView.getCurrentItem();
                int min = minWheelView.getCurrentItem();

                chooseTime = polishingTimeStr(n_year, n_month, n_monday, hour, min);
            }
        };

        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        chooseTime = polishingTimeStr(curYear, curMonth, curDate, hour, min);

        View view = getLayoutInflater().inflate(R.layout.wheel_time_picker, null);

        TextView cancelView = (TextView) view.findViewById(R.id.cancel);
        TextView sureView = (TextView) view.findViewById(R.id.sure);

        yearWheel = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(context, 1950, curYear);
        numericWheelAdapter1.setLabel("年");
        yearWheel.setViewAdapter(numericWheelAdapter1);
        yearWheel.setCyclic(true);
        yearWheel.addScrollingListener(scrollListener);
        yearWheel.setVisibleItems(7);//设置显示行数

        monthWheel = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(context, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        monthWheel.setViewAdapter(numericWheelAdapter2);
        monthWheel.setCyclic(true);
        monthWheel.addScrollingListener(scrollListener);
        monthWheel.setVisibleItems(7);

        dayWheelView = (WheelView) view.findViewById(R.id.day);
        dayWheelView.setCyclic(true);
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel("日");
        dayWheelView.setViewAdapter(numericWheelAdapter);
        dayWheelView.addScrollingListener(scrollListener);
        dayWheelView.setVisibleItems(7);

        hourWheelView = (WheelView) view.findViewById(R.id.min);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, 0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        hourWheelView.setViewAdapter(numericWheelAdapter3);
        hourWheelView.setCyclic(true);
        hourWheelView.addScrollingListener(scrollListener);
        hourWheelView.setVisibleItems(7);

        minWheelView = (WheelView) view.findViewById(R.id.sec);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(context, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        minWheelView.setViewAdapter(numericWheelAdapter4);
        minWheelView.setCyclic(true);
        minWheelView.addScrollingListener(scrollListener);
        minWheelView.setVisibleItems(7);

        yearWheel.setCurrentItem(curYear - 1950);
        monthWheel.setCurrentItem(curMonth - 1);
        dayWheelView.setCurrentItem(curDate - 1);
        hourWheelView.setCurrentItem(hour - 1);
        minWheelView.setCurrentItem(min - 1);

        cancelView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sureView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimeView.setText(chooseTime);
                dismiss();
                if (myOnItemClickListener != null) {
                    myOnItemClickListener.onItemClick(chooseTime);
                }
            }
        });

        super.setContentView(view);
    }


    public TimeDialog(Context context, TextView showTimeView) {
        this(context, R.style.timechoosedialog,false);
        this.showTimeView = showTimeView;
    }
    public TimeDialog(Context context, TextView showTimeView,boolean hideHour) {
        this(context, R.style.timechoosedialog,hideHour);
        this.showTimeView = showTimeView;
        hideHourAndSecondView(hideHour);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.BOTTOM);
        //设置对话框的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        p.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    private String polishingTimeStr(int n_year, int n_month, int monday, int hour, int min) {

        String monthStr = null;
        String mondayStr = null;
        String hourStr = null;
        String minStr = null;

        if (n_month < 10) {
            monthStr = "0" + n_month;
        }else{
            monthStr = ""+n_month;
        }

        if (monday < 10) {
            mondayStr = "0" + monday;
        }else{
            mondayStr = ""+monday;
        }

        if (hour < 10) {
            hourStr = "0" + hour;
        }else{
            hourStr = ""+hour;
        }

        if (min < 10) {
            minStr = "0" + min;
        }else{
            minStr = ""+ min;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n_year);
        stringBuilder.append("-");
        stringBuilder.append(monthStr);
        stringBuilder.append("-");
        stringBuilder.append(mondayStr);
        if (!hide){
            stringBuilder.append(" ");
            stringBuilder.append(hourStr);
            stringBuilder.append(":");
            stringBuilder.append(minStr);
            stringBuilder.append(":00");
        }
        return stringBuilder.toString();
    }


    public void hideHourAndSecondView(boolean hideHour) {
        this.hide = hideHour;
        if (hideHour) {
            hourWheelView.setVisibility(View.GONE);
            minWheelView.setVisibility(View.GONE);
        }else {
            hourWheelView.setVisibility(View.VISIBLE);
            minWheelView.setVisibility(View.VISIBLE);
        }
    }
}
