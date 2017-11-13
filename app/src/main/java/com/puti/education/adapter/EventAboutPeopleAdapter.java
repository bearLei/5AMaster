package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;

/**
 * Created by xjbin on 2017/4/21 0021.
 *
 *  添加事件-- 涉事人，知情人 adapter
 *
 */

public class EventAboutPeopleAdapter extends ABaseAdapter<EventAboutPeople>{

    private boolean mIsChanged = true;
    public EventAboutPeopleAdapter(Context context) {
        super(context);
    }

    public void setDataChanged(boolean value){
        mIsChanged = value;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_addevent_about_people;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        EventAboutPeople people = mList.get(position);
        ImageView headImg = holder.obtainView(convertView,R.id.grid_head_img);
        TextView nameTv = holder.obtainView(convertView,R.id.grid_head_name_tv);
        TextView involeType = holder.obtainView(convertView, R.id.grid_tv_invole_type);
        ImageView delImg = holder.obtainView(convertView,R.id.del_img);
        involeType.setVisibility(View.GONE);

        if (people.isPeople){
            ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle,people.avatar,headImg);
            nameTv.setText(people.name);
            if (mIsChanged){
                delImg.setVisibility(View.VISIBLE);
            }else{
                delImg.setVisibility(View.GONE);
            }
        }else{
            ImgLoadUtil.displayCirclePic(R.mipmap.add_people_btn_bg,people.avatar,headImg);
            nameTv.setText("");
            delImg.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(people.involveType)){
            involeType.setVisibility(View.VISIBLE);
            setNameDisplay(involeType, people.dutyType);
            involeType.setText(people.involveType);
        }
        LogUtil.i("people is ",people.isPeople+"");

        return convertView;
    }

    private void setNameDisplay(TextView nametv, String dutytype){
             if (dutytype.equals(Constant.EVENT_DUTY_MAJOR) || dutytype.equals(Constant.EVENT_DUTY_MAJOR)) {
                 nametv.setTextColor(context.getResources().getColor(R.color.status_green));
                 nametv.setBackgroundResource(R.drawable.circle_corner_green_bg);
             }else if (dutytype.equals(Constant.EVENT_DUTY_WITNESS)) {
                 nametv.setTextColor(context.getResources().getColor(R.color.status_orange));
                 nametv.setBackgroundResource(R.drawable.circle_corner_orange_bg);
             }else if (dutytype.equals(Constant.EVENT_DUTY_KNOWN) || dutytype.equals(Constant.EVENT_DUTY_REPORT)) {
                 nametv.setTextColor(context.getResources().getColor(R.color.status_red));
                 nametv.setBackgroundResource(R.drawable.circle_corner_red_bg);
             }

    }

}
