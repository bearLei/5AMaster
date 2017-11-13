package com.puti.education.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.widget.timeWidget.wheeladapter.NumericWheelAdapter;
import com.puti.education.widget.timeWidget.wheelview.OnWheelScrollListener;
import com.puti.education.widget.timeWidget.wheelview.WheelView;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class TimePopupWindow extends PopupWindow {

    private Context mContext;
    private String chooseTime = null;

    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheelView;
    private WheelView hourWheelView;
    private WheelView minWheelView;
    private int mType = 2;  // 2：年月日时分； 3：年月日； 4：年月

    public interface OnTimeItemClickListener{
        void onItemClick(String timeStr);
    }

    OnTimeItemClickListener myOnItemClickListener = null;
    public void setMyOnItemClickListener(OnTimeItemClickListener myOnItemClickListener){
        this.myOnItemClickListener = myOnItemClickListener;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener()
    {
        @Override
        public void onScrollingStarted(WheelView wheel)
        {
        }

        @Override
        public void onScrollingFinished(WheelView wheel)
        {
            int n_year = yearWheel.getCurrentItem() + 1950;//年
            int n_month = monthWheel.getCurrentItem() + 1;//月
            int monday = dayWheelView.getCurrentItem() + 1;
            int hour = hourWheelView.getCurrentItem() + 1;
            int min = minWheelView.getCurrentItem() + 1;

            if (mType == 3){
                chooseTime = n_year + "-" + n_month + "-" + monday;
            }else if (mType == 4){
                chooseTime = n_year + "-" + n_month;
            }else{
                chooseTime = n_year + "-" + n_month + "-" + monday + " " + hour + ":" + min +":00";
            }

        }
    };

    public TimePopupWindow(Context context, int type)
    {
        super(context);
        mType = type;
        mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.wheel_time_picker_top,null);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        initTimeWheel(rootView);
    }

    public void initTimeWheel(View rootView){
        Calendar c = Calendar.getInstance();

        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        if (mType == 3){
            chooseTime = curYear + "-" + curMonth + "-" + curDate;
        }else if (mType == 4){
            chooseTime = curYear + "-" + curMonth;
        }else{
            chooseTime = curYear + "-" + curMonth + "-" + curDate + " " + hour + ":" + min +":00";
        }

        TextView cancelView = (TextView) rootView.findViewById(R.id.cancel);
        TextView sureView = (TextView) rootView.findViewById(R.id.sure);

        yearWheel = (WheelView) rootView.findViewById(R.id.year);
        monthWheel = (WheelView) rootView.findViewById(R.id.month);
        dayWheelView = (WheelView) rootView.findViewById(R.id.day);
        hourWheelView = (WheelView) rootView.findViewById(R.id.hour);
        minWheelView = (WheelView) rootView.findViewById(R.id.min);

        if (mType == 3){
            hourWheelView.setVisibility(View.GONE);
            minWheelView.setVisibility(View.GONE);
        }else if (mType == 4){
            dayWheelView.setVisibility(View.GONE);
            hourWheelView.setVisibility(View.GONE);
            minWheelView.setVisibility(View.GONE);
        }

        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(mContext, 1950, curYear);
        numericWheelAdapter1.setLabel("年");
        yearWheel.setViewAdapter(numericWheelAdapter1);
        yearWheel.setCyclic(true);
        yearWheel.addScrollingListener(scrollListener);
        yearWheel.setVisibleItems(7);//设置显示行数


        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(mContext, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        monthWheel.setViewAdapter(numericWheelAdapter2);
        monthWheel.setCyclic(true);
        monthWheel.addScrollingListener(scrollListener);
        monthWheel.setVisibleItems(7);


            dayWheelView.setCyclic(true);
            NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext, 1, getDay(curYear, curMonth), "%02d");
            numericWheelAdapter.setLabel("日");
            dayWheelView.setViewAdapter(numericWheelAdapter);
            dayWheelView.addScrollingListener(scrollListener);
            dayWheelView.setVisibleItems(7);



            NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(mContext, 1, 23, "%02d");
            numericWheelAdapter3.setLabel("时");
            hourWheelView.setViewAdapter(numericWheelAdapter3);
            hourWheelView.setCyclic(true);
            hourWheelView.addScrollingListener(scrollListener);
            hourWheelView.setVisibleItems(7);


            NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(mContext, 1, 59, "%02d");
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
                dismiss();
                myOnItemClickListener.onItemClick(chooseTime);
            }
        });
    }


    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month)
    {
        int day = 30;
        boolean flag = false;
        switch (year % 4)
        {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month)
        {
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

}
