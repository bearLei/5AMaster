package com.puti.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Student;
import com.puti.education.util.ConfigUtil;
import com.puti.education.util.Constant;

/**
 *
 * Created by xjibn on 2017/5/25 0025.
 *
 */

public class ChildInfoListAdapter extends ABaseAdapter<Student>{

    public interface DelClickLitener{
        void onclick(int position);
    }

    private DelClickLitener delClickLitener;

    public void setDelClickLitener(DelClickLitener delClickLitener) {
        this.delClickLitener = delClickLitener;
    }

    private boolean isEnable;
    private boolean mIsEdit;

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void setEdit(boolean enable) {
        mIsEdit = enable;
    }

    public ChildInfoListAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_parent_child_info;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent, ViewHolder holder) {


        TextView nameTv = holder.obtainView(convertView,R.id.child_name_tv);
        TextView schoolTv = holder.obtainView(convertView,R.id.child_school_tv);
        TextView gradeTv = holder.obtainView(convertView,R.id.child_grade_tv);
        TextView majorTv = holder.obtainView(convertView,R.id.child_major_tv);
        TextView classTv = holder.obtainView(convertView,R.id.child_class_tv);
        TextView numTv = holder.obtainView(convertView,R.id.child_num_tv);
        FrameLayout delFrame = holder.obtainView(convertView,R.id.del_frame);

        if (mIsEdit){
            delFrame.setVisibility(View.VISIBLE);
        }else{
            delFrame.setVisibility(View.GONE);
        }

        Student info = mList.get(position);
        nameTv.setText(info.name);
        schoolTv.setText(info.school);
        gradeTv.setText("");
        classTv.setText(info.className);
        majorTv.setText(info.major);
        numTv.setText(info.number);

        nameTv.setEnabled(isEnable);
        schoolTv.setEnabled(isEnable);
        gradeTv.setEnabled(isEnable);
        majorTv.setEnabled(isEnable);
        classTv.setEnabled(isEnable);
        numTv.setEnabled(isEnable);

        delFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delClickLitener != null){
                    delClickLitener.onclick(position);
                }
            }
        });

        return convertView;
    }
}
