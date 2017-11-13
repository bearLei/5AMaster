package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puti.education.R;
import com.puti.education.bean.Schools;

import java.util.List;


/**
 * 登录角色下拉选择
 */
public class SchoolListAdapter extends BasicRecylerAdapter<Schools.School>{

    public SchoolListAdapter(Context myContext){
        super(myContext);
    }


    @Override
    public int getLayoutId() {
        return R.layout.rolelist_item_layout;
    }

    public void refershData(List<Schools.School> lists){
        setDataList(lists);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        Schools.School school = mList.get(position);

        viewHolder.setText(R.id.role_name_tv, school.name);
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
