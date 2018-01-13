package com.puti.education.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puti.education.event.EmptyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment{

    public abstract int getLayoutResourceId();
    public abstract  void initVariables();
    public abstract  void initViews(View view);
    public abstract  void loadData();

    private View rootView;

    public Context mContext;

    public ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if (rootView == null){
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
            rootView = inflater.inflate(getLayoutResourceId(),container,false);
            ButterKnife.bind(this, rootView);//视图绑定
            initVariables();
            initViews(rootView);
            loadData();
        }

        ViewGroup parent = (ViewGroup)rootView.getParent();
        if(parent != null) {
            parent.removeView(rootView);
        }


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void disLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("加载中…");
            mProgressDialog.show();
        }
    }
    public void disLoading(String msg){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideLoading(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on3EventMainThread(EmptyEvent event){

    }
}
