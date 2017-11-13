package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;

/**
 * Created by yzh on 2017/4/21 0021.
 *
 *  添加事件-- 涉事人，知情人 adapter
 *
 */

public class EventInvolverPeopleAdapter extends ABaseAdapter<EventDeailInvolvedPeople>{

    public EventInvolverPeopleAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_addevent_about_people;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        EventDeailInvolvedPeople people = mList.get(position);
        ImageView headImg = holder.obtainView(convertView,R.id.grid_head_img);
        TextView nameTv = holder.obtainView(convertView,R.id.grid_head_name_tv);
        TextView involeType = holder.obtainView(convertView, R.id.grid_tv_invole_type);
        ImageView delImg = holder.obtainView(convertView,R.id.del_img);
        involeType.setVisibility(View.GONE);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,people.user.avatar,headImg);
        nameTv.setText(people.user.name);
        delImg.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(people.involvedTypeName)){
            involeType.setVisibility(View.VISIBLE);
            //setNameDisplay(involeType, people.dutyType);
            involeType.setText(people.involvedTypeName);
        }

        return convertView;
    }


}
