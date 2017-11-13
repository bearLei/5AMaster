package com.puti.education.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.bean.EventBean;
import com.puti.education.ui.uiTeacher.TeacherEventDetailActivity;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.R;
import com.puti.education.util.Key;
import com.puti.education.util.ToastUtil;
import com.puti.education.widget.ItemGridView;

import java.util.List;


/**
 * Created by xjbin on 2017/4/19 0019.
 *
 * 教师端 事件列表 适配器
 */

public class EventListAdapter extends BasicRecylerAdapter<EventBean>{

    public EventListAdapter(Context context) {
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

        EventBean info = mList.get(position);
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
