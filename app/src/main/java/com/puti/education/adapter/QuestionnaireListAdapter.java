package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puti.education.bean.Questionnaire;
import com.puti.education.R;

import java.util.List;


/**
 * 问卷列表
 */
public class QuestionnaireListAdapter extends BasicRecylerAdapter<Questionnaire>{

    public QuestionnaireListAdapter(Context myContext){
        super(myContext);
    }


    @Override
    public int getLayoutId() {
        return R.layout.qt_item_layout;
    }

    public void refershData(List<Questionnaire> lists){
        setDataList(lists);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        Questionnaire question = mList.get(position);

        viewHolder.setText(R.id.qt_name_tv, question.name);
        viewHolder.setText(R.id.qt_status_tv, statusConvert(question.status));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnclickListener != null) {
                    myItemOnclickListener.onItemClick(position);
                }
            }
        });

    }



    private String statusConvert(String status){
        String result = "";
        if (status.equals("1")){
            result = "已回答";
        }else if (status.equals("2")){
            result = "未回答";
        }
        return result;
    }


}
