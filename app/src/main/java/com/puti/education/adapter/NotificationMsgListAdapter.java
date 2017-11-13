package com.puti.education.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.NotificationMsg;

/**
 * Created by xjibn on 2017/4/28 0028.
 *
 * 通知中心
 *
 *  1 系统通知  2 事件消息 其它？
 */

public class NotificationMsgListAdapter extends BasicRecylerAdapter<NotificationMsg>{

    public NotificationMsgListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_notifycation_msg_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        NotificationMsg msg = mList.get(position);
        CommonViewHolder cHolder = (CommonViewHolder) holder;

        TextView mTitleTv = cHolder.obtainView(R.id.msg_title_tv);
        TextView mDateTv = cHolder.obtainView(R.id.msg_date_tv);
        TextView mDesTv = cHolder.obtainView(R.id.msg_des_tv);
        mTitleTv.setText(msg.title);
        mDateTv.setText(msg.time);
        mDesTv.setText(msg.content);

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
