package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.puti.education.bean.Role;
import com.puti.education.R;

import java.util.List;


/**
 * 登录角色下拉选择
 */
public class PopListAdapter extends BasicRecylerAdapter<Role>{

    public PopListAdapter(Context myContext){
        super(myContext);
    }


    @Override
    public int getLayoutId() {
        return R.layout.rolelist_item_layout;
    }

    public void refershData(List<Role> lists){
        setDataList(lists);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        Role role = mList.get(position);

        TextView nameTv = viewHolder.obtainView(R.id.role_name_tv);
        nameTv.setText(role.name);
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
