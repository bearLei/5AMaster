package com.puti.education.util.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.puti.education.R;

import java.util.List;


/**
 * Created by Sai on 15/8/9.
 */
public class DialogViewAdapter extends BaseAdapter {
    private List<String> mDatas;

    public DialogViewAdapter(List<String> datas){
        this.mDatas =datas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String data= mDatas.get(position);
        Holder holder=null;
        View view =convertView;
        if(view==null){
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            view=inflater.inflate(R.layout.item_dialogbutton, null);
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialogbutton,null);
            holder=creatHolder(view);
            view.setTag(holder);
        }
        else{
            holder=(Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(),data,position);
        return view;
    }
    public Holder creatHolder(View view){
        return new Holder(view);
    }
    class Holder {
        private TextView tvAlert;

        public Holder(View view){
            tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }
        public void UpdateUI(Context context, String data, int position){
            tvAlert.setText(data);
                tvAlert.setTextColor(context.getResources().getColor(R.color.base_333333));

        }
    }
}