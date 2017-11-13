package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventBean;
import com.puti.education.bean.ReportBean;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;


/**
 * Created by yuzhihu on 2017/9/20 0019.
 *
 * 学生端举报列表adapter
 */

public class ReportListAdapter extends BasicRecylerAdapter<ReportBean>{

    private int mRole = Constant.ROLE_STUDENT;
    public ReportListAdapter(Context context, int role) {
        super(context);
        mRole = role;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_student_reportlist;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView tvTime = cHolder.obtainView(R.id.tv_time);
        TextView tvStatu= cHolder.obtainView(R.id.event_status_tv);
        TextView tvDesc = cHolder.obtainView(R.id.tv_desc);
        TextView tvName = cHolder.obtainView(R.id.tv_name);
        ImageView tvAvatar = cHolder.obtainView(R.id.img_avatar);

        LinearLayout layoutPhotos = cHolder.obtainView(R.id.layout_photos);
        ImageView tvPhoto1 = cHolder.obtainView(R.id.img_report1);
        ImageView tvPhoto2 = cHolder.obtainView(R.id.img_report2);
        ImageView tvPhoto3 = cHolder.obtainView(R.id.img_report3);


        ReportBean info = mList.get(position);
        tvTime.setText(info.time);
        tvDesc.setText(info.desc);

        if (mRole == Constant.ROLE_TEACHER){
            tvStatu.setVisibility(View.VISIBLE);
            setStatusDisplay(info.confirm, tvStatu);
        }else{
            tvStatu.setVisibility(View.GONE);
        }

        if (info.records != null && info.records.size() > 0){
            tvPhoto1.setVisibility(View.GONE);
            tvPhoto2.setVisibility(View.GONE);
            tvPhoto3.setVisibility(View.GONE);

            layoutPhotos.setVisibility(View.VISIBLE);
            tvPhoto1.setVisibility(View.VISIBLE);
            ImgLoadUtil.displayPic(R.mipmap.ic_picture, info.records.get(0).url, tvPhoto1);
            if (info.records.size() > 1){
                tvPhoto2.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayPic(R.mipmap.ic_picture, info.records.get(1).url, tvPhoto2);
            }
            if (info.records.size() > 2){
                tvPhoto3.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayPic(R.mipmap.ic_picture, info.records.get(2).url, tvPhoto3);
            }
        }else{
            layoutPhotos.setVisibility(View.GONE);
        }


        if (info.user != null){
            tvName.setText(info.user.name);
            ImgLoadUtil.displayPic(R.mipmap.ic_avatar_middle,info.user.avatar, tvAvatar);
        }

        cHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (myItemOnclickListener != null){
                   myItemOnclickListener.onItemClick(position);
               }
            }
        });
    }

    public void setStatusDisplay(int status, TextView tvstatus){

        switch(status){
            case 0:
                tvstatus.setText("未确认事件");
                tvstatus.setBackgroundResource(R.drawable.circle_corner_red_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_red));
                break;
            case 1:
                tvstatus.setText("已确认事件");
                tvstatus.setBackgroundResource(R.drawable.circle_corner_green_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_green));
                break;
            case 2:
                tvstatus.setText("已拒绝事件");
                tvstatus.setBackgroundResource(R.drawable.circle_corner_dark_bg);
                tvstatus.setTextColor(mContext.getResources().getColor(R.color.status_dark));
                break;
        }
    }

}
