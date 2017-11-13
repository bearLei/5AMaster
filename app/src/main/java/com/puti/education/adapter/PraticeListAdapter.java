package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.puti.education.R;
import com.puti.education.bean.Practice;

/**
 * Created by xjbin on 2017/5/16 0016.
 * 实践记录
 */

public class PraticeListAdapter extends BasicRecylerAdapter<Practice>{

    public PraticeListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pratice_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        CommonViewHolder cHolder = (CommonViewHolder) holder;
        TextView titleTv = cHolder.obtainView(R.id.pratice_title_tv);
        TextView contentTv = cHolder.obtainView(R.id.pratice_content_tv);
        TextView dateTv = cHolder.obtainView(R.id.pratice_date_tv);

        Practice practice = mList.get(position);
        titleTv.setText(practice.title);
        contentTv.setText(practice.content);
        dateTv.setText(practice.time);
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
