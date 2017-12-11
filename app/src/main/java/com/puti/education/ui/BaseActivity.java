package com.puti.education.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.puti.education.R;
import com.puti.education.event.EmptyEvent;
import com.puti.education.event.UpdateUserInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class BaseActivity extends AppCompatActivity{


    public abstract int getLayoutResourceId();
    public abstract  void initVariables();
    public abstract  void initViews();
    public abstract  void loadData();

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        initStatusColor();
        initVariables();
        initViews();
        loadData();
    }

    public void initStatusColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.common_status_color));
        }
    }

    public void disLoading(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("加载中…");
        mProgressDialog.show();
    }

    public void disLoading(String msg){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void disLoading(String msg, boolean bCancel){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(bCancel);
        mProgressDialog.setCanceledOnTouchOutside(bCancel);
        mProgressDialog.show();
    }

    public void hideLoading(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void finishActivity(View v){
        this.finish();
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
