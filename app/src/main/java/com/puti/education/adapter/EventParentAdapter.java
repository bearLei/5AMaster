package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/4/21 0021.
 *
 *  添加事件-- 涉事人，知情人 adapter
 *
 */

public class EventParentAdapter extends ABaseAdapter<ParentFromStudent> {

    public EventParentAdapter(Context context) {
        super(context);
    }

    private class ItemClickLinstener implements View.OnClickListener{

        ImageView checImg;
        int position;

        public ItemClickLinstener(ImageView checImg,int position){
            this.checImg = checImg;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ParentFromStudent parentFromStudent = mList.get(position);
            if (parentFromStudent.isSelected){
                parentFromStudent.isSelected = false;
                checImg.setImageResource(R.mipmap.ic_item_unselected);
            }else{
                parentFromStudent.isSelected = true;
                checImg.setImageResource(R.mipmap.ic_item_selected);
            }

        }
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        ParentFromStudent parentFromStudent = mList.get(position);
        LinearLayout itemContainer = holder.obtainView(convertView,R.id.item_container);
        ImageView headImg = holder.obtainView(convertView,R.id.grid_head_img);
        TextView nameTv = holder.obtainView(convertView,R.id.grid_head_name_tv);
        ImageView signImg = holder.obtainView(convertView,R.id.prent_sign_img);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,parentFromStudent.avatar,headImg);
        nameTv.setText(TextUtils.isEmpty(parentFromStudent.name) ?"暂无":parentFromStudent.name);
        itemContainer.setOnClickListener(new ItemClickLinstener(signImg,position));



        return convertView;
    }
}
