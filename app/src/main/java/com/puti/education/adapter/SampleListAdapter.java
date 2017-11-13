package com.puti.education.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.NotificationMsg;
import com.puti.education.bean.SampleInfo;

/**
 *
 * 显示个案分析和措施对策
 */

public class SampleListAdapter extends BasicRecylerAdapter<SampleInfo>{

    public SampleListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_sample_detail;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        SampleInfo msg = mList.get(position);
        CommonViewHolder cHolder = (CommonViewHolder) holder;

        TextView mTvSample = cHolder.obtainView(R.id.tv_msg_content);
        TextView mTvExpend = cHolder.obtainView(R.id.tv_expend);

        mTvSample.setText(msg.value);

        mTvExpend.setTag(R.id.adapter_pos, position);
        mTvExpend.setOnClickListener(mContextLister);

        if (msg.isExpend){
            mTvExpend.setText("缩起");
            mTvSample.setEllipsize(null); // 展开
            mTvSample.setSingleLine(false);
        }else{
            mTvExpend.setText("展开");
            mTvSample.setEllipsize(TextUtils.TruncateAt.END);
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


    private View.OnClickListener mContextLister = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int pos = (Integer)(v.getTag(R.id.adapter_pos));
            SampleInfo msgObj = mList.get(pos);

            Layout layout = ((TextView)v).getLayout();
            if (layout != null) {
                int lines = layout.getLineCount();
                if (lines > 0) {
                    if (layout.getEllipsisCount(lines - 1) > 0) {
                        Log.d("Sandy", "Text is ellipsized"+lines + " l.getEllipsisCount(lines - 1): " + layout.getEllipsisCount(lines - 1));
                        msgObj.isExpend = true;
                        notifyDataSetChanged();
                    }else{
                        Log.d("Sandy", "Text is not ellipsized"+lines);
                        msgObj.isExpend = false;
                    }
                }
            }
        }
    };
}
