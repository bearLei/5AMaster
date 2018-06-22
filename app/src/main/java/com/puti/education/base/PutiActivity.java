package com.puti.education.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.puti.education.widget.EduDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import unit.eventbus.TokenErrorEvent;
import unit.moudle.login.LoginActivity;
import unit.util.UserInfoUtils;

/**
 * Created by ${lei} on 2018/6/2.
 * 基类Activity
 */
public abstract class PutiActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        BindPtr();
        ParseIntent();
        AttachPtrView();
        Star();
    }

    public abstract int getContentView();
    public abstract void BindPtr();
    public abstract void ParseIntent();
    public abstract void AttachPtrView();
    public abstract void DettachPtrView();
    public abstract void InitView();
    public abstract void Star();
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        InitView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(TokenErrorEvent event) {
        UserInfoUtils.setUserInfo(null);
        if (getTopActivity().equals(getClass().getName())) {
            final EduDialog dialog = new EduDialog(this, "您的账号在别的设备登录，请重新登录");
            dialog.setCancelable(false);
            dialog.setOnPositiveButtonClickListener(new EduDialog.OnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(View view) {
                    startActivity(new Intent(PutiActivity.this, LoginActivity.class));
                    finish();
                    dialog.dismiss();
                }
            }, "确定");
            dialog.show();
        }
    }


    private String getTopActivity(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        if (tasks != null && tasks.size() > 0){
            ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
            ComponentName componentName = taskInfo.topActivity;
            return componentName.getClassName();
        }else {
            return "";
        }
    }
}
