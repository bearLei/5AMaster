package com.puti.education;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.puti.education.bean.ClassInfo;
import com.puti.education.netFrame.RetrofitUtil;
import com.puti.education.util.ToastUtil;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


public class App extends MultiDexApplication{

    public static String mJPushRegId = "";
    public static BDLocation mMyLocation;
    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitUtil.initRetrofit();//Retrofit 初始化
        SDKInitializer.initialize(getApplicationContext());
        ToastUtil.setContext(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mJPushRegId =JPushInterface.getRegistrationID(this);

    }

    public static Context getContext(){
        return getContext();
    }
    public ArrayList<ClassInfo> mClassList;
    public synchronized void setClassList(ArrayList<ClassInfo> classlist){
        mClassList = classlist;
    }

    public ArrayList<ClassInfo> getClassList(){
        return mClassList;
    }

}
