package com.puti.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puti.education.R;
import com.puti.education.bean.Questionnaire;
import com.puti.education.bean.SynergyBean;

import java.util.List;


/**
 * 培训记录和实践记录
 */
public class SynergyListAdapter extends BasicRecylerAdapter<SynergyBean>{

    public SynergyListAdapter(Context myContext){
        super(myContext);
    }


    @Override
    public int getLayoutId() {
        return R.layout.qt_item_layout;
    }

    public void refershData(List<SynergyBean> lists){
        setDataList(lists);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        SynergyBean synergy = mList.get(position);

        viewHolder.setText(R.id.qt_name_tv, synergy.title);
        viewHolder.setText(R.id.qt_status_tv, statusConvert(synergy.time));
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
        String result = "未填写";
        if (status.equals("1")){
            result = "正在分析中";
        }else if (status.equals("2")){
            result = "已出结果";
        }
        return result;
    }


}
