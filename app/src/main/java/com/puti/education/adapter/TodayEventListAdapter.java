package com.puti.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.TodayEventBean;

/**
 * Created by xjbin on 2017/6/1 0001.
 */

public class TodayEventListAdapter extends ABaseAdapter<TodayEventBean>{

    public TodayEventListAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_today_event_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        TextView mEventNameTv = holder.obtainView(convertView,R.id.tv_event_name);
        TextView mEventTypeTv = holder.obtainView(convertView,R.id.tv_event_type);
        TextView mDutyTypeTv = holder.obtainView(convertView,R.id.tv_duty_type);
        TextView mEventClass = holder.obtainView(convertView, R.id.tv_event_class);

        TodayEventBean todayEventBean = mList.get(position);

        mEventNameTv.setText(todayEventBean.studentname);
        mEventTypeTv.setText(todayEventBean.eventtype);
        mDutyTypeTv.setText(todayEventBean.involvedtype);
        mEventClass.setText(todayEventBean.classname);

        return convertView;
    }
}
