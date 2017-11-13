package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.DetectiveBean;
import com.puti.education.bean.ReportBean;
import com.puti.education.util.Constant;
import com.puti.education.util.ImgLoadUtil;


/**
 * Created by yuzhihu on 2017/9/20 0019.
 *
 * 教师端巡检列表adapter
 */

public class DetectiveListAdapter extends BasicRecylerAdapter<DetectiveBean>{

    public DetectiveListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_teacher_detectivelist;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView tvTime = cHolder.obtainView(R.id.tv_time);
        TextView tvDesc = cHolder.obtainView(R.id.tv_desc);
        TextView tvStatu= cHolder.obtainView(R.id.event_status_tv);

        LinearLayout layoutPhotos = cHolder.obtainView(R.id.layout_photos);
        ImageView tvPhoto1 = cHolder.obtainView(R.id.img_photo1);
        ImageView tvPhoto2 = cHolder.obtainView(R.id.img_photo1);
        ImageView tvPhoto3 = cHolder.obtainView(R.id.img_photo1);

        DetectiveBean info = mList.get(position);
        tvTime.setText(info.time);
        tvDesc.setText(info.result);

        setStatusDisplay(info.confirm, tvStatu);

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
