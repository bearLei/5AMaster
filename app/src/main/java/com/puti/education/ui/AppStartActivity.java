package com.puti.education.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.puti.education.util.LogUtil;

import unit.entity.UserBaseInfo;
import unit.entity.UserInfo;
import unit.home.HomeActivity;
import unit.login.LoginActivity;
import unit.util.UserInfoUtils;


/**
 *  App 启动页
 */
public class AppStartActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoginUI();

    }

    public void startLoginUI(){
        UserBaseInfo userInfo = UserInfoUtils.getUserInfo();
        if (userInfo != null) {
            LogUtil.d("lei", userInfo.toString());
        }
        Intent intent = new Intent();
        if (userInfo == null){
            intent.setClass(this, LoginActivity.class);
        }else {
            //跳转到首页
            intent.setClass(this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
