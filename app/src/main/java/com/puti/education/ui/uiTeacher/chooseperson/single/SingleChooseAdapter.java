package com.puti.education.ui.uiTeacher.chooseperson.single;

import android.content.Context;
import android.view.ViewGroup;

import com.puti.education.base.BaseRVAdapter;
import com.puti.education.base.holder.BaseHolder;

import java.util.List;

/**
 * created by lei at 2017/12/30
 */

public class SingleChooseAdapter extends BaseRVAdapter {

    private List<SingleChooseBean> mData;
    private SingleSeletePersonCallBack mSeleteCallBack;
    public SingleChooseAdapter(Context context, List<SingleChooseBean> mData) {
        super(context);
        this.mData = mData;
    }

    public SingleChooseAdapter(Context context, List<SingleChooseBean> mData, SingleSeletePersonCallBack mSeleteCallBack) {
        super(context);
        this.mData = mData;
        this.mSeleteCallBack = mSeleteCallBack;
    }

    public SingleChooseAdapter(Context context) {
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
        return new SingleChooseHolder(context,mSeleteCallBack);
    }
}
