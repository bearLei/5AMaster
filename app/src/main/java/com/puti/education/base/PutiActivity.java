package com.puti.education.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.puti.education.event.EmptyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

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
    public void on3EventMainThread(EmptyEvent event){

    }
}
