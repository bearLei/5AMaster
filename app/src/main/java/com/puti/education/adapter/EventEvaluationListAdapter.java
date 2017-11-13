package com.puti.education.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventEvaluationBean;
import com.puti.education.bean.Practice;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.widget.RatingSmallBarView;

/**
 * Created by icebery on 2017/5/24 0028.
 *
 * 事件互评列表
 */

public class EventEvaluationListAdapter extends BasicRecylerAdapter<EventEvaluationBean>{

    public EventEvaluationListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_event_evaluation_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        EventEvaluationBean record = mList.get(position);
        CommonViewHolder cHolder = (CommonViewHolder) holder;

        TextView mDate = cHolder.obtainView(R.id.tv_time);
        TextView mType = cHolder.obtainView(R.id.tv_type);
        TextView mName = cHolder.obtainView(R.id.tv_name);
        TextView mContent = cHolder.obtainView(R.id.tv_desc);
        TextView mAddress = cHolder.obtainView(R.id.tv_address);
        ImageView mAvatar = cHolder.obtainView(R.id.iv_avatar);
        RatingSmallBarView mRatebar = cHolder.obtainView(R.id.rbv_event_level);

        mContent.setText(record.eventDescription);
        mDate.setText(record.eventTime);
        mType.setText(record.eventType);
        mName.setText(record.studentName);
        mAddress.setText(record.eventAddress);
        mRatebar.setStar(record.eventLevel, true);
        mRatebar.setClickable(false);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_middle, record.studentAvatar, mAvatar);

        cHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null){
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });
    }

}
