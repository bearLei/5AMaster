package com.puti.education.nation;
/**
 * Created by lenovo on 2017/12/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puti.education.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * created by lei at 2017/12/16
 */
public class NationChooseAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;

    public NationChooseAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (mList != null) size = mList.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View  view = null;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_nation_choose,null,false);
        }else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder == null){
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
        }
        holder.title.setText(mList.get(position));
        return view;
    }

    class ViewHolder{
        TextView title;
    }
}
