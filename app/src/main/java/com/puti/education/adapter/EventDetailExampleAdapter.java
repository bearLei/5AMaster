package com.puti.education.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.bean.Example;

/**
 * Created by xjbin on 2017/4/25 0025.
 *
 *  参考处理方法例子
 */

public class EventDetailExampleAdapter  extends ABaseAdapter<Example>{

    public EventDetailExampleAdapter(Context context) {
        super(context);
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_event_detail_example_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {

        Example example = mList.get(position);

        TextView exampleNameTv = holder.obtainView(convertView,R.id.example_name_tv);
        TextView exampleDateTv = holder.obtainView(convertView,R.id.example_date_tv);
        exampleNameTv.setText(example.id + "");
        exampleDateTv.setText(TextUtils.isEmpty(example.time)? "暂无":example.time);
        return convertView;
    }
}
