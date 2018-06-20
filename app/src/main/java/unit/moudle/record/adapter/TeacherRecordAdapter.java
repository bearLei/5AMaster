package unit.moudle.record.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.puti.education.R;
import com.puti.education.base.InflateService;

import java.util.ArrayList;

import unit.moudle.record.holder.ClaRecordInfo;

/**
 * Created by lei on 2018/6/20.
 */

public class TeacherRecordAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ClaRecordInfo> mData;

    public TeacherRecordAdapter(Context context, ArrayList<ClaRecordInfo> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount(){
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = InflateService.g().inflate(R.layout.puti_record_class_item);
            holder.num = (TextView) convertView.findViewById(R.id.num_index);
            holder.className = (TextView) convertView.findViewById(R.id.class_name);
            holder.major = (TextView) convertView.findViewById(R.id.major);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClaRecordInfo info = mData.get(position);
        holder.num.setText(String.valueOf(info.getNum()));
        holder.className.setText(info.getClassName());
        holder.major.setText(info.getMajor());

        return convertView;
    }

    public class ViewHolder{
        public TextView num,className,major;
    }

}
