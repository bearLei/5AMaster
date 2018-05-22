package com.puti.education.base;

import android.view.View;

import com.puti.education.ui.BaseFragment;

/**
 * Created by lenovo on 2018/1/12.
 */

public abstract class BaseMvpFragment  extends BaseFragment{

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void loadData() {

    }

    public abstract void initPresenter();
}
