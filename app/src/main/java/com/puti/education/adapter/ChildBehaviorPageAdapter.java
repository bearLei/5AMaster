package com.puti.education.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *  Created by xjbin on 2017/5/22 0022.
 *
 *  家长端 孩子关心 viewpager
 */

public class ChildBehaviorPageAdapter extends PagerAdapter {

    private List<View> mViews;

    public ChildBehaviorPageAdapter(List<View> views) {
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return this.mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(this.mViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

}
