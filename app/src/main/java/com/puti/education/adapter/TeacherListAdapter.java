package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Teacher;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by Administrator on 2017/4/20 0020.
 *
 * 新增事件  老师，学生 列表适配器
 */

public class TeacherListAdapter extends BasicRecylerAdapter<Teacher> implements StickyHeaderAdapter{

    public TeacherListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_teacher_and_student_layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_head, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        HeaderHolder headerHolder = (HeaderHolder)viewholder;
        headerHolder.header.setText((mList.get(position).pinyin.charAt(0)+"").toUpperCase());
    }

    @Override
    public long getHeaderId(int position) {

        //这里面的是如果当前position与之前position重复（内部判断）  则不显示悬浮标题栏  如果不一样则显示标题栏
        char ch = mList.get(position).pinyin.charAt(0);

        if(lastChar == '\u0000'){

            lastChar = ch;

            return DisplayIndex;
        }else{

            if(lastChar == ch){

                return DisplayIndex;

            }else{

                lastChar = ch;
                DisplayIndex ++ ;
                return DisplayIndex;
            }

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Teacher teacher = mList.get(position);

        CommonViewHolder commonViewHolder = (CommonViewHolder) holder;

        LinearLayout itemContainerLinear = commonViewHolder.obtainView(R.id.item_container);
        ImageView mHeadImg = commonViewHolder.obtainView(R.id.person_head_img);
        TextView mNameTv = commonViewHolder.obtainView(R.id.person_name_tv);
        TextView mClassTv = commonViewHolder.obtainView(R.id.person_class_tv);
        ImageView mIvChoose = commonViewHolder.obtainView(R.id.iv_select);
        itemContainerLinear.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,teacher.avatar,mHeadImg);
        mNameTv.setText(teacher.name);
        mClassTv.setText(TextUtils.isEmpty(teacher.teachMajor) ?"暂无":teacher.teachMajor);
        mIvChoose.setVisibility(View.VISIBLE);

        if (teacher.isSelected){
            mIvChoose.setImageResource(R.mipmap.ic_item_selected);
        }else{
            mIvChoose.setImageResource(R.mipmap.ic_item_unselected);
        }

        mIvChoose.setTag(position);
        mIvChoose.setOnClickListener(mCheckListener);
        commonViewHolder.itemView.setTag(position);
        commonViewHolder.itemView.setOnClickListener(mCheckListener);

//        commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myItemOnclickListener != null){
//                    myItemOnclickListener.onItemClick(position);
//                }
//            }
//        });
    }

    private View.OnClickListener mCheckListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int posi = (Integer)v.getTag();
            if (myItemOnclickListener != null){
                myItemOnclickListener.onItemClick(posi);
            }
        }
    };

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.head_tv);
        }
    }

    /**
     * 获得指定首字母的位置
     * @param ch
     * @return
     */
    public int getPositionForSection(char ch){

        for (int i = 0; i < getItemCount(); i++) {
            char firstChar = mList.get(i).pinyin.charAt(0);
            if (firstChar == ch) {
                return i;
            }
        }
        return -1;

    }
}
