package com.puti.education.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.ConsultExpert;
import com.puti.education.bean.EventAboutPeople;
import com.puti.education.util.ImgLoadUtil;
import com.puti.education.util.LogUtil;

/**
 * Created by xjbin on 2017/4/21 0021.
 * <p>
 * 添加事件-- 涉事人，知情人 adapter
 */

public class RecommondProfessorAdapter extends ABaseAdapter<ConsultExpert> {

    public RecommondProfessorAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_recommond_professor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        ConsultExpert people = mList.get(position);
        ImageView headImg = holder.obtainView(convertView, R.id.professor_img);
        TextView nameTv = holder.obtainView(convertView, R.id.professor_name_tv);
        TextView marjorTv = holder.obtainView(convertView, R.id.professor_marjor_tv);

        ImgLoadUtil.displayCirclePic(R.drawable.img_circle_bg, people.avatar, headImg);
        nameTv.setText(people.name);
        marjorTv.setText(people.desc);

        return convertView;
    }
}
