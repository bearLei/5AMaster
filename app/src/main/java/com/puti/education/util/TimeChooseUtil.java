package com.puti.education.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.puti.education.widget.TimeDialog;

/**
 * Created by lenovo on 2017/12/13.
 */

public class TimeChooseUtil {
    public TimeChooseUtil() {
    }

    public void showTimeDialog(Context context, TextView textView) {
        TimeDialog timeDialog = null;
        if (timeDialog == null){
            timeDialog = new TimeDialog(context,textView);
            timeDialog.setMyOnItemClickListener(new TimeDialog.OnTimeItemClickListener() {
                @Override
                public void onItemClick(String timeStr) {
                    if (!TextUtils.isEmpty(timeStr)){
                        String nowTime = TimeUtils.getCurrentTime();
                        if (TimeUtils.compareDate(timeStr, nowTime) > 0){
                            ToastUtil.show("事件时间大于当前时间");
                        }
                    }

                }

            });
        }
        timeDialog.show();
    }
}
