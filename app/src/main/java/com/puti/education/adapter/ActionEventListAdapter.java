package com.puti.education.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventBean;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.DisPlayUtil;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.Key;


/**
 * Created by icebery on 2017/5/10 0019.
 *
 * 学生端 行为事件列表 适配器
 */

public class ActionEventListAdapter extends BasicRecylerAdapter<EventBean>{

    public ActionEventListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_teacher_eventlist;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView eventTypeTv = cHolder.obtainView(R.id.eventtype_tv);
        TextView eventStatusTv = cHolder.obtainView(R.id.event_status_tv);
        TextView eventDesTv = cHolder.obtainView(R.id.event_des_tv);
        TextView eventAddressTv = cHolder.obtainView(R.id.event_address_tv);
        TextView eventDateTv = cHolder.obtainView(R.id.event_date_tv);

        TextView eventName = cHolder.obtainView(R.id.tv_name_duty);
        TextView eventClass= cHolder.obtainView(R.id.tv_class_grade);
        ImageView imgAvatar= cHolder.obtainView(R.id.img_avatar);

        final EventBean info = mList.get(position);
        eventTypeTv.setText(info.eventtypename);
        setStatusDisplay(info.status, eventStatusTv, info.statusname);

        eventDesTv.setText(info.eventdescription);
        eventAddressTv.setText(info.evenaddress);
        eventDateTv.setText(info.eventtime);
        eventName.setText(info.studentname + "(" +info.involvedtype+")");
        eventClass.setText(info.classname);

        ImgLoadUtil.displayPic(R.mipmap.ic_avatar_middle,info.studentphoto, imgAvatar);


        cHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null){
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });
    }

    private void setAvatarMore(EventBean eBean, LinearLayout layoutContaner){
        View subView = null;
        LinearLayout subLayout = null;
        layoutContaner.removeAllViews();
        int currentRow = -1;
        int size = eBean.involvedPeople.size();
        for (int j= 0; j<size; j++){
            int row  = (j/5);
            subView = addAvatar(eBean.involvedPeople.get(j));
            if (currentRow == row){
                if (subLayout != null){
                    subLayout.addView(subView);
                }
            }else{
                currentRow = row;
                subLayout = addSubLayout();
                subLayout.addView(subView);
                layoutContaner.addView(subLayout);
            }
        }
    }

    private LinearLayout addSubLayout(){
        LinearLayout subLayout = null;
        ViewGroup.LayoutParams params;
        subLayout = new LinearLayout(mContext);
        params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisPlayUtil.dip2px(mContext, 99));
        subLayout.setLayoutParams(params);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        return subLayout;
    }

    private View addAvatar(EventBean.InvolvedPeople involver){
        View itemView;
        ViewGroup.LayoutParams params;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_subitem_avatar, null);
        params = new ViewGroup.LayoutParams(DisPlayUtil.dip2px(mContext, 65), DisPlayUtil.dip2px(mContext, 99));
        itemView.setLayoutParams(params);
        ImageView avatar = (ImageView)itemView.findViewById(R.id.img_avatar);
        TextView nameTv = (TextView)itemView.findViewById(R.id.tv_name);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,involver.avatar, avatar);
        nameTv.setText(involver.name);

        return itemView;
    }

    public void setStatusDisplay(int status, TextView tvstatus, String statusname){
        tvstatus.setText(statusname);
        switch(status){
            case Constant.EVENT_STATUS_FINISHED:
            case Constant.EVENT_STATUS_REFUSE:
                tvstatus.setBackgroundResource(R.drawable.circle_corner_dark_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_dark));
                break;
            case Constant.EVENT_STATUS_UNCONFIRM:
                tvstatus.setBackgroundResource(R.drawable.circle_corner_red_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_red));
                break;
            case Constant.EVENT_STATUS_CONFIRMED:
                tvstatus.setBackgroundResource(R.drawable.circle_corner_green_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_green));
                break;
            case Constant.EVENT_STATUS_UNCHECK:
                tvstatus.setBackgroundResource(R.drawable.circle_corner_orange_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_orange));
                break;
            case Constant.EVENT_STATUS_UNTRACK:
                tvstatus.setBackgroundResource(R.drawable.circle_corner_blue_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_blue));
                break;
        }
    }


}
