package com.puti.education.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import unit.entity.UserInfo;
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
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        if (userInfo == null){
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            //跳转到首页
        }
    }

}
