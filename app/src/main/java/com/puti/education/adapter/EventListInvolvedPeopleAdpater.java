package com.puti.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventBean;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/6/1 0001.
 */

public class EventListInvolvedPeopleAdpater extends ABaseAdapter<EventBean.InvolvedPeople>{

    public EventListInvolvedPeopleAdpater(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_eventlist_involved_head_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        TextView nameTv = holder.obtainView(convertView,R.id.grid_head_name_tv);
        ImageView imgView = holder.obtainView(convertView,R.id.grid_head_img);

        EventBean.InvolvedPeople involvedPeople = mList.get(position);
        nameTv.setText(involvedPeople.name);
        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,involvedPeople.avatar,imgView);

        return convertView;
    }
}
