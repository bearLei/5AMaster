package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventDeailInvolvedPeople;
import com.puti.education.bean.Student;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by Administrator on 2017/4/20 0020.
 *
 *  涉事人选择
 */

public class InvolvedPeopleChooseListAdapter extends BasicRecylerAdapter<EventDeailInvolvedPeople> {

    public InvolvedPeopleChooseListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_choose_involved_people;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        EventDeailInvolvedPeople student = mList.get(position);

        CommonViewHolder commonViewHolder = (CommonViewHolder) holder;

        RelativeLayout itemContainerLinear = commonViewHolder.obtainView(R.id.item_container);
        ImageView mHeadImg = commonViewHolder.obtainView(R.id.person_head_img);
        TextView mNameTv = commonViewHolder.obtainView(R.id.person_name_tv);
        TextView mClassTv = commonViewHolder.obtainView(R.id.person_class_tv);
        itemContainerLinear.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,student.avatar,mHeadImg);
//        mNameTv.setText(student.name);
//        mClassTv.setText(student.major);
//        commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myItemOnclickListener != null){
//                    myItemOnclickListener.onItemClick(position);
//                }
//            }
//        });
    }

}
