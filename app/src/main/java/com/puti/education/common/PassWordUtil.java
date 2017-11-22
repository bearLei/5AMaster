package com.puti.education.common;

import android.app.Activity;
import android.content.Context;

import com.puti.education.util.ConfigUtil;
import com.puti.education.util.dialog.CustomDialog;

/**
 * Created by lenovo on 2017/11/22.
 */

public class PassWordUtil {

    private static class SingleInstance{
        private static final PassWordUtil instance = new PassWordUtil();
    }

    public static PassWordUtil g(){
        return SingleInstance.instance;
    }

    public void showDialog(Activity activity , final BingPhoneListener listener){
        new CustomDialog(activity).showDoubleDialog("", "检查到您当前的账号还未绑定手机，请先绑定手机号", "去绑定", "取消", new CustomDialog.PositiveCallBack() {
            @Override
            public void callBack() {
                if (listener != null){
                    listener.bind();
                }
            }
        }, new CustomDialog.NavigationCallBack() {
            @Override
            public void callBack() {

            }
        });
    }
}
