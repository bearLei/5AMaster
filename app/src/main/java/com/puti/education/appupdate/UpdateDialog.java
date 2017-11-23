package com.puti.education.appupdate;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.puti.education.R;
import com.puti.education.util.ViewUtils;

/**
 * Created by lenovo on 2017/11/20.
 */

public class UpdateDialog extends DialogFragment {

    private Context context;


    private String update_url;

    private ViewGroup rootView;
    private FrameLayout mContainer;

    @SuppressLint({"NewApi", "ValidFragment"})
    public UpdateDialog(Context context) {
        this.context = context;
    }

    public UpdateDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.layout_dialog,null);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mContainer = (FrameLayout) rootView.findViewById(R.id.container);
        initView(mContainer);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null){
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width =WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
    }
    private void initView(ViewGroup container){
        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.popdialog_appupdate, container);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(ViewUtils.dip2px(context,25),0,ViewUtils.dip2px(context,25),0);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        LinearLayout update = (LinearLayout) rootView.findViewById(R.id.pop_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApkDownloadTask(context).execute(update_url);
                dismiss();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }
}
