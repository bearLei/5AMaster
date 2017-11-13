package com.puti.education.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.bean.Practice;
import com.puti.education.util.Constant;

/**
 * Created by icebery on 2017/5/24 0028.
 *
 * 实践培训记录
 *
 *  1 培训记录  2 实践记录
 */

public class PracticeTrainListAdapter extends BasicRecylerAdapter<Practice>{

    public PracticeTrainListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_practice_train_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Practice record = mList.get(position);
        CommonViewHolder cHolder = (CommonViewHolder) holder;

        TextView mDate = cHolder.obtainView(R.id.tv_time);
        TextView mType = cHolder.obtainView(R.id.tv_type);
        TextView mTheme= cHolder.obtainView(R.id.tv_theme);
        TextView mContent = cHolder.obtainView(R.id.tv_content);
        TextView mAddress = cHolder.obtainView(R.id.tv_address);

        mContent.setText(record.content);
        mDate.setText(record.time);
        if (record.type.equals("1")){
            mType.setBackgroundResource(R.drawable.circle_corner_orange_bg);
            mType.setTextColor(mContext.getResources().getColor(R.color.status_orange));
        }else{
            mType.setBackgroundResource(R.drawable.circle_corner_green_bg);
            mType.setTextColor(mContext.getResources().getColor(R.color.status_green));
        }
        mType.setText(record.typename);
        mTheme.setText(record.title);
        mAddress.setText(record.address);

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
