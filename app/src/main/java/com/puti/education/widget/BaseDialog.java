package com.puti.education.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.puti.education.R;

/**
 * Created by xjbin on 2017/5/16 0016.
 *
 */

public abstract class BaseDialog extends Dialog{

    interface SureClickListener{
        void onclick();
    }

    interface  OtherClickListener{
        void onclick();
    }

    SureClickListener mSureClickListener;
    OtherClickListener mOtherClickListener;

    public SureClickListener getmSureClickListener() {
        return mSureClickListener;
    }

    public void setmSureClickListener(SureClickListener mSureClickListener) {
        this.mSureClickListener = mSureClickListener;
    }


    public BaseDialog(Context context) {
        super(context, R.style.dialog_theme);
    }

    public abstract int getDialogLayoutId();
    public abstract void setting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getDialogLayoutId());
        setting();
    }
}
