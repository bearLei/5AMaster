package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.ConsultExpert;
import com.puti.education.util.ImgLoadUtil;

/**
 * Created by xjbin on 2017/4/25 0025
 *
 *  专家
 *
 */

public class ProfessorListAdapter extends ABaseAdapter<ConsultExpert>{

    public interface BtnClickListener{
        void onBtnClick(int position);
    }

    BtnClickListener btnClickListener;

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public ProfessorListAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_event_detail_professor_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent, ViewHolder holder) {

        ConsultExpert consultExpert = mList.get(position);
        ImageView img = holder.obtainView(convertView, R.id.professor_img);
        TextView nameTv = holder.obtainView(convertView,R.id.professor_name_tv);
        TextView prosessionTv = holder.obtainView(convertView,R.id.profession_name_tv);
        Button consultBtn = holder.obtainView(convertView,R.id.consult_btn);

        ImgLoadUtil.displayCirclePic(R.mipmap.ic_avatar_default,consultExpert.avatar,img);
        nameTv.setText(TextUtils.isEmpty(consultExpert.name)? "暂无":consultExpert.name);
        prosessionTv.setText(TextUtils.isEmpty(consultExpert.desc)? "暂无":consultExpert.desc);

        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnClickListener != null){
                    btnClickListener.onBtnClick(position);
                }
            }
        });

        return convertView;
    }
}
