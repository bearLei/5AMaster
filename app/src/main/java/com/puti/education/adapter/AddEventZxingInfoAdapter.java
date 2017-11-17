package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.puti.education.bean.EventAboutPeople;

/**
 * Created by lenovo on 2017/11/16.
 */

public class AddEventZxingInfoAdapter extends BasicRecylerAdapter<EventAboutPeople> {

    public AddEventZxingInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
