package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventAddress;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class EventAddressListAdapter extends BasicRecylerAdapter<EventAddress>{

    public EventAddressListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_dropview_item_layout;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        EventAddress eventAddress = (EventAddress)mList.get(position);

        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        TextView textView = (TextView) viewHolder.obtainView(R.id.item_name_tv);
        textView.setText(eventAddress.address);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null) {
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });
    }
}
