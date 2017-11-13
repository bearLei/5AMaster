package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puti.education.R;
import com.puti.education.bean.Role;

import java.util.List;


/**
 * 字符串数组的下拉选择
 */
public class SpinnerPopAdapter extends BasicRecylerAdapter<String>{

    public SpinnerPopAdapter(Context myContext){
        super(myContext);
    }


    @Override
    public int getLayoutId() {
        return R.layout.rolelist_item_layout;
    }

    public void refershData(List<String> lists){
        setDataList(lists);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        String str = mList.get(position);

        viewHolder.setText(R.id.role_name_tv, str);
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
