package com.puti.education.ui.uiTeacher.chooseperson.mul;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.List;

/**
 * created by lei at 2017/12/30
 */

public class MulChooseAdapter extends BaseRVAdapter {

    private List<MulPersonBean> mData;
    private MulSeletePersonCallBack mSeleteCallBack;
    public MulChooseAdapter(Context context, List<MulPersonBean> mData) {
        super(context);
        this.mData = mData;
    }

    public MulChooseAdapter(Context context, List<MulPersonBean> mData, MulSeletePersonCallBack mSeleteCallBack) {
        super(context);
        this.mData = mData;
        this.mSeleteCallBack = mSeleteCallBack;
    }

    public MulChooseAdapter(Context context) {
        super(context);
    }



    @Override
    protected Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    protected int getCount() {
        if (mData == null || mData.size() == 0) return 0;
        return mData.size();
    }

    @Override
    protected BaseHolder getViewHolder(Context context, ViewGroup parent, int viewType) {

        return new MulChooseHolder(context,mSeleteCallBack);
    }
}
