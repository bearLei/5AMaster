package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 * 涉事人处罚信息
 */

public class PunishListAdapter extends ABaseAdapter<EventDeailInvolvedPeople>{

    public PunishListAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_punishinfo_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        EventDeailInvolvedPeople people = mList.get(position);
        ImageView img = holder.obtainView(convertView,R.id.event_punish_img);
        TextView dutyrankTv = holder.obtainView(convertView,R.id.event_punish_type);
        TextView nameTv = holder.obtainView(convertView,R.id.event_punish_name);
        TextView namePunishInfoTv = holder.obtainView(convertView,R.id.event_punish_msg_tv);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,"",img);
//        nameTv.setText(people.name);
//        dutyrankTv.setText(people.dutyRank);
//        if (people.dutyRank!= null && people.dutyRank.equals("主要责任人")){
//            dutyrankTv.setBackgroundResource(R.drawable.event_detail_red_circular_bead_bg);
//        }else{
//            dutyrankTv.setBackgroundResource(R.drawable.event_detail_yellow_circular_head_bg);
//        }
//        namePunishInfoTv.setText(TextUtils.isEmpty(people.punish) ? "暂无":people.punish);

        return convertView;
    }
}
